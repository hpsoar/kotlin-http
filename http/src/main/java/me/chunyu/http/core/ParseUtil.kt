package me.chunyu.http.core

import me.chunyu.http.okhttp.GsonParserFactory
import me.chunyu.http.okhttp.Parser

/**
 * Created by Roger Huang on 13/11/2017.
 */


class ParseUtil {
    companion object {
        var parserFactory: Parser.Factory? = GsonParserFactory()
        fun shutDown() {
            parserFactory = null
        }
    }
}
