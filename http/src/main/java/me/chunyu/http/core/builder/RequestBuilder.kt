package me.chunyu.http.core.builder

import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.ParseUtil
import me.chunyu.http.core.common.Priority
import me.chunyu.http.core.common.Method
import me.chunyu.http.core.interfaces.KotHttpClient
import okhttp3.CacheControl
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by huangpeng on 12/11/2017.
 */
open class RequestBuilder(val url: String, val method: Method) {

    var priority: Priority = Priority.MEDIUM
    var tag: Any? = null
    val headersMap: MutableMap<String, String> = mutableMapOf()
    val queryParameterMap: MutableMap<String, String> = mutableMapOf()
    val pathParameterMap: MutableMap<String, String> = mutableMapOf()
    var cacheControl: CacheControl? = null
    var executor: Executor? = null
    var httpClient: KotHttpClient? = null
    var userAgent: String? = null

     fun setPriority(priority: Priority): RequestBuilder {
        this.priority = priority
        return this
    }

     fun setTag(tag: Any): RequestBuilder {
        this.tag = tag
        return this
    }

     fun addHeaders(key: String, value: String): RequestBuilder {
        headersMap.put(key, value)
        return this
    }

     fun addHeaders(headerMap: MutableMap<String, String>): RequestBuilder {
        headersMap.putAll(headerMap)
        return this
    }

     fun addHeaders(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> headersMap.putAll(it) }
        return this
    }

     fun addQueryParameter(key: String, value: String): RequestBuilder {
        queryParameterMap.put(key, value)
        return this
    }

     fun addQueryParameter(queryParameterMap: MutableMap<String, String>): RequestBuilder {
        this.queryParameterMap.putAll(queryParameterMap)
        return this
    }

     fun addQueryParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> queryParameterMap.putAll(it) }
        return this
    }

     fun addPathParameter(key: String, value: String): RequestBuilder {
        pathParameterMap.put(key, value)
        return this
    }

     fun addPathParameter(pathParameterMap: MutableMap<String, String>): RequestBuilder {
        this.pathParameterMap.putAll(pathParameterMap)
        return this
    }

     fun addPathParameter(objectAny: Any): RequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { it -> pathParameterMap.putAll(it) }
        return this
    }

     fun doNotCacheResponse(): RequestBuilder {
        cacheControl = CacheControl.Builder().noStore().build()
        return this
    }

     fun getResponseOnlyIfCached(): RequestBuilder {
        cacheControl = CacheControl.FORCE_CACHE
        return this
    }

     fun getResponseOnlyFromNetwork(): RequestBuilder {
        cacheControl = CacheControl.FORCE_NETWORK
        return this
    }

     fun setMaxAgeCacheControl(maxAge: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxAge(maxAge, timeUnit).build()
        return this
    }

     fun setMaxStaleCacheControl(maxStale: Int, timeUnit: TimeUnit): RequestBuilder {
        cacheControl = CacheControl.Builder().maxStale(maxStale, timeUnit).build()
        return this
    }

     fun setExecutor(executor: Executor): RequestBuilder {
        this.executor = executor
        return this
    }

     fun setHttpClient(httpClient: KotHttpClient): RequestBuilder {
        this.httpClient = httpClient
        return this
    }

    fun setUserAgent(userAgent: String): RequestBuilder {
        this.userAgent = userAgent
        return this
    }

    open fun build(): KotRequest {
        return KotRequest(this)
    }
}