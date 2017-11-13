package me.chunyu.http.core.builder

import me.chunyu.http.core.DownloadRequest
import me.chunyu.http.core.Request

/**
 * Created by huangpeng on 12/11/2017.
 */
class DownloadRequestBuilder(url: String) : GetRequestBuilder(url) {
    var percentageThresholdForCancelling: Int = 0

    fun setPercentageThresholdForCancelling(percentageThresholdForCancelling: Int): DownloadRequestBuilder {
        this.percentageThresholdForCancelling = percentageThresholdForCancelling
        return this
    }

    override fun build(): Request {
        return DownloadRequest(this)
    }
}