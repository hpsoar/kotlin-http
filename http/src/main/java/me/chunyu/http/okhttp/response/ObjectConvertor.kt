package me.chunyu.http.okhttp.response

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.request.TResponse
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Created by Roger Huang on 13/11/2017.
 */
class ObjectConvertor<T> : KotConvertor<T> {
    override fun convertResponse(response: KotResponse): TResponse<T> {
        val resp = StringConvertor().convertResponse(response)
        if (resp.data == null) {
            return TResponse(resp.response, resp.error)
        }

        return try {
            val o =  object: JSON<T>() {}.toObject(resp.data)
            TResponse(o, resp.response, resp.error)
        } catch (e: Exception) {
            return TResponse(response, KotError(e))
        }
    }
}