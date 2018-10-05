package com.example.mohammed.skyquestionbank.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mohammed.skyquestionbank.interfaces.OnChallengeStatusChange;
import com.example.mohammed.skyquestionbank.interfaces.OnFirebaseValueSent;
import com.example.mohammed.skyquestionbank.interfaces.OnOnlineFriendListLoad;
import com.example.mohammed.skyquestionbank.interfaces.OnQuestionNumberListener;
import com.example.mohammed.skyquestionbank.interfaces.OnUserLoggedIn;
import com.example.mohammed.skyquestionbank.models.OnlineUser;
import com.example.mohammed.skyquestionbank.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE_STATUS;

public class FireBaseUtils {

    public static void sendUserWonPoints(int wonPoints, OnFirebaseValueSent onValueSent) {


        DatabaseReference userRef = FirebaseQuestionReferences.getUserPointRef();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int currentUnderpants = dataSnapshot.getValue(Integer.class);
                dataSnapshot.getRef().setValue(currentUnderpants + wonPoints)
                        .addOnCompleteListener(task -> onValueSent.onSent(wonPoints));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void createUser(OnUserLoggedIn loggedIn) {
        FirebaseQuestionReferences.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loggedIn.loggedIn();
                } else {
                    User user = new User();
                    user.setEasyQuestions(0);
                    user.setMediumQuestions(0);
                    user.setHardQuestions(0);
                    user.setPoints(0);
                    DatabaseReference userRef = FirebaseQuestionReferences.getUserRef();
                    userRef.setValue(user).
                            addOnCompleteListener(task -> loggedIn.loggedIn());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getOnlineFriendList(OnOnlineFriendListLoad friendListLoad) {

        DatabaseReference onlineList = FirebaseQuestionReferences.getOnlineRef();

        onlineList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<OnlineUser> users = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(OnlineUser.class));
                }
                friendListLoad.onLoadList(users);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void putUserOnline() {
        String playerId = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();

        OnlineUser user = new OnlineUser();

        user.setImgUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        user.setUserName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        user.setPlaying(false);
        user.setPlayerId(playerId);
        user.setUid(FirebaseQuestionReferences.getUid());

        DatabaseReference userOnlineRef = FirebaseQuestionReferences.getOnlineUserRef();
        userOnlineRef.setValue(user);

    }

    public static void getChallengeStatusChanges(OnChallengeStatusChange statusChange, String oppUid) {

        DatabaseReference duelChallengeRef = FirebaseQuestionReferences.getMeAsChallengingRef(oppUid)
                .child(DUEL_CHALLENGE_STATUS);


        duelChallengeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                    statusChange.onChange(dataSnapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setOnCurrentQuestionNumberListener(OnQuestionNumberListener numberListener, DatabaseReference challengerOnCurrentQuestionRef) {
        challengerOnCurrentQuestionRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.e("QUESTION", dataSnapshot.getRef().toString());
                        if (dataSnapshot.exists())
                            numberListener.onChange(dataSnapshot.getValue(Integer.class));
                        else
                            numberListener.onChange(1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }
}
