package me.chunyu.http.core

import me.chunyu.http.core.builder.MultipartRequestBuilder
import java.io.File

/**
 * Created by huangpeng on 12/11/2017.
 */
class KotMultipartRequest(builder: MultipartRequestBuilder) : KotPostRequest(builder) {
    var multiPartParameterMap : MutableMap<String, String> = mutableMapOf()
    var multiPartFileMap: MutableMap<String, File> = mutableMapOf()

    var percentageThresholdForCancelling : Int = 0

    init {
        multiPartFileMap = builder.mMultiPartFileMap
        multiPartParameterMap = builder.mMultiPartParameterMap
        percentageThresholdForCancelling = builder.mPercentageThresholdForCancelling
    }
}