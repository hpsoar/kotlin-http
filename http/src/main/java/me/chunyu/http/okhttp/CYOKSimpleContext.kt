package me.chunyu.http.okhttp

import android.net.TrafficStats
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.common.Method
import java.io.IOException

/**
 * Created by Roger Huang on 13/11/2017.
 */
class CYOKSimpleContext(request: KotRequest) : CYOKContext(request) {
    override fun execute(): KotResponse {

        try {
            var builder = okhttp3.Request.Builder().url(request.getFormattedUrl())

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

            val okHttpRequest = builder.build()

            call = CYOkHttp.sOkHttpClient.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()
            val startBytes = TrafficStats.getTotalRxBytes()

            val okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(requestBody, okHttpResponse, startBytes, timeTaken)

            return CYOKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}

