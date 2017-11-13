package me.chunyu.http.core

import com.mindorks.kotnetworking.request.*
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
//    override fun convertResponse(response: CYResponse): CYResponse<Response> {
//        if (response is CYOKResponse) {
//            if (response.okHttpResponse != null) {
//                if (response.okHttpResponse.code() < 400) {
//                    return CYResponse(response.okHttpResponse, response.okHttpResponse)
//                } else {
//                    val error = KotError()
//                    error.errorCode = response.okHttpResponse.code()
//                    error.errorDetail = response.okHttpResponse.message()
//                    return CYResponse(error, response.okHttpResponse)
//                }
//            } else {
//                val error = KotError()
//                error.errorCode = -1
//                error.errorDetail = "failed to create response"
//                return CYResponse(error, response.okHttpResponse)
//            }
//        } else {
//            return CYResponse(KotError.unsupported(response), null)
//        }
//    }
//}
//
//open class JSONArrayCallback: CYConverter<JSONArray> {
//    override fun convertResponse(response: CYRawResponse): CYResponse<JSONArray> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return CYResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            val json = JSONArray(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8())
//
//            CYResponse(json, resp.rawResponse)
//        } catch (e: Exception) {
//            CYResponse(KotError.convertFailure<JSONArray>(), resp.rawResponse)
//        }
//    }
//}
//
//open class CYStringConvertor : CYConverter<String> {
//    override fun convertResponse(response: CYRawResponse): CYResponse<String> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return CYResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            CYResponse(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8(), resp.rawResponse)
//        } catch (e: Exception) {
//            CYResponse(KotError.convertFailure<String>(), resp.rawResponse)
//        }
//    }
//}
//
//open class JSONObjectCallback: CYConverter<JSONObject> {
//    override fun convertResponse(response: CYRawResponse): CYResponse<JSONObject> {
//        val resp = OKCallback().convertResponse(response)
//        if (resp.throwable != null) {
//            return CYResponse(resp.throwable!!, resp.rawResponse)
//        }
//
//        return try {
//            val json: JSONObject = JSONObject(Okio.buffer(resp.rawResponse!!.body().source()).readUtf8())
//            CYResponse(json, resp.rawResponse)
//        } catch (e: Exception) {
//            CYResponse(KotError.convertFailure<JSONObject>(), resp.rawResponse)
//        }
//
//    }
//}
