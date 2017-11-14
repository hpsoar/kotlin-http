package me.chunyu.http.core

import me.chunyu.http.core.common.KotConstants
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

