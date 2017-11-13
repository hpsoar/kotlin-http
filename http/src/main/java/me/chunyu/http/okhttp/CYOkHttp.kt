package me.chunyu.http.okhttp

import android.net.TrafficStats
import me.chunyu.http.core.*
import me.chunyu.http.core.Request
import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.common.Progress
import me.chunyu.http.core.interfaces.HttpClient
import me.chunyu.http.core.interfaces.RequestExecutorContext
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class CYOKResponse(val okHttpResponse: Response?, error: KotError?) : CYResponse(error) {
    var success = false
    init {
        if (okHttpResponse != null && okHttpResponse.code() < 400) {
            success = true
        }
    }

    override fun isSuccess(): Boolean {
        return success
    }
}

fun Request.getOKRequestBody(): RequestBody {
    return FormBody.Builder().build()
}

fun PostRequest.okMediaType(): MediaType {
    var customMediaType : MediaType? = null
    if (customContentType != null)  {
        customMediaType = MediaType.parse(customContentType)
    }

    if (customMediaType == null) {
        if (applicationJsonString != null) {
            return MediaType.parse("application/json; charset=utf-8")
        }
        else {
            return MediaType.parse("text/x-markdown; charset=utf-8")
        }
    }
    return customMediaType
}

fun PostRequest.getOKRequestBody(): RequestBody {
    val customMediaType = okMediaType()

    if (applicationJsonString != null) {
        return RequestBody.create(customMediaType, applicationJsonString)
    } else if (stringBody != null) {
        return RequestBody.create(customMediaType, stringBody)
    } else if (file != null) {
        return RequestBody.create(customMediaType, file)
    } else if (bytes != null) {
        return RequestBody.create(customMediaType, bytes)
    } else {
        val builder = FormBody.Builder()

        try {
            bodyParameterMap.entries.forEach { entry -> builder.add(entry.key, entry.value) }
            urlEncodedFormBodyParameterMap.entries.forEach { entry -> builder.addEncoded(entry.key, entry.value) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return builder.build()
    }
}

fun MultipartRequest.getOKRequestBody(): RequestBody {
    val customMediaType = okMediaType()

    val builder = MultipartBody.Builder().setType(customMediaType ?: MultipartBody.FORM)

    try {
        for ((key, value) in multiPartFileMap) {
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"$key\""),
                    RequestBody.create(null, value))
        }
        for ((key, value) in multiPartFileMap.entries) {
            val fileName = value.name
            val fileBody = RequestBody.create(MediaType.parse(KotUtils.getMimeType(fileName)),
                    value)
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name=\"$key\"; filename=\"$fileName\""),
                    fileBody)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return builder.build()
}


abstract class CYOKContext(request: Request) : RequestExecutorContext(request) {
    var call: Call? = null

    override fun cancel(forceCancel: Boolean): Boolean {
        return isCancelled
    }

