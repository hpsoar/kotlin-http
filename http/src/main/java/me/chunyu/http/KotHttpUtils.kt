package me.chunyu.http

import com.google.gson.JsonElement
import me.chunyu.http.core.*
import me.chunyu.http.core.builder.RequestBuilder
import me.chunyu.http.core.request.TCallback
import me.chunyu.http.core.request.TResponse

/**
 * Created by Roger Huang on 13/11/2017.
 */

fun RequestBuilder.async(callback: KotCallback) {
    build().async(callback)
}

fun RequestBuilder.sync(callback: KotCallback?): KotResponse {
    return build().sync(callback)
}


fun<T> RequestBuilder.async(callback: TCallback<T>) {
    build().async(callback)
}

fun<T> RequestBuilder.sync(callback: TCallback<T>) : TResponse<T> {
    return build().sync(callback)
}

fun RequestBuilder.asyncJson(callback: (TResponse<JsonElement>)->Unit) {
    async(object: TCallback<JsonElement>() {
        override fun onCallback(response: TResponse<JsonElement>) {
            callback(response)
        }
    })
}
