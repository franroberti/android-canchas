package com.example.franco.myapplication2.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by franco on 9/27/17.
 */

public class NetworkUtils {
    final static  String ID_CANCHA = "59b0ac5ab729bb0042bfcc8b";
    final static String CANCHAS_URL =
            "https://canchas.ml/clubes";

    public static String getResponseFromHttpUrl() throws IOException {
        URL url = new URL(CANCHAS_URL + '/' + ID_CANCHA);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
