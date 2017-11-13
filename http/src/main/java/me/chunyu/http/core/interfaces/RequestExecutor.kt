package me.chunyu.http.core.interfaces

import me.chunyu.http.core.Request
import me.chunyu.http.core.CYResponse
import java.util.concurrent.Future

/**
 * Created by huangpeng on 12/11/2017.
 */
abstract class RequestExecutorContext(val request: Request) {
    var future: Future<*>? = null
    var isCancelled = false

    abstract fun execute(): CYResponse

    abstract fun cancel(forceCancel: Boolean): Boolean
}

