package me.chunyu.http.okhttp

import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.common.Progress
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Created by Roger Huang on 13/11/2017.
 */

class ResponseProgressBody(private val responseBody: ResponseBody, progressCallback: ((bytesDownloaded: Long, totalBytes: Long) -> Unit)?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private var progressHandler: DownloadProgressHandler? = null

    init {
        if (progressCallback != null) {
            this.progressHandler = DownloadProgressHandler(progressCallback)
        }
    }


    override fun contentLength(): Long = responseBody.contentLength()

    override fun contentType(): MediaType = responseBody.contentType()

    override fun source(): BufferedSource {
        return bufferedSource ?: Okio.buffer(source(responseBody.source()))
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {

            internal var totalBytesRead: Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                val progress: Progress = Progress(totalBytesRead, responseBody.contentLength())
                progressHandler?.obtainMessage(KotConstants.UPDATE, progress)?.sendToTarget()
                return bytesRead
            }
        }
    }

}