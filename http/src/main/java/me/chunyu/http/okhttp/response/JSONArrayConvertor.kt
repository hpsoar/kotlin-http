package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.request.TResponse
import okio.Okio
import org.json.JSONArray

/**
 * Created by Roger Huang on 13/11/2017.
 */
open class JSONArrayConvertor{
    override fun convertResponse(response: KotResponse): TResponse<JSONArray> {
        val resp = ResponseConvertor().convertResponse(response)
        if (resp.data == null) {
            return TResponse(resp.response, resp.error)
        }

        return try {
            val json = JSONArray(Okio.buffer(resp.data.body().source()).readUtf8())

            TResponse(json, resp.response, resp.error)
        } catch (e: Exception) {
            TResponse(response, KotError(e))
        } finally {
            ResponseCloser(resp.data)
        }
    }
}
