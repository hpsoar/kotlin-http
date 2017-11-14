package me.chunyu.http.okhttp.response

import me.chunyu.http.core.KotConvertor
import me.chunyu.http.core.interfaces.KotConvertorFactory
import okhttp3.Response
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 14/11/2017.
 */
class KotOkConvertorFactory : KotConvertorFactory {
    override fun <T> objectCovertor(type: Type): KotConvertor<T> {
        return ObjectConvertor(type)
    }

    override fun stringConvertor(): KotConvertor<String> {
        return StringConvertor()
    }
}