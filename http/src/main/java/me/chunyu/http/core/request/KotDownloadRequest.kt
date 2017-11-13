package me.chunyu.http.core

import me.chunyu.http.core.builder.DownloadRequestBuilder

/**
 * Created by huangpeng on 12/11/2017.
 */
class KotDownloadRequest(builder: DownloadRequestBuilder) : KotRequest(builder) {
    var percentageThresholdForCancelling: Int = 0
    init {
        percentageThresholdForCancelling = builder.percentageThresholdForCancelling
    }
}