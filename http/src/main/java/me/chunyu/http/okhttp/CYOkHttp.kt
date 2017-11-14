package me.chunyu.http.okhttp

import me.chunyu.http.core.*
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.interfaces.KotHttpClient
import me.chunyu.http.core.interfaces.RequestExecutorContext
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Created by huangpeng on 12/11/2017.
 */
class CYOkHttp : KotHttpClient {
    companion object {
        // you can customize in your code
        var sOkHttpClient: OkHttpClient = defaultOkHttpClient()
        var userAgent: String? = null

        fun defaultOkHttpClient(): OkHttpClient = OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    override fun executorContext(request: KotRequest): RequestExecutorContext {
        if (request as? KotDownloadRequest != null) {
            return CYOKDownloadContext(request)
        } else if (request as? KotMultipartRequest != null) {
            return CYOKMultipartContext(request)
        }
        return CYOKSimpleContext(request)
    }
}


