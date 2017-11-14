package me.chunyu.http.okhttp

import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Created by Roger Huang on 13/11/2017.
 */
class OKUtils {
    companion object {
        fun saveFile(response: Response?, dirPath: String?, fileName: String?) {
            val inputStream: InputStream? = response?.body()?.byteStream()

            val dir = File(dirPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(dir, fileName)
            val fos = FileOutputStream(file)
            inputStream.use { input ->
                fos.use { output ->
                    if (output is FileOutputStream) input?.copyTo(output)
                }
            }
        }
    }
}