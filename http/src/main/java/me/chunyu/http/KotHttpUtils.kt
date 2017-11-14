package me.chunyu.http

import com.google.gson.JsonElement
import me.chunyu.http.core.*
import me.chunyu.http.core.builder.RequestBuilder
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.okhttp.response.JSON
import me.chunyu.http.okhttp.response.ObjectConvertor
import me.chunyu.http.okhttp.response.StringConvertor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 13/11/2017.
 */

fun RequestBuilder.async(callback: KotCallback) {
    build().async(callback)
}

fun RequestBuilder.sync(callback: KotCallback?): KotResponse {
    return build().sync(callback)
}

abstract class ObjectTypeInferer<T> {
    fun getType(): Type {
        val genType = this::class.java.genericSuperclass
        val params = (genType as ParameterizedType).actualTypeArguments
        val type = params[0]

        return type
    }
}

abstract class ObjectCallback<T> : ObjectTypeInferer<T>() {
    open fun onCallback(response: TResponse<T>) {
    }
}

fun<T> RequestBuilder.async(callback: ObjectCallback<T>) {
    build().async(object : KotCallback {
        override fun onSuccess(response: KotResponse) {
            val convertor = KotResponse.responseFactory!!.objectCovertor<T>(callback.getType())

            val resp = convertor.convertResponse(response)

            callback.onCallback(resp)
        }

        override fun onError(error: KotError) {
            callback.onCallback(TResponse(null, error))
        }
    })
}

fun RequestBuilder.asyncJson(callback: (TResponse<JsonElement>)->Unit) {
    async(object : ObjectCallback<JsonElement>() {
        override fun onCallback(response: TResponse<JsonElement>) {
            callback(response)
        }
    })
}
