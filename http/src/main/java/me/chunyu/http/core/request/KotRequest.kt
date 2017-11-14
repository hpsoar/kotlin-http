package me.chunyu.http.core

import me.chunyu.http.core.builder.RequestBuilder
import me.chunyu.http.core.common.Method
import me.chunyu.http.core.common.Priority
import me.chunyu.http.core.common.Progress
import me.chunyu.http.core.interfaces.KotHttpClient
import me.chunyu.http.core.interfaces.RequestExecutorContext
import me.chunyu.http.core.request.TCallback
import me.chunyu.http.core.request.TResponse
import okhttp3.*
import java.util.concurrent.Executor

/**
 * Created by huangpeng on 12/11/2017.
 */
open class KotRequest(builder: RequestBuilder) {

    companion object {
        var httpClient: KotHttpClient? = null
    }

    val url: String
    val method: Method
    var baseUrl: String? =  null
    var sequenceNumber: Int = 0

    var priority: Priority = Priority.MEDIUM
    var tag: Any? = null
    var headersMap: MutableMap<String, String> = mutableMapOf()
    var queryParameterMap: MutableMap<String, String> = mutableMapOf()
    var pathParameterMap: MutableMap<String, String> = mutableMapOf()
    var cacheControl: CacheControl? = null
    var userAgent: String? = null

    var httpClient: KotHttpClient? = null

    var executor: Executor? = null

    var context: RequestExecutorContext? = null

    private var callback: KotCallback? = null

    init {
        url = builder.url
        method = builder.method
        priority = builder.priority
        tag = builder.tag
        headersMap = builder.headersMap
        queryParameterMap = builder.queryParameterMap
        pathParameterMap = builder.pathParameterMap
        cacheControl = builder.cacheControl
        httpClient = builder.httpClient
        userAgent = builder.userAgent
    }

    fun getFormattedUrl(): String {
        var tempUrl = url

        pathParameterMap.entries.forEach { entry -> tempUrl = tempUrl.replace("{${entry.key}}", entry.value) }

        val urlBuilder: HttpUrl.Builder = HttpUrl.parse(tempUrl).newBuilder()

        queryParameterMap.entries.forEach { entry -> urlBuilder.addQueryParameter(entry.key, entry.value) }

        return urlBuilder.build().toString()

    }

    fun cancel(forceCancel: Boolean): Boolean {
        context?.cancel(forceCancel)
        return context?.isCancelled ?: false
    }

    fun currentHttpClient(): KotHttpClient? {
        return httpClient ?: KotRequest.httpClient
    }

    fun async(callback: KotCallback?) {
        this.callback = callback

        val httpClient = currentHttpClient()
        if (httpClient != null) {

            context = httpClient.executorContext(this)

            KotRequestQueue.INSTANCE.addRequest(this)
        } else {
            deliverError(noHttpClientError())
        }
    }

    fun sync(callback: KotCallback?): KotResponse {
        val httpClient = currentHttpClient()
        if (httpClient != null) {
            this.callback = callback

            val context = httpClient.executorContext(this)

            this.context = context

            return context.execute()
        }

        return KotResponse(noHttpClientError())
    }

    fun<T> sync(callback: TCallback<T>) : TResponse<T> {

        val response = sync(wrapTCallback(callback))

        return convertResponse(response, callback)
    }

    fun<T> async(callback: TCallback<T>) {
        async(wrapTCallback(callback))
    }

    fun<T> wrapTCallback(callback: TCallback<T>): KotCallback {
        return object : KotCallback {
            override fun onSuccess(response: KotResponse) {
                val resp = convertResponse(response, callback)
                callback.onCallback(resp)
            }

            override fun onError(error: KotError) {
                callback.onCallback(TResponse(null, error))
            }

            override fun uploadProgress(progress: Progress) {
                callback.onProgress(progress)
            }

            override fun downloadProgress(progress: Progress) {
                callback.onProgress(progress)
            }
        }
    }

    fun<T> convertResponse(response: KotResponse, callback: TCallback<T>) : TResponse<T> {
        val convertor = KotResponse.convertorFactory?.objectCovertor<T>(callback.getType())

        if (convertor != null) {
            return convertor.convertResponse(response)
        }

        return TResponse(response, KotError("please set KotResponse.convertorFactory"))
    }

    fun noHttpClientError(): KotError {
        val error = KotError()
        error.errorCode = -1
        error.errorDetail = "httpClient is not set for either KotRequest or current request object"

        return error
    }

    fun updateUploadProgress(progress: Progress) {
        callback?.uploadProgress(progress)
    }

    fun updateDownloadProgress(progress: Progress) {
        callback?.downloadProgress(progress)
    }

    fun deliverResponse(response: KotResponse) {
        callback?.onSuccess(response)
        finish()
    }

    fun deliverError(error: KotError) {
        callback?.onError(error)
        finish()
    }

    fun finish() {
        callback?.onFinish()
        callback = null
    }
}