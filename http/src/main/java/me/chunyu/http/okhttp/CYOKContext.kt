package me.chunyu.http.okhttp

import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.interfaces.RequestExecutorContext
import okhttp3.Call
import okhttp3.Headers

/**
 * Created by Roger Huang on 13/11/2017.
 *
 */

abstract class CYOKContext(request: KotRequest) : RequestExecutorContext(request) {
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

    fun addHeadersToRequestBuilder(builder: okhttp3.Request.Builder) {

        val requestHeaders = getHeaders()

        builder.headers(requestHeaders)

        request.userAgent = request.userAgent ?: CYOkHttp.userAgent

        if (request.userAgent != null && !requestHeaders.names().contains(KotConstants.USER_AGENT)) {
            builder.addHeader(KotConstants.USER_AGENT, request.userAgent)
        }
    }
}
