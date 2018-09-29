package com.example.mohammed.skyquestionbank.utils;

import android.os.Build;
import android.text.Html;

public class HTMLDecoder {

    public static String decodeHtml(String txt) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(txt, Html.FROM_HTML_MODE_LEGACY).toString();
        } else
            return Html.fromHtml(txt).toString();

    }
}
