package com.example.franco.myapplication

/**
 * Created by franco on 10/9/17.
 */

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * Created by franco on 9/27/17.
 */

class NetworkUtils {

    fun responseFromHttpUrl(url: URL): String? {

        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in` = urlConnection.inputStream

            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")

            val hasInput = scanner.hasNext()
            return if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }

}