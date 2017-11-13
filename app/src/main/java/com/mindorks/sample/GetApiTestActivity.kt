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

package com.mindorks.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import me.chunyu.http.KotHttp
import me.chunyu.http.async
import me.chunyu.http.core.CYCallback
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse

class GetApiTestActivity : AppCompatActivity() {

    companion object {

        private val TAG: String? = "GetApiTestActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_api_test)

    }

    fun getAsString(view: View) {
        KotHttp.get("https://www.baidu.com/").async(object : CYCallback {
            override fun onError(error: KotError) {
                Log.e("hello", error.errorDetail)
                error.message?.let {
                    Log.e("hello", it)
                }
            }

            override fun onFinish() {
                Log.i("world", "finish")
            }

            override fun onSuccess(response: KotResponse) {
                Log.i("world2", response.toString())
            }
        })
    }

    fun getAsJSONArray(view: View) {
    }

    fun getAsJSONObject(view: View) {
    }

    fun downloadImageFile(view: View) {
    }

    fun sendAndCancelAll(view: View) {
    }
}
