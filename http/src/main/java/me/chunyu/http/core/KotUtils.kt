package me.chunyu.http.core

import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.common.Progress
import me.chunyu.http.core.request.TCallback
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.schedular.Core
import okhttp3.Response
import okio.Okio
import java.net.URLConnection

/**
 * Created by aamir on 03/05/17.
 */
class KotUtils private constructor() {

    companion object {
        fun getMimeType(path: String): String {
            val fileNameMap = URLConnection.getFileNameMap()
            var mimeType: String? = fileNameMap.getContentTypeFor(path)
            if (mimeType == null) {
                mimeType = "application/octet-stream"
            }
            return mimeType
        }

        fun sendAnalytics(analyticsListener: ((timeTakenInMillis: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) -> Unit)?,
                          timeTaken: Long, bytesSent: Long, bytesReceived: Long, isFromCache: Boolean) {
            Core.instance.executorSupplier.forMainThreadTasks().execute {
                analyticsListener?.invoke(timeTaken, bytesSent, bytesReceived, isFromCache)
            }
        }

    }

}

fun<T> KotError.Companion.unsupported(t: T) : KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "unsupportted response: " + t.toString()
    return error
}

inline fun<reified T> KotError.Companion.responseParseError(): KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "failed to convert response to:" + T::class
    return error
}

fun KotError.Companion.connectionError(): KotError {
    val kotError = KotError()

    kotError.errorDetail = KotConstants.CONNECTION_ERROR
    kotError.errorCode = 0

    return kotError
}

fun KotError.Companion.noHttpClientError(): KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "please set KotRequest.httpClient, or KotRequest().httpClient"

    return error
}

fun KotError.Companion.cancelRequestError(): KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "request is cancelled"
    return error
}

fun<T> TCallback<T>.wrapInKotCallback(): KotCallback {
    val callback = this

    return object : KotCallback {
        override fun onSuccess(response: KotResponse) {
            val resp = convertResponse(response)
            callback.onCallback(resp)
        }

        override fun onError(error: KotError) {
            callback.onCallback(TResponse(null, error))
        }

        override fun uploadProgress(progress: Progress) {
            callback.onProgress(progress)
        }

        override fun downloadProgress(progress: Progress) {
            callback.onProgress(progress)
        }
    }
}

fun<T> TCallback<T>.convertResponse(response: KotResponse): TResponse<T> {
    val callback = this

    val convertor = KotResponse.convertorFactory?.objectCovertor<T>(callback.getType())

    if (convertor != null) {
        return convertor.convertResponse(response)
    }

    return TResponse(response, KotError("please set KotResponse.convertorFactory"))
}

