package me.chunyu.http.core

import me.chunyu.http.core.interfaces.KotConvertorFactory

/**
 * Created by huangpeng on 12/11/2017.
 */

// TODO: add more http related interfaces, eg. code, .., which should be relied on android system only
//       in very scarce cases, okHttp interface can be exposed
open class KotResponse(var error: KotError?){
    open fun isSuccess(): Boolean {
        return error == null
    }

    companion object {
        var convertorFactory: KotConvertorFactory? = null
    }
}
