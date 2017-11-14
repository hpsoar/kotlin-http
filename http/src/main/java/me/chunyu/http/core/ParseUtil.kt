package me.chunyu.http.core

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
