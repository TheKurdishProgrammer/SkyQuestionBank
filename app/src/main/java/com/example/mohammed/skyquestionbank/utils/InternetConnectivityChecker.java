package com.example.mohammed.skyquestionbank.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnectivityChecker {
    public static boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected();
        }

        return false;
    }
}
