package me.chunyu.http.okhttp

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type
import java.util.HashMap

/**
 * Created by Roger Huang on 13/11/2017.
 */

class GsonParserFactory(private val gson: Gson = Gson()) : Parser.Factory() {

    override fun responseBodyParser(type: Type): Parser<ResponseBody, *>? {
        val typeAdapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return GsonResponseBodyParser(gson, typeAdapter)
    }

    override fun requestBodyParser(type: Type): Parser<*, RequestBody>? {
        val typeAdapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return GsonRequestBodyParser(gson, typeAdapter)
    }

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