package me.chunyu.http.core

import me.chunyu.http.core.common.Priority
import me.chunyu.http.schedular.KotRunnable

/**
 * Created by huangpeng on 12/11/2017.
 */
open class Runnable(val request: Request) : KotRunnable {

    override val priority: Priority = request.priority
    override val sequence: Int = request.sequenceNumber

    override fun run() {
        val context = request.context
        if (context == null || context.isCancelled) {
            return
        }

        try {
            val resp = context.execute()

            request.deliverResponse(resp)
        } catch (ex: Exception) {
            ex.printStackTrace()

            val error = KotUtils.getErrorForConnection(KotError(ex))
            request.deliverErrorResponse(error)
        } finally {

        }
    }
}
