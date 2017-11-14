package me.chunyu.http

import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.builder.GetRequestBuilder
import me.chunyu.http.okhttp.CYOkHttp
import me.chunyu.http.okhttp.response.KotOkConvertorFactory

/**
 * Created by Roger Huang on 13/11/2017.
 */
class KotHttp {
    companion object {
        // NOTE: this is the only place that couples with okHttp
        fun initialize() {
            KotRequest.httpClient = CYOkHttp()
            KotResponse.responseFactory = KotOkConvertorFactory()
            // TODO: config okHttpClient
        }

        fun get(url: String): GetRequestBuilder {
            return GetRequestBuilder(url)
        }
    }
}