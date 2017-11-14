package me.chunyu.http.okhttp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by Roger Huang on 13/11/2017.
 */

interface Parser<in F, out T> {

    abstract class Factory {

        open fun getObject(string: String, type: Type): Any? {
            return null
        }

        open fun getString(objectAny: Any): String? {
            return null
        }

        open fun getStringMap(objectAny: Any): Map<String, String>? {
            return null
        }

    }
}