package me.chunyu.http

import me.chunyu.http.core.KotCallback
import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
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
    fun onCallback(response: TResponse<T>?, error: KotError?) {

    }
}

fun<T> RequestBuilder.async(callback: ObjectCallback<T>) {
    build().async(object : KotCallback {
        override fun onSuccess(response: KotResponse) {
            val resp = ObjectConvertor<T>(callback.getType()).convertResponse(response)
            callback.onCallback(resp, null)
        }

        override fun onError(error: KotError) {
            callback.onCallback(null, error)
        }
    })
}
