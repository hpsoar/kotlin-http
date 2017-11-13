package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.core.unsupported
import me.chunyu.http.okhttp.CYOKResponse
import okhttp3.Response

/**
 * Created by huangpeng on 12/11/2017.
 */
open class ResponseConvertor : KotConvertor<Response> {
    override fun convertResponse(response: KotResponse): TResponse<Response> {
        if (response is CYOKResponse) {
            if (response.okHttpResponse != null) {
                if (response.okHttpResponse.code() < 400) {
                    return TResponse(response.okHttpResponse, response)
                } else {
                    val error = KotError()
                    error.errorCode = response.okHttpResponse.code()
                    error.errorDetail = response.okHttpResponse.message()
                    return TResponse(response.okHttpResponse, response, error)
                }
            } else {
                val error = KotError()
                error.errorCode = -1
                error.errorDetail = "didn't get any response"
                return TResponse(null, error)
            }
        } else {
            return TResponse(response, KotError.unsupported(response))
        }
    }
}


