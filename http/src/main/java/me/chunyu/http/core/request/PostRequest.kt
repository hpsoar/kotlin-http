package me.chunyu.http.core

import me.chunyu.http.core.builder.PostRequestBuilder
import java.io.File

/**
 * Created by huangpeng on 12/11/2017.
 */
open class PostRequest(builder: PostRequestBuilder) : Request(builder) {
    var bodyParameterMap: MutableMap<String, String> = mutableMapOf()
    var urlEncodedFormBodyParameterMap: MutableMap<String, String> = mutableMapOf()
    var file: File? = null
    var bytes: ByteArray? = null
    var stringBody: String? = null
    var applicationJsonString: String? = null
    var customContentType: String? = null

    init {
        bodyParameterMap = builder.bodyParameterMap
        urlEncodedFormBodyParameterMap = builder.urlEncodedFormBodyParameterMap
        file = builder.file
        bytes = builder.bytes
        stringBody = builder.stringBody
        applicationJsonString = builder.applicationJsonString
        customContentType = builder.customContentType
    }
}