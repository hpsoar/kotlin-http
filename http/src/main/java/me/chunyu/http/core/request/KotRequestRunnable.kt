package me.chunyu.http.core

import me.chunyu.http.core.common.Priority
import me.chunyu.http.schedular.KotRunnable

/**
 * Created by huangpeng on 12/11/2017.
 */
open class KotRequestRunnable(val request: KotRequest) : KotRunnable {

    override val priority: Priority = request.priority
    override val sequence: Int = request.sequenceNumber

    override fun run() {
        val context = request.requestExecutor
        if (context == null || context.isCancelled) {
            return
        }

        try {
            val resp = context.execute()

            request.deliverResponse(resp)
        } catch (ex: Exception) {
            ex.printStackTrace()

            request.deliverError(KotError.connectionError())
        } finally {

        }
    }
}
