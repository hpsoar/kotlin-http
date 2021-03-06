package me.chunyu.http

import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.builder.GetRequestBuilder
import me.chunyu.http.okhttp.OkHttp
import me.chunyu.http.okhttp.response.KotOkConvertorFactory

/**
 * Created by Roger Huang on 13/11/2017.
 */
class KotHttp {
    companion object {
        // NOTE: this is the only place that couples with okHttp
        fun initialize() {
            KotRequest.httpClient = OkHttp() // httpClient will delegate real request

            /*
             * convertorFactory is responsible to convert raw response (wrapped in KotResponse) to typed response TResponse<T>
             * basically, you need to implement one for your httpClient
             */
            KotResponse.convertorFactory = KotOkConvertorFactory()
            // TODO: config okHttpClient
        }

        fun get(url: String): GetRequestBuilder {
            return GetRequestBuilder(url)
        }
    }
}