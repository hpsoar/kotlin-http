package me.chunyu.http.core

/**
 * Created by Roger Huang on 13/11/2017.
 */

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


class KotError : Exception {

    var errorBody: String? = null
    var errorCode = 0
    var errorDetail: String? = null

    constructor()

    constructor(message: String) : super(message)

    constructor(message: String, throwable: Throwable) : super(message, throwable)

    constructor(throwable: Throwable) : super(throwable)

    fun setCancellationMessageInError() {

    }

    fun <T> getErrorAsObject(objectClass: Class<T>): T? {
        TODO("Add Logic")
    }

    companion object
}