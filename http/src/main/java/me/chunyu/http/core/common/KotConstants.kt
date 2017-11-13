package me.chunyu.http.core.common

/**
 * Created by Roger Huang on 13/11/2017.
 */

class KotConstants {

    companion object {
        val UPDATE: Int = 0x01
        val CONNECTION_ERROR = "connectionError"
        val PARSE_ERROR = "parseError"
        val RESPONSE_FROM_SERVER_ERROR = "responseFromServerError"
        val REQUEST_CANCELLED_ERROR = "requestCancelledError"
        val USER_AGENT = "User-Agent"
    }

}