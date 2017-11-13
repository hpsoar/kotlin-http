package me.chunyu.http.core

import me.chunyu.http.core.builder.DownloadRequestBuilder

/**
 * Created by huangpeng on 12/11/2017.
 */
class DownloadRequest(builder: DownloadRequestBuilder) : Request(builder) {
    var percentageThresholdForCancelling: Int = 0
    init {
        percentageThresholdForCancelling = builder.percentageThresholdForCancelling
    }
}