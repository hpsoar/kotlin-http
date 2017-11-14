package me.chunyu.http.okhttp

import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import okhttp3.Response

/**
 * Created by Roger Huang on 13/11/2017.
 */

class OKResponse(val okHttpResponse: Response?, error: KotError?) : KotResponse(error) {
    var success = false
    init {
        if (okHttpResponse != null && okHttpResponse.code() < 400) {
            success = true
        }
    }

    override fun isSuccess(): Boolean {
        return success
    }
}

