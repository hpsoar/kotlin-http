package me.chunyu.http.core.builder

import me.chunyu.http.core.KotMultipartRequest
import me.chunyu.http.core.KotRequest
import me.chunyu.http.core.ParseUtil
import java.io.File

/**
 * Created by huangpeng on 12/11/2017.
 */
class MultipartRequestBuilder(url: String) : PostRequestBuilder(url) {
    var mMultiPartParameterMap : MutableMap<String, String> = mutableMapOf()
    var mMultiPartFileMap: MutableMap<String, File> = mutableMapOf()

    var mPercentageThresholdForCancelling : Int = 0

    fun addMultiPartParameter(key: String, value: String): MultipartRequestBuilder {
        mMultiPartParameterMap.put(key, value)
        return this
    }

    fun addMultiPartParameter(objectAny: Any): MultipartRequestBuilder {
        ParseUtil.parserFactory?.getStringMap(objectAny)?.let {
            stringMap -> this.mMultiPartParameterMap.putAll(stringMap)
        }
        return this
    }

    fun addMultiPartParameter(params : MutableMap<String, String>): MultipartRequestBuilder {
        mMultiPartParameterMap.putAll(params)
        return this
    }


    fun addMultiPartFile(key: String, file: File): MultipartRequestBuilder {
        mMultiPartFileMap.put(key, file)
        return this
    }

    fun addMultiPartFile(params: MutableMap<String, File>): MultipartRequestBuilder {
        mMultiPartFileMap.putAll(params)
        return this
    }

    fun setPercentageThresholdForCancelling(threshold: Int): MultipartRequestBuilder {
        mPercentageThresholdForCancelling = threshold
        return this
    }

    override fun build(): KotRequest {
        return KotMultipartRequest(this)
    }
}