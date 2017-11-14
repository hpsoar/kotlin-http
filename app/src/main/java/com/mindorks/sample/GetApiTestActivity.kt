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
import com.google.gson.JsonElement
import com.mindorks.sample.ApiEndPoint.Companion.GET_JSON_ARRAY
import me.chunyu.http.KotHttp
import me.chunyu.http.async
import me.chunyu.http.asyncJson
import me.chunyu.http.core.KotCallback
import me.chunyu.http.core.KotError
import me.chunyu.http.core.KotResponse
import me.chunyu.http.core.common.Progress
import me.chunyu.http.core.request.TCallback
import me.chunyu.http.core.request.TResponse
import me.chunyu.http.okhttp.response.JSON
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class GetApiTestActivity : AppCompatActivity() {

    class User(val username: String, val emailAddress: String)

    class Resp(val success: Boolean, val user: User)

    companion object {

        private val TAG: String? = "GetApiTestActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_api_test)

    }

    fun getAsString(view: View) {
        KotHttp.get("https://www.baidu.com/").async(object : KotCallback {
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
        //GET_JSON_ARRAY
        KotHttp.get(GET_JSON_ARRAY)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .async(object: TCallback<JsonElement>() {
            override fun onCallback(response: TResponse<JsonElement>) {
                print("hello")
            }
        })

        KotHttp.get(GET_JSON_ARRAY)
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "5")
                .asyncJson { response ->
                }
    }

    fun getAsJSONObject(view: View) {
        KotHttp.get("https://www.chunyuyisheng.com").async(object: TCallback<Resp>() {
            override fun onCallback(response: TResponse<Resp>) {
            }
        })
    }

    fun downloadImageFile(view: View) {
    }

    fun test() {
        val u = hashMapOf("username" to "Roger Huang", "email_address" to "huangpeng@chunyu.me")
        val d = hashMapOf("success" to true, "user" to u)
        val jsonStr = JSON.toJSONString(d)

        val type = object : TCallback<Resp>() {}.getType()

        val resp = JSON.toObject<Resp>(jsonStr, type)

        val jsonObject = JSON.toObject<JsonElement>(jsonStr, object : TCallback<JsonElement>(){}.getType())
    }

    fun sendAndCancelAll(view: View) {
    }
}
