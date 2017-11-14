package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.request.TResponse
import okio.Okio

/**
 * Created by Roger Huang on 13/11/2017.
 */

open class StringConvertor {
    override fun convertResponse(response: KotResponse): TResponse<String> {
        val resp = ResponseConvertor().convertResponse(response)
        if (resp.data == null) {
            return TResponse(resp.response, resp.error)
        }

        return try {
            val str = Okio.buffer(resp.data.body().source()).readUtf8()

            // 有可能是404，返回了html
            TResponse(str, resp.response, resp.error)
        } catch (e: Exception) {
            TResponse(response, KotError(e))
        } finally {
            ResponseCloser(resp.data)
        }
    }
}
