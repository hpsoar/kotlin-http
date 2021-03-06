package me.chunyu.http.core

import me.chunyu.http.core.builder.RequestBuilder
import me.chunyu.http.core.common.Method
import me.chunyu.http.core.common.Priority
import me.chunyu.http.core.common.Progress
import me.chunyu.http.core.interfaces.KotHttpClient
import me.chunyu.http.core.interfaces.RequestExecutorContext
import me.chunyu.http.core.request.TCallback
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.schedular.Core
import okhttp3.*
import java.util.concurrent.Executor

/**
* Created by Roger Huang on 12/11/2017.
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

    var requestExecutor: RequestExecutorContext? = null

    private var callback: KotCallback? = null

    protected var progress = Progress(0, 0)

    private var isDelivered = false

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

    /**
     * if in an executable state
     */
    fun canExecute(): Boolean {
        return requestExecutor?.let { !it.isCancelled } ?: false
    }

    /**
     * this will be respected by request executor
     */
    open fun canCancel(): Boolean {
        return true
    }

    /**
     * do cancel
     */
    fun cancel(forceCancel: Boolean): Boolean {
        requestExecutor?.cancel(forceCancel)
        return requestExecutor?.isCancelled ?: true
    }

    fun async(callback: KotCallback?) {
        this.callback = callback

        val httpClient = currentHttpClient()
        if (httpClient != null) {

            requestExecutor = httpClient.executorContext(this)

            KotRequestQueue.INSTANCE.addRequest(this)
        } else {
            deliverError(KotError.noHttpClientError())
        }
    }

    /**
     * only uploadProgress/downloadProgress/onStart/onFinish will be called
     * do not call onSuccess or OnError, because the response may be processed twice
     */
    fun sync(callback: KotCallback?): KotResponse {
        val httpClient = currentHttpClient()
        if (httpClient != null) {
            this.callback = callback

            val context = httpClient.executorContext(this)

            this.requestExecutor = context

            try {
                return context.execute()
            }
            catch (e: Exception) {
                return KotResponse(KotError(e))
            }
            finally {
                runCallback {
                    finish()
                }
            }
        }

        return KotResponse(KotError.noHttpClientError())
    }

    /**
     * @param callback the callback is mainly used to get the type of template T,
     *      optionally, you can listen to callbacks, eg. upload/download progress
     */
    fun<T> sync(callback: TCallback<T>) : TResponse<T> {

        val response = sync(callback.wrapInKotCallback())

        return callback.convertResponse(response)
    }

    /**
     * @param callback with TResponse: parsed object, raw response, and error
     */
    fun<T> async(callback: TCallback<T>) {
        async(callback.wrapInKotCallback())
    }

    // MARK - update progress & deliver result
    // NOTE: for any request finish should be called

    fun getUploadProgressListener(): ((Long, Long) -> Unit)? {
        return { bytesDownloaded: Long, totalBytes: Long ->
            requestExecutor?.let {
                if (!it.isCancelled) {
                    progress = Progress(bytesDownloaded, totalBytes)

                    runCallback {
                        callback?.uploadProgress(progress)
                    }
                }
            }
        }
    }

    fun getDownloadProgressListener(): ((Long, Long) -> Unit)? {
        return { bytesDownloaded: Long, totalBytes: Long ->
            requestExecutor?.let {
                if (!it.isCancelled) {
                    progress = Progress(bytesDownloaded, totalBytes)

                    runCallback {
                        callback?.downloadProgress(progress)
                    }
                }
            }
        }
    }

    fun deliverResponse(response: KotResponse) {
        runCallback {
            if (!isDelivered) {
                callback?.onSuccess(response)

                finish()
            }
        }
    }

    fun deliverError(error: KotError) {
        runCallback {
            if (!isDelivered) {

                callback?.onError(error)

                finish()
            }
        }
    }

    /**
     * every request should call this once and only once
     */
    private fun finish() {
        if (!isDelivered) {
            isDelivered = true

            callback?.onFinish()
            callback = null

            requestExecutor = null

            KotRequestQueue.INSTANCE.finish(this)
        }
    }

    /**
     * to run upload/download progress callback & deliver response or error
     */
    private fun runCallback(block: (Unit) -> Unit) {
        executor?.execute {
            block(Unit)
        } ?: Core.instance.executorSupplier.forMainThreadTasks().execute {
            block(Unit)
        }
    }

    private fun currentHttpClient(): KotHttpClient? {
        return httpClient ?: KotRequest.httpClient
    }
}
