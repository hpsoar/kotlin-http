package me.chunyu.http.okhttp

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by Roger Huang on 13/11/2017.
 */

class GsonResponseBodyParser<out T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Parser<ResponseBody, T> {


    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val jsonReader = gson.newJsonReader(value.charStream())
        value.use {
            return adapter.read(jsonReader)
        }
    }
}