package me.chunyu.http.core

import me.chunyu.http.core.common.Priority
import me.chunyu.http.schedular.Core
import org.jetbrains.annotations.NotNull
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by huangpeng on 12/11/2017.
 */
class KotRequestQueue private constructor() {
    private val sequenceGenerator: AtomicInteger = AtomicInteger()
    private var currentRequest: MutableSet<KotRequest> = mutableSetOf()

    private object Holder {
        val INSTANCE = KotRequestQueue()
    }

    companion object {
        val INSTANCE: KotRequestQueue by lazy { Holder.INSTANCE }
    }

    fun addRequest(request: KotRequest) {
        synchronized(currentRequest) {
            try {
                currentRequest.add(request)
            } catch (ex: Exception) {
                request.deliverError(KotError(ex))
                ex.printStackTrace()
            }
        }

        try {
            // prepare for run

            request.sequenceNumber = getSequenceNumber()

            request.requestExecutor?.future = when (request.priority) {

                Priority.IMMEDIATE -> {
                    Core.instance
                            .executorSupplier
                            .forImmediateNetworkTasks()
                            .submit(KotRequestRunnable(request))
                }

                else -> {
                    Core.instance
                            .executorSupplier
                            .forNetworkTasks()
                            .submit(KotRequestRunnable(request))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            request.deliverError(KotError(ex))
        }
    }

    private fun getSequenceNumber(): Int = sequenceGenerator.incrementAndGet()


    fun cancelRequestWithGivenTag(@NotNull tag: Any, forceCancel: Boolean) {
        synchronized(currentRequest) {
            cancel(object : RequestFilter {
                override fun apply(request: KotRequest): Boolean {
                    if (tag is String && request.tag is String) {
                        return tag == request.tag
                    } else {
                        return tag === request.tag
                    }
                }
            }, forceCancel)
        }
    }

    fun cancel(requestFilter: RequestFilter, forceCancel: Boolean) {
        synchronized(currentRequest) {
            val iterator: MutableIterator<KotRequest> = currentRequest.iterator()
            while (iterator.hasNext()) {
                with(iterator.next() /*KotRequest*/) {
                    if (requestFilter.apply(this /*KotRequest*/)) {
                        if (cancel(forceCancel)) {
                            iterator.remove()
                        }
                    }
                }
            }
        }
    }

    fun cancelAll(forceCancel: Boolean) {
        synchronized(currentRequest) {
            val iterator: MutableIterator<KotRequest> = currentRequest.iterator()
            while (iterator.hasNext()) {
                with(iterator.next() /*KotRequest*/) {
                    if (cancel(forceCancel)) {
                        iterator.remove()
                    }
                }
            }
        }
    }

    fun finish(request: KotRequest) {
        synchronized(currentRequest) {
            currentRequest.remove(request)
        }
    }


    interface RequestFilter {
        fun apply(request: KotRequest): Boolean
    }
}