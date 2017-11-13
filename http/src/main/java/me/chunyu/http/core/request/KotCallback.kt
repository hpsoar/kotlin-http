package me.chunyu.http.core

import me.chunyu.http.core.common.Progress

/**
 * Created by huangpeng on 12/11/2017.
 */
interface KotCallback {

    /** 请求网络开始前，UI线程  */
    fun onStart(request: KotRequest) {

    }

    /** 对返回数据进行操作的回调， UI线程  */
    fun onSuccess(response: KotResponse) {
    }

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程  */
    fun onError(error: KotError) {

    }

    /** 请求网络结束后，UI线程  */
    fun onFinish() {

    }

    /** 上传过程中的进度回调，get请求不回调，UI线程  */
    fun uploadProgress(progress: Progress) {

    }

    /** 下载过程中的进度回调，UI线程  */
    fun downloadProgress(progress: Progress) {

    }
}