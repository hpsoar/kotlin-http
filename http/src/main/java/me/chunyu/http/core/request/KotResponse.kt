package me.chunyu.http.core

/**
 * Created by huangpeng on 12/11/2017.
 */
open class KotResponse(var error: KotError?){
    open fun isSuccess(): Boolean {
        return error == null
    }
}
