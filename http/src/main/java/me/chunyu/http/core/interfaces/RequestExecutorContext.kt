package me.chunyu.http.core.interfaces

import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.KotResponse
import java.io.IOException
import java.util.concurrent.Future

/**
 * Created by huangpeng on 12/11/2017.
 */
abstract class RequestExecutorContext(val request: KotRequest) {
    var future: Future<*>? = null

    /**
     * if isCancelled, asynchronous request may not be executed @{KotRequestRunnable}
     */
    var isCancelled = false

    @Throws(IOException::class)
    abstract fun execute(): KotResponse
    
    abstract fun cancel(forceCancel: Boolean): Boolean
}

