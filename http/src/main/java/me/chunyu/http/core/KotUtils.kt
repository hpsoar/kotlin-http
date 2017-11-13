package me.chunyu.http.core

import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.schedular.Core
import java.net.URLConnection

/**
 * Created by aamir on 03/05/17.
 */
class KotUtils private constructor() {

    companion object {

        fun getErrorForConnection(kotError: KotError): KotError {
            kotError.errorDetail = KotConstants.CONNECTION_ERROR
            kotError.errorCode = 0
            return kotError
        }

        fun getErrorForServerResponse(kotError: KotError, kotRequest: Request, code: Int): KotError {
            val parsedKotError = kotRequest.parseNetworkError(kotError)
            parsedKotError.errorDetail = KotConstants.RESPONSE_FROM_SERVER_ERROR
            parsedKotError.errorCode = code
            return parsedKotError
        }

        fun getErrorForParse(kotError: KotError): KotError {
            kotError.errorCode = 0
            kotError.errorDetail = KotConstants.PARSE_ERROR
            return kotError
        }

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