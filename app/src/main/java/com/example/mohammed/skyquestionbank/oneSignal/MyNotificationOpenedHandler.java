package com.example.mohammed.skyquestionbank.oneSignal;

import android.content.Context;
import android.content.Intent;

import com.example.mohammed.skyquestionbank.firebase.FirebaseQuestionReferences;
import com.example.mohammed.skyquestionbank.ui.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_STATUS;
import static com.example.mohammed.skyquestionbank.ui.DuelChallengeActivity.OPPONENR_ON_CRITERIA;
import static com.example.mohammed.skyquestionbank.ui.MainActivity.CHALLENGE_STATUS;
import static com.example.mohammed.skyquestionbank.ui.MainActivity.PLAYER_MULTIPLE;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private Context context;

    public MyNotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

        // here update the real time saying player received your
        JSONObject jsonObject = result.notification.payload.additionalData;

        if (jsonObject != null) {

            String uid = null;
            try {
                uid = jsonObject.getString("uid");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            DatabaseReference meAsOpponentRef = FirebaseQuestionReferences.getMeAsOpponentRef(uid);

            meAsOpponentRef.child(DUEL_CHALLENGE_STATUS).setValue(OPPONENR_ON_CRITERIA);



            Intent intent = new Intent(context, MainActivity.class);

            intent.putExtra(CHALLENGE_STATUS, PLAYER_MULTIPLE);
            intent.putExtra("uid", uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        }
    }
}