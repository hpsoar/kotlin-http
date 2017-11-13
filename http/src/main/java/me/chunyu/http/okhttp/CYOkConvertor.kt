package me.chunyu.http.core

import okhttp3.Response
import okio.Okio
import org.json.JSONArray
import org.json.JSONObject

fun<T> KotError.Companion.unsupported(t: T) : KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "unsupportted response: " + t.toString()
    return error
}

inline fun<reified T> KotError.Companion.convertFailure(): KotError {
    val error = KotError()
    error.errorCode = -1
    error.errorDetail = "failed to convert response to:" + T::class
    return error
}

/**
 * Created by huangpeng on 12/11/2017.
 */
//open class OKCallback: CYConverter<Response> {
//    override fun convertResponse(response: KotResponse): KotResponse<Response> {
//        if (response is CYOKResponse) {
//            if (response.okHttpResponse != null) {
//                if (response.okHttpResponse.code() < 400) {
//                    return KotResponse(response.okHttpResponse, response.okHttpResponse)
//                } else {
//                    val error = KotError()
//                    error.errorCode = response.okHttpResponse.code()
//                    error.errorDetail = response.okHttpResponse.message()
//                    return KotResponse(error, response.okHttpResponse)
//                }
//            } else {
//                val error = KotError()
//                error.errorCode = -1
//                error.errorDetail = "failed to create response"
//                return KotResponse(error, response.okHttpResponse)
//            }
//        } else {
//            return KotResponse(KotError.unsupported(response), null)
//        }
//    }
//}
//
//open class JSONArrayCallback: CYConverter<JSONArray> {
//    override fun convertResponse(response: CYRawResponse): KotResponse<JSONArray> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return KotResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            val json = JSONArray(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8())
//
//            KotResponse(json, resp.rawResponse)
//        } catch (e: Exception) {
//            KotResponse(KotError.convertFailure<JSONArray>(), resp.rawResponse)
//        }
//    }
//}
//
//open class CYStringConvertor : CYConverter<String> {
//    override fun convertResponse(response: CYRawResponse): KotResponse<String> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return KotResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            KotResponse(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8(), resp.rawResponse)
//        } catch (e: Exception) {
//            KotResponse(KotError.convertFailure<String>(), resp.rawResponse)
//        }
//    }
//}
//
//open class JSONObjectCallback: CYConverter<JSONObject> {
//    override fun convertResponse(response: CYRawResponse): KotResponse<JSONObject> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return KotResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            val json: JSONObject = JSONObject(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8())
//            KotResponse(json, resp.rawResponse)
//        } catch (e: Exception) {
//            KotResponse(KotError.convertFailure<JSONObject>(), resp.rawResponse)
//        }
//
//    }
//}
