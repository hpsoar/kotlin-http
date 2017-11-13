package me.chunyu.http.core.builder

import me.chunyu.http.core.common.Method


/**
 * Created by huangpeng on 12/11/2017.
 */
open class GetRequestBuilder(url: String): RequestBuilder(url, Method.GET) {
}