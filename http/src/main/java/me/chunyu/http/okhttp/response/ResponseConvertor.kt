package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.core.unsupported
import me.chunyu.http.okhttp.OKResponse
import okhttp3.Response
import okio.Okio

/**
* Created by Roger Huang on 12/11/2017.
*/
open class ResponseConvertor : KotConvertor<Response> {
    override fun convertResponse(response: KotResponse): TResponse<Response> {
        if (response is OKResponse) {
            return if (response.okHttpResponse != null) {
                if (response.okHttpResponse.code() < 400) {
                    TResponse(response.okHttpResponse, response)
                } else {
                    parseErrorResponse(response)
                }
            } else {
                val error = KotError()
                error.errorCode = -1
                error.errorDetail = "didn't get any response"
                TResponse(null, error)
            }
        } else {
            return TResponse(response, KotError.unsupported(response))
        }
    }

    private fun parseErrorResponse(response: OKResponse): TResponse<Response> {
        try {
            val errorResponse = response.okHttpResponse!!

            val parsedKotError = KotError()
            parsedKotError.errorDetail = KotConstants.RESPONSE_FROM_SERVER_ERROR

            parsedKotError.errorCode = errorResponse.code()
            parsedKotError.errorBody = errorResponse.body()?.let {
                errorResponse.body().source()?.let { source ->
                    Okio.buffer(source).readUtf8()
                }
            }
            return TResponse(response, parsedKotError)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return TResponse(response, KotError(ex))
        }
    }
}


