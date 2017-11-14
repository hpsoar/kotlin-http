package me.chunyu.http.core

import me.chunyu.http.core.interfaces.KotConvertorFactory

/**
 * Created by huangpeng on 12/11/2017.
 */
open class KotResponse(var error: KotError?){
    open fun isSuccess(): Boolean {
        return error == null
    }

    companion object {
        var responseFactory: KotConvertorFactory? = null
    }
}
