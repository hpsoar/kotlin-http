package me.chunyu.http.okhttp

import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.common.Progress
import okhttp3.RequestBody
import java.io.IOException

/**
 * Created by Roger Huang on 13/11/2017.
 */
class CYOKMultipartContext(request: KotRequest) : CYOKContext(request) {

    override fun execute(): KotResponse {
        try {
            var builder = okhttp3.Request.Builder().url(request.getFormattedUrl())

            addHeadersToRequestBuilder(builder)

            val requestBody: RequestBody = request.getOKRequestBody()

            val requestBodyLength = requestBody.contentLength()

            builder = builder.post(RequestProgressBody(requestBody, {
                bytesUploaded, totalBytes ->
                request.updateUploadProgress(Progress(bytesUploaded, totalBytes))
            }))

            request.cacheControl?.let { builder.cacheControl(it) }

            val okHttpRequest = builder.build()

            call = CYOkHttp.sOkHttpClient.newCall(okHttpRequest)

            val startTime = System.currentTimeMillis()

            val okHttpResponse = call?.execute()

            val timeTaken = System.currentTimeMillis() /*endTime*/ - startTime

            request.logRequestResult(requestBody, okHttpResponse, startBytes = 0, timeTaken = timeTaken)

            return CYOKResponse(okHttpResponse, null)
        } catch (ioe: IOException) {
            throw KotError(ioe)
        }
    }
}
