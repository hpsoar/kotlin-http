package me.chunyu.http.okhttp.response

import okhttp3.Response

/**
 * Created by Roger Huang on 13/11/2017.
 */
class ResponseCloser {
    constructor(response: Response) {
        try {
            response.body().close()
        } catch (e: Exception) {

        }
    }
}