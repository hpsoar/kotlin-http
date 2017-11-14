package me.chunyu.http.core.interfaces

import me.chunyu.http.core.KotRequest

/**
 * Created by Roger Huang on 13/11/2017.
 *
 */

interface KotHttpClient {
    fun executorContext(request: KotRequest): RequestExecutorContext
}
