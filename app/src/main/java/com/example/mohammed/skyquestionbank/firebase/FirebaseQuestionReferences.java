package com.example.mohammed.skyquestionbank.firebase;

import com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.DUEL_CHALLENGE;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.USER_POINT_REF;
import static com.example.mohammed.skyquestionbank.interfaces.FirebaseRefLinks.USER_REF;

public class FirebaseQuestionReferences {

    public static DatabaseReference getUserRef() {

        DatabaseReference userRef = getRef(USER_REF);
        return userRef.child(FirebaseAuth.getInstance().getUid());

    }


    public static DatabaseReference getUserRef(String uid) {

        DatabaseReference userRef = getRef(USER_REF);
        return userRef.child(uid);

    }

    public static DatabaseReference getUserPointRef() {
        DatabaseReference userPointRef = getUserRef();
        userPointRef = userPointRef.child(USER_POINT_REF);

        return userPointRef;

    }


    public static DatabaseReference getOnlineRef() {
        DatabaseReference onlineRef = getRef(FirebaseRefLinks.ONLINE_USERS);

        return onlineRef;

    }

    public static DatabaseReference getRef(String ref) {
        return FirebaseDatabase.getInstance().getReference(ref);
    }


    public static DatabaseReference getOnlineUserRef() {
        return getOnlineRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static DatabaseReference getMeAsChallengingRef(String opponentUid) {
        return getRef(DUEL_CHALLENGE).
                child(getUid())
                .child(opponentUid);

    }

    public static String getUid() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static DatabaseReference getMeAsOpponentRef(String uid) {
        return getRef(DUEL_CHALLENGE)
                .child(uid)
                .child(getUid());
    }

    public static DatabaseReference getOpponentOnCurrentQuestionRef(String uid, String playerType) {
        return getMeAsOpponentRef(uid)
                .child(playerType);
    }

    public static DatabaseReference getChallengerOnCurrentQuestionRef(String uid, String platerType) {
        return getMeAsChallengingRef(uid)
                .child(platerType);
    }

}
