package me.chunyu.http.core.common

import java.io.Serializable

/**
 * Created by Roger Huang on 13/11/2017.
 */

/**
 * Created by amitshekhar on 01/05/17.
 */
class Progress(var currentBytes: Long, var totalBytes: Long) : Serializable {
    fun percentage(): Double {
        if (totalBytes <= 0) {
            return 0.0
        }
        return  currentBytes.toDouble() / totalBytes
    }
}