package me.chunyu.http.okhttp.response

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 14/11/2017.
 */

abstract class JSONDummy<T> {
    fun dummny() {

    }
}
abstract class JSON<T> {
    fun getType(): Type {
        val genType = this::class.java.genericSuperclass
        val params = (genType as ParameterizedType).actualTypeArguments
        val type = params[0]

        return type
    }

    companion object {
        val gsonBuilder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }

    fun toObject(jsonString: String): T {
        val gson = gsonBuilder.create()

        val type = getType()

        return gson.fromJson<T>(jsonString, type)
    }

    fun toJSONString(d: T): String {
//        val type = getType()

        val gson = gsonBuilder.create()

        return gson.toJson(d)
    }
}