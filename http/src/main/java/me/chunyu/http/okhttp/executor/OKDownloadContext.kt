package me.chunyu.http.okhttp

import android.net.TrafficStats
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.common.Progress
import okhttp3.Response
import java.io.IOException

/**
 * Created by Roger Huang on 13/11/2017.
 */

class OKDownloadContext(request: KotRequest) : OKContext(request) {
    override fun execute(): KotResponse {
        try {
            var builder = okhttp3.Request.Builder().url(request.getFormattedUrl())
            addHeadersToRequestBuilder(builder)
            builder = builder.get()

            request.cacheControl?.let { builder.cacheControl(it) }

            val okHttpRequest = builder.build()

            val okHttpClient =
                    OkHttp.sOkHttpClient.newBuilder().addInterceptor { chain ->
                        val response: Response = chain.proceed(chain.request())
                        response.newBuilder()
                                .body(ResponseProgressBody(response.body(), request.getDownloadProgressListener()))
                                .build()
                    }?.build()

            call = okHttpClient?.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()
            val startBytes = TrafficStats.getTotalRxBytes()

            val okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(null, okHttpResponse, startBytes, timeTaken)

            return OKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}