    fun getHeaders(): Headers {
        val builder: Headers.Builder = Headers.Builder()
        try {
            request.headersMap.entries.forEach { entry -> builder.add(entry.key, entry.value) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return builder.build()
    }

    fun addHeadersToRequestBuilder(builder: okhttp3.Request.Builder.Builder) {

        val requestHeaders = getHeaders()

        builder.headers(requestHeaders)

        request.userAgent = request.userAgent ?: CYOkHttp.userAgent

        if (request.userAgent != null && !requestHeaders.names().contains(KotConstants.USER_AGENT)) {
            builder.addHeader(KotConstants.USER_AGENT, request.userAgent)
        }
    }
}

class CYOKSimpleContext(request: Request) : CYOKContext(request) {
    override fun execute(): CYResponse {

        try {
            var okHttpRequest: okhttp3.Request? = null
            var okHttpResponse: Response? = null

            var builder: okhttp3.Request.Builder.Builder = Request.Builder().url(request.getFormattedUrl())

            addHeadersToRequestBuilder(builder)

            var requestBody = request.getOKRequestBody()
            when (request.method) {
                Method.GET -> {
                    builder = builder.get()
                }
                Method.POST -> {
                    builder = builder.post(requestBody)
                }
                Method.PUT -> {
                    builder = builder.put(requestBody)
                }
                Method.DELETE -> {
                    builder = builder.delete(requestBody)
                }
                Method.HEAD -> {
                    builder = builder.head()
                }
                Method.PATCH -> {
                    builder = builder.patch(requestBody)
                }
            }

            request.cacheControl?.let { builder.cacheControl(it) }

            okHttpRequest = builder.build()

            call = CYOkHttp.sOkHttpClient.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()
            val startBytes = TrafficStats.getTotalRxBytes()

            okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(requestBody, okHttpResponse, startBytes, timeTaken)

            return CYOKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}

class CYOKDownloadContext(request: Request) : CYOKContext(request) {
    override fun execute(): CYResponse {
        try {
            val okHttpRequest: okhttp3.Request
            val okHttpResponse: Response?
            var builder = Request.Builder().url(request.getFormattedUrl())
            addHeadersToRequestBuilder(builder)
            builder = builder.get()

            request.cacheControl?.let { builder.cacheControl(it) }

            okHttpRequest = builder.build()

            val okHttpClient =
                    CYOkHttp.sOkHttpClient.newBuilder().addInterceptor { chain ->
                        val response: Response = chain.proceed(chain.request())
                        response.newBuilder()
                                .body(ResponseProgressBody(response.body(), {
                                    // progress callback
                                    bytesDownloaded, totalBytes ->
                                    request.callbck?.downloadProgress(Progress(bytesDownloaded, totalBytes))
                                }))
                                .build()
                    }?.build()

            call = okHttpClient?.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()
            val startBytes = TrafficStats.getTotalRxBytes()

            okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(null, okHttpResponse, startBytes, timeTaken)

            return CYOKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}

class CYOKMultipartContext(request: Request) : CYOKContext(request) {

    override fun execute(): CYResponse {
        try {
            val okHttpRequest: okhttp3.Request
            val okHttpResponse: Response?

            var builder = Request.Builder().url(request.getFormattedUrl())

            addHeadersToRequestBuilder(builder)

            val requestBody: RequestBody = request.getOKRequestBody()

            val requestBodyLength = requestBody.contentLength()

            builder = builder.post(CYRequestProgressBody(requestBody, {
                bytesUploaded, totalBytes ->
                request.callbck?.uploadProgress(Progress(bytesUploaded, totalBytes))
            }))

            request.cacheControl?.let { builder.cacheControl(it) }

            okHttpRequest = builder.build()

            call = CYOkHttp.sOkHttpClient.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()

            okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(requestBody, okHttpResponse, startBytes = 0, timeTaken = timeTaken)

            return CYOKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}


fun Request.logRequestResult(requestBody: RequestBody?, okHttpResponse: Response?, startBytes: Long, timeTaken: Long) {
    var bytesSent = 0L
    var bytesReceived = 0L
    if (okHttpResponse?.cacheResponse() == null) {
        val finalBytes = TrafficStats.getTotalRxBytes()
        val diffBytes: Long?
        if (finalBytes == TrafficStats.UNSUPPORTED.toLong() || startBytes == TrafficStats.UNSUPPORTED.toLong()) {
            diffBytes = okHttpResponse?.body()?.contentLength()
        } else {
            diffBytes = finalBytes - startBytes
        }
        ConnectionClassManager.instance?.updateBandwidth(diffBytes, timeTaken)

        if (requestBody != null && requestBody.contentLength() > 0L) {
            bytesSent = requestBody.contentLength()
        }

        bytesReceived = diffBytes ?: 0
    } else {
        // KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, bytesSent, bytesReceived, false)

        if (okHttpResponse?.networkResponse() == null) {
            // KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, 0, 0, true)
        } else {
            var bytesSent = -1L
            if (requestBody != null && requestBody.contentLength() != 0L)
                bytesSent = requestBody.contentLength()
            // KotUtils.sendAnalytics(kotRequest.analyticsListener, timeTaken, bytesSent, 0, true)
        }
    }
}


/**
 * Created by huangpeng on 12/11/2017.
 */
class CYOkHttp : HttpClient {
    companion object {
        // you can customize in your code
        var sOkHttpClient: OkHttpClient = defaultOkHttpClient()
        var userAgent: String? = null

        fun defaultOkHttpClient(): OkHttpClient = OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    override fun executorContext(request: Request): RequestExecutorContext {
        if (request as? DownloadRequest != null) {
            return CYOKDownloadContext(request)
        } else if (request as? MultipartRequest != null) {
            return CYOKMultipartContext(request)
        }
        return CYOKSimpleContext(request)
    }
}

