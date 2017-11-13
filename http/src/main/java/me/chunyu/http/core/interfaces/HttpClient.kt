package me.chunyu.http.core.interfaces

import me.chunyu.http.core.Request

/**
 * Created by Roger Huang on 13/11/2017.
 *
 */

interface HttpClient {
    fun executorContext(request: Request): RequestExecutorContext
}
