package me.chunyu.http.core.builder

import me.chunyu.http.core.PostRequest
import me.chunyu.http.core.Request
import me.chunyu.http.core.ParseUtil
import me.chunyu.http.core.common.Method
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Created by huangpeng on 12/11/2017.
 */
open class PostRequestBuilder(url: String) : RequestBuilder(url, Method.POST) {
    val bodyParameterMap: MutableMap<String, String> = mutableMapOf()
    val urlEncodedFormBodyParameterMap: MutableMap<String, String> = mutableMapOf()

    var file: File? = null
    var bytes: ByteArray? = null
    var stringBody: String? = null
    var applicationJsonString: String? = null
    var customContentType: String? = null


    fun addBodyParameter(key: String, value: String): PostRequestBuilder {
        this.bodyParameterMap.put(key, value)

        return this
    }

    fun addBodyParameter(objectAny: Any): PostRequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { stringMap -> this.bodyParameterMap.putAll(stringMap) }
        return this
    }

    fun addBodyParamter(params: MutableMap<String, String>): PostRequestBuilder {
        this.bodyParameterMap.putAll(params)
        return this
    }

    fun addUrlEncodedFormBodyParamete(key: String, value: String): PostRequestBuilder {
        this.urlEncodedFormBodyParameterMap.put(key, value)
        return this
    }

    fun addUrlEncodedFormBodyParamete(objectAny: Any): PostRequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let { stringMap -> this.urlEncodedFormBodyParameterMap.putAll(stringMap) }
        return this
    }

    fun addUrlEncodedFormBodyParamete(params: MutableMap<String, String>): PostRequestBuilder {
        this.urlEncodedFormBodyParameterMap.putAll(params)
        return this
    }

    fun addStringBody(stringBody: String): PostRequestBuilder {
        this.stringBody = stringBody
        return this
    }

    fun addFileBody(file: File): PostRequestBuilder {
        this.file = file
        return this
    }

    fun addByteBody(bytes: ByteArray): PostRequestBuilder {
        this.bytes = bytes
        return this
    }

    fun addApplicationJsonBody(jsonObject: JSONObject): PostRequestBuilder {
        this.applicationJsonString = jsonObject.toString()
        return this
    }

    fun addApplicationJsonBody(jsonArray: JSONArray): PostRequestBuilder {
        this.applicationJsonString = jsonArray.toString()
        return this
    }

    fun addApplicationJsonBody(objectAny: Any): PostRequestBuilder {
        ParseUtil.parserFactory?.getString(objectAny)?.let { string -> this.applicationJsonString = string }
        return this
    }

    fun setContentType(contentType: String): PostRequestBuilder {
        this.customContentType = contentType
        return this
    }

    override fun build(): Request {
        return PostRequest(this)
    }
}