package com.example.mohammed.skyquestionbank.oneSignal;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OSNotificationSender {

    public static void sendNotification(String playerId, String uid) {
        AsyncTask.execute(() -> {

            try {
                String jsonResponse;

                URL url = new URL("https://onesignal.com/api/v1/notifications");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestMethod("POST");


                String strJsonBody = "{"
                        + "\"app_id\": \"01ea1fcb-281a-4934-a422-5fae6308c088\","
                        + "\"include_player_ids\": [\"" + playerId + "\"],"
                        + "\"data\": {\"uid\": \"" + uid + "\"},"
                        + "\"contents\": {\"en\": \"English Message\"}"
                        + "}";

                Log.e("JSON_BODY", strJsonBody);
//
//                String strJsonBody = "{"
//                        +   "\"app_id\": \"5eb5a37e-b458-11e3-ac11-000c2940e62c\","
//                        +   "\"include_player_ids\": [\"6392d91a-b206-4b7b-a620-cd68e32c3a76\",\"76ece62b-bcfe-468c-8a78-839aeaa8c5fa\",\"8e0f21fa-9a5a-4ae7-a9a6-ca1f24294b86\"],"
//                        +   "\"data\": {\"foo\": \"bar\"},"
//                        +   "\"contents\": {\"en\": \"English Message\"}"
//                        + "}";


                System.out.println("strJsonBody:\n" + strJsonBody);

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();
                Log.e("httpResponse: ", String.valueOf(httpResponse));


                if (httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                } else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                Log.e("jsonResponse:\n", jsonResponse);

            } catch (Throwable t) {
                t.printStackTrace();
            }

        });
    }
}
