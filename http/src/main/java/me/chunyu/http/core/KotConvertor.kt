package me.chunyu.http.core

import me.chunyu.http.core.request.TResponse

/**
 * Created by Roger Huang on 13/11/2017.
 */
interface KotConvertor<T> {
    fun convertResponse(response: KotResponse): TResponse<T>
}