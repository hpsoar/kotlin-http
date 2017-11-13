package me.chunyu.http

import me.chunyu.http.core.CYCallback
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.builder.RequestBuilder

/**
 * Created by Roger Huang on 13/11/2017.
 */

fun RequestBuilder.async(callback: CYCallback) {
    build().async(callback)
}

fun RequestBuilder.sync(callback: CYCallback?): KotResponse {
    return build().sync(callback)
}
