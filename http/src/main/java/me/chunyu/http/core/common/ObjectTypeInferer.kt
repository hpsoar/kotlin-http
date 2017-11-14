package me.chunyu.http.core.request

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 14/11/2017.
 */
abstract class ObjectTypeInferer<T> {
    fun getType(): Type {
        val genType = this::class.java.genericSuperclass
        val params = (genType as ParameterizedType).actualTypeArguments
        val type = params[0]

        return type
    }
}

