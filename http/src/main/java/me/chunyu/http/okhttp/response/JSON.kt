package me.chunyu.http.okhttp.response

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.Reader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 14/11/2017.
 */

class JSON {
    companion object {
        val gsonBuilder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

        fun<T> toObject(jsonString: String, type: Type): T {
            val gson = gsonBuilder.create()

            return gson.fromJson<T>(jsonString, type)
        }

        fun<T> toObject(reader: Reader, type: Type): T {
            val gson = gsonBuilder.create()

            return gson.fromJson(reader, type)
        }

        fun<T> toJSONString(d: T): String {

            val gson = gsonBuilder.create()

            return gson.toJson(d)
        }
    }
}