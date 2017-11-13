package me.chunyu.http.core.request

import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse

/**
 * Created by Roger Huang on 13/11/2017.
 */
class TResponse<T>(val data: T?, val response: KotResponse?, error: KotError?) : KotResponse(error){
    constructor(data: T?, response: KotResponse?): this(data, response, null)

    constructor(response: KotResponse?, error: KotError?): this(null, response, error)
}