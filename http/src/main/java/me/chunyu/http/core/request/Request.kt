package me.chunyu.http.core

import me.chunyu.http.core.builder.RequestBuilder
import me.chunyu.http.core.common.Method
import me.chunyu.http.core.common.Priority
import me.chunyu.http.core.interfaces.HttpClient
import me.chunyu.http.core.interfaces.RequestExecutorContext
import okhttp3.*
import okio.Okio
import java.util.concurrent.Executor

/**
 * Created by huangpeng on 12/11/2017.
 */
open class Request(builder: RequestBuilder) {

    companion object {
        var httpClient: HttpClient? = null
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

    var httpClient: HttpClient? = null

    var executor: Executor? = null

    var context: RequestExecutorContext? = null

    var callbck: CYCallback? = null

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

    fun currentHttpClient(): HttpClient? {
        return httpClient ?: Request.httpClient
    }

    fun async(callback: CYCallback) {
        this.callbck = callbck

        val httpClient = currentHttpClient()
        if (httpClient != null) {

            context = httpClient.executorContext(this)

            RequestQueue.INSTANCE.addRequest(this)
        } else {
            deliverErrorResponse(noHttpClientError())
        }
    }

    // TODO: sync是不是也可以传callback?
    fun <T> sync(callback: CYCallback): CYResponse {
        val httpClient = currentHttpClient()
        if (httpClient != null) {
            this.callbck = callbck

            val context = httpClient.executorContext(this)

            this.context = context

            val resp = context.execute()

            return resp
        }

        return CYResponse(noHttpClientError())
    }

    fun parseNetworkError(kotError: KotError): KotError {
        try {
            val errorResponse: Response? = kotError.response
            kotError.errorBody = errorResponse?.let {
                errorResponse.body()?.let {
                    errorResponse.body().source()?.let {
                        source ->
                        Okio.buffer(source).readUtf8()
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return kotError
    }

    fun noHttpClientError(): KotError {
        val error = KotError()
        error.errorCode = -1
        error.errorDetail = "httpClient is not set for either Request or current request object"

        return error
    }

    fun finish() {
        callbck?.onFinish()
        callbck = null
    }

    fun deliverResponse(response: CYResponse) {
        // TODO:
        callbck?.onSuccess(response)
        finish()
    }

    fun deliverErrorResponse(error: KotError) {
        callbck?.onError(error)
        finish()
    }
}