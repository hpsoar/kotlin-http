package com.mindorks.kotnetworking.request

/**
 * Created by huangpeng on 12/11/2017.
 */
interface KotHttpClient {
    fun makeSimpleRequestAndGetResponse(kotRequest: KotRequest): Any?
    fun makeDownloadRequestAndGetResponse(kotRequest: KotRequest): Any?
    fun makeUploadRequestAndGetResponse(kotRequest: KotRequest): Any?
}