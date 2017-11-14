package me.chunyu.http.core.interfaces

import me.chunyu.http.core.KotConvertor
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 14/11/2017.
 */
interface KotConvertorFactory {
    fun stringConvertor(): KotConvertor<String>

    fun<T> objectCovertor(type: Type): KotConvertor<T>
}