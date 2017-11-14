package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.request.TResponse
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 13/11/2017.
 */
class ObjectConvertor<T>(val type: Type) : KotConvertor<T> {
    override fun convertResponse(response: KotResponse): TResponse<T> {
        val resp = StringConvertor().convertResponse(response)
        if (resp.data == null) {
            return TResponse(resp.response, resp.error)
        }

        return try {
            val o =  JSON.toObject<T>(resp.data, type)
            TResponse(o, resp.response, resp.error)
        } catch (e: Exception) {
            return TResponse(response, KotError(e))
        }
    }
}