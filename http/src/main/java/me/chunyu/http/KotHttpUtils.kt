package me.chunyu.http

import me.chunyu.http.core.KotCallback
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.builder.RequestBuilder

/**
 * Created by Roger Huang on 13/11/2017.
 */

fun RequestBuilder.async(callback: KotCallback) {
    build().async(callback)
}

fun RequestBuilder.sync(callback: KotCallback?): KotResponse {
    return build().sync(callback)
}
