package me.chunyu.http.okhttp

import android.net.TrafficStats
import me.chunyu.http.core.*
import okhttp3.*

/**
 * Created by Roger Huang on 13/11/2017.
 */

fun KotRequest.getOKRequestBody(): RequestBody {
    return FormBody.Builder().build()
}

fun KotPostRequest.okMediaType(): MediaType {
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

fun KotPostRequest.getOKRequestBody(): RequestBody {
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

fun KotMultipartRequest.getOKRequestBody(): RequestBody {
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



fun KotRequest.logRequestResult(requestBody: RequestBody?, okHttpResponse: Response?, startBytes: Long, timeTaken: Long) {
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

