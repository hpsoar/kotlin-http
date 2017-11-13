/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mindorks.kotnetworking.internal

import com.mindorks.kotnetworking.common.Priority
import com.mindorks.kotnetworking.core.Core
import com.mindorks.kotnetworking.request.KotRequest
import org.jetbrains.annotations.NotNull
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by amitshekhar on 30/04/17.
 */
class KotRequestQueue private constructor() {

    val sequenceGenerator: AtomicInteger = AtomicInteger()
    var currentRequest: MutableSet<KotRequest> = mutableSetOf()

    private object Holder {
        val INSTANCE = KotRequestQueue()
    }

    companion object {
        val instance: KotRequestQueue by lazy { Holder.INSTANCE }
    }

    fun addRequest(request: KotRequest) {
        synchronized(currentRequest) {
            try {
                currentRequest.add(request)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        try {
            request.sequenceNumber = getSequenceNumber()

            request.future = when (request.priority) {

                Priority.IMMEDIATE -> {
                    Core.instance
                            .executorSupplier
                            .forImmediateNetworkTasks()
                            .submit(KotRunnable(request))
                }

                else -> {
                    Core.instance
                            .executorSupplier
                            .forNetworkTasks()
                            .submit(KotRunnable(request))
                }
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
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
                        cancel(forceCancel)
                        if (isCancelled) {
                            destroy()
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
                    cancel(forceCancel)
                    if (isCancelled) {
                        destroy()
                        iterator.remove()
                    }
                }
            }
        }
    }

    fun finish(kotRequest: KotRequest) {
        synchronized(currentRequest) {
            currentRequest.remove(kotRequest)
        }
    }


    interface RequestFilter {
        fun apply(request: KotRequest): Boolean
    }
}