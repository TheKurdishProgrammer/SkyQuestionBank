package com.example.mohammed.skyquestionbank.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.mohammed.skyquestionbank.R;
import com.example.mohammed.skyquestionbank.firebase.FireBaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class MainActivity extends AppCompatActivity {


    public final static int PLAYER_SINGLE = 1;
    public final static int PLAYER_MULTIPLE = 2;
    public final static String CHALLENGE_STATUS = "CHALLENGE_STATUS";
    private Toolbar toolbar;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_drawer);

        Bundle bundle2 = getIntent().getExtras();


        if (getIntent().hasExtra(CHALLENGE_STATUS))
            Log.e("HAS", "YES");

        int challengeStatus = getIntent().getIntExtra(CHALLENGE_STATUS, PLAYER_SINGLE);
        Log.e("PLAYER", "challenge Status" + challengeStatus);


        setupTheDrawer(savedInstanceState);

        ChooseQuestionCriteriaFragment questionCriteria = new ChooseQuestionCriteriaFragment();

        if (challengeStatus == PLAYER_SINGLE) {
            FireBaseUtils.putUserOnline();

        } else {
            Bundle bundle = new Bundle();

            String uid = getIntent().getStringExtra("uid");

            Log.e("PLAYER", "Im here" + uid);
            bundle.putInt(CHALLENGE_STATUS, challengeStatus);
            bundle.putString("uid", uid);

            questionCriteria.setArguments(bundle);
        }

        transactFragment(questionCriteria);
    }

    private void setupTheDrawer(Bundle savedInstance) {


        //creating the accountHeader
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.ic_launcher_background)
                .withTranslucentStatusBar(true)
                .withSavedInstance(savedInstance)
                .withHeaderBackground(R.drawable.question_back)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(new ProfileDrawerItem().withName(user.getDisplayName())
                        .withEmail(user.getEmail())
                        .withIcon(user.getPhotoUrl()))
                .build();

        //creating the drawer items
        PrimaryDrawerItem challengeYourSelf = new PrimaryDrawerItem();
        challengeYourSelf.withIdentifier(1).withIcon(R.drawable.ic_person_black_24dp).withName("New Challenge");


        PrimaryDrawerItem challengeFriend = new PrimaryDrawerItem();
        challengeFriend.withIdentifier(2).withIcon(R.drawable.ic_challenge_freind_black_24dp).withName("Challenge a Friend");

        PrimaryDrawerItem leaderBoards = new PrimaryDrawerItem();
        leaderBoards.withIdentifier(3).withIcon(R.drawable.ic_dashboard_black_24dp).withName("LeaderBoards");

        PrimaryDrawerItem about = new PrimaryDrawerItem();
        about.withIdentifier(4).withIcon(R.drawable.ic_info_black_24dp).withName("About");


        // building the drawer


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withShowDrawerOnFirstLaunch(true)
                .withShowDrawerUntilDraggedOpened(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(challengeYourSelf, challengeFriend, leaderBoards
                        , new DividerDrawerItem(), about
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    switch (position) {
                        case 1:
                            toolbar.setTitle("Challenge Yourself");
                            transactFragment(new ChooseQuestionCriteriaFragment());
                            break;
                        case 2:
                            transactFragment(new ChallengeFriendFragment());
                            break;

                        case 3:
                            transactFragment(new LeaderBoardsFragment());
                            break;
                        case 5:
                            transactFragment(new AboutFragment());
                            break;
                    }
                    return true;
                })
                .build();
    }

    private void transactFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_view, fragment);
        fragmentTransaction.commit();
        drawer.closeDrawer();
    }
}
