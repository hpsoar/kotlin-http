package me.chunyu.http.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Roger Huang on 13/11/2017.
 */

class GsonParserFactory(private val gson: Gson = Gson()) : Parser.Factory() {

    override fun getObject(string: String, type: Type): Any? {
        try {
            return gson.fromJson(string, type)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    override fun getString(objectAny: Any): String? {
        return gson.toJson(objectAny)
    }

    override fun getStringMap(objectAny: Any): HashMap<String, String>? {
        try {
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            return gson.fromJson<HashMap<String, String>>(gson.toJson(objectAny), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return HashMap()
    }
}