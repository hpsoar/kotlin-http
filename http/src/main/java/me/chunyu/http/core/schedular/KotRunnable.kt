package me.chunyu.http.schedular

import me.chunyu.http.core.Priority

/**
 * Created by Roger Huang on 13/11/2017.
 */
interface KotRunnable : Runnable {
    val priority: Priority
    val sequence: Int
}