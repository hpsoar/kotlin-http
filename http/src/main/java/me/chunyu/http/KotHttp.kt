package me.chunyu.http

import android.app.VoiceInteractor
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.builder.GetRequestBuilder
import me.chunyu.http.okhttp.CYOkHttp

/**
 * Created by Roger Huang on 13/11/2017.
 */
class KotHttp {
    companion object {
        fun initialize() {
            KotRequest.httpClient = CYOkHttp()
            // TODO: config okHttpClient
        }

        fun get(url: String): GetRequestBuilder {
            return GetRequestBuilder(url)
        }
    }
}