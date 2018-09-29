package com.example.mohammed.skyquestionbank.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.example.mohammed.skyquestionbank.models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int LOGIN_CODE = 3007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String x = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            startActivity(new Intent(this, MainActivity.class));
        }

        SignInButton mEmailSignInButton = findViewById(R.id.sign_in);

        mEmailSignInButton.setOnClickListener(view -> {
//            attemptLogin();

            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setLogo(R.drawable.busy_icon)
                            .setTheme(R.style.FirebaseUI)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(), LOGIN_CODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case LOGIN_CODE:
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {

                    User user = new User();
                    user.setEasyQuestions(0);
                    user.setMediumQuestions(0);
                    user.setHardQuestions(0);
                    user.setPoints(0);
                    FireBaseUtils.createUser(user, () -> {
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    });
                    return;
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled
                    return;
                }

                if (response != null)
                    if (response.getError() != null)
                        if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                            //showSnackbar(R.string.no_internet_connection);
                            return;
                        }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}




