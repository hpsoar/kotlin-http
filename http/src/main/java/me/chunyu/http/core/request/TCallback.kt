package me.chunyu.http.core.request

import me.chunyu.http.core.common.Progress

/**
 * Created by Roger Huang on 14/11/2017.
 */
abstract class TCallback<T> : ObjectTypeInferer<T>() {
    open fun onCallback(response: TResponse<T>) {
    }

    open fun onProgress(progress: Progress) {

    }
}