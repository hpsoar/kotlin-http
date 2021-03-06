package me.chunyu.http.okhttp

import android.os.Handler
import android.os.Looper
import android.os.Message
import me.chunyu.http.core.common.KotConstants
import me.chunyu.http.core.common.Progress

/**
 * Created by Roger Huang on 13/11/2017.
 */


class UploadProgressHandler(val progressCallback: ((bytesUploaded: Long, totalBytes: Long) -> Unit)) : Handler(Looper.getMainLooper()) {

    override fun handleMessage(msg: Message?) {
        when (msg?.what) {
            KotConstants.UPDATE -> {
                val progress = msg.obj as Progress
                progressCallback(progress.currentBytes, progress.totalBytes)
            }
            else -> super.handleMessage(msg)
        }
    }

}