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
import android.view.View

class PostApiTestActivity : AppCompatActivity() {

    companion object {

        private val TAG: String? = "PostApiTestActivity"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_api_test)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_post_as_json_object -> {
            }

            R.id.btn_post_as_json_array -> {
            }

            R.id.btn_post_as_string -> {
            }

            R.id.btn_check_header -> {

            }

            R.id.btn_create_user -> {
            }

            R.id.btn_create_new_user -> {
            }
        }
    }
}
