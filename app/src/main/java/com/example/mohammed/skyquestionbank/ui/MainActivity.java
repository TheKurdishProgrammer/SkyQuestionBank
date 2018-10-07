package com.example.mohammed.skyquestionbank.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public final static String UID = "uid";
    public final static int PLAYER_SINGLE = 1;
    public final static int PLAYER_MULTIPLE = 2;
    public final static String CHALLENGE_STATUS = "CHALLENGE_STATUS";
    private static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    private static final String CURRENT_FRAGMENT_INDEX = "CURRENT_FRAGMENT_INDEX";
    private Toolbar toolbar;
    private Drawer drawer;
    private List<Fragment> fragments;
    private int currentFragmentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_drawer);
        fragments = new ArrayList<>();
        int challengeStatus = getIntent().getIntExtra(CHALLENGE_STATUS, PLAYER_SINGLE);


        setupTheDrawer(savedInstanceState);


        fragments.add(new ChooseQuestionCriteriaFragment());
        fragments.add(new ChallengeFriendFragment());
        fragments.add(new LeaderBoardsFragment());
        fragments.add(new AboutFragment());


        if (savedInstanceState != null) {
            currentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT_INDEX);
            Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT);
            fragments.set(currentFragmentIndex, fragment);
        }


        if (currentFragmentIndex == 0 || savedInstanceState == null) {
            if (challengeStatus == PLAYER_SINGLE) {
                toolbar.setTitle(getString(R.string.choose_criteria));
                FireBaseUtils.putUserOnline();

            } else {
                toolbar.setTitle(getString(R.string.challenge_friend));
                Bundle bundle = new Bundle();

                String uid = getIntent().getStringExtra(UID);

                bundle.putInt(CHALLENGE_STATUS, challengeStatus);
                bundle.putString(UID, uid);

                fragments.get(0).setArguments(bundle);
            }
        }
        transactFragment(fragments.get(currentFragmentIndex));

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
        challengeYourSelf.withIdentifier(1).withIcon(R.drawable.ic_person_black_24dp).withName(R.string.new_challenge);


        PrimaryDrawerItem challengeFriend = new PrimaryDrawerItem();
        challengeFriend.withIdentifier(2).withIcon(R.drawable.ic_challenge_freind_black_24dp).withName(getString(R.string.challenge_friend));

        PrimaryDrawerItem leaderBoards = new PrimaryDrawerItem();
        leaderBoards.withIdentifier(3).withIcon(R.drawable.ic_dashboard_black_24dp).withName(getString(R.string.leadrboards));

        PrimaryDrawerItem about = new PrimaryDrawerItem();
        about.withIdentifier(4).withIcon(R.drawable.ic_info_black_24dp).withName(getString(R.string.about));


        // building the drawer


        drawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstance)
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
                            currentFragmentIndex = 0;
                            toolbar.setTitle(R.string.choose_criteria);
                            break;
                        case 2:
                            currentFragmentIndex = 1;
                            toolbar.setTitle(getString(R.string.challenge_friend));
                            break;
                        case 3:
                            currentFragmentIndex = 2;
                            toolbar.setTitle(getString(R.string.leadrboards));
                            break;
                        case 5:
                            currentFragmentIndex = 3;
                            toolbar.setTitle(getString(R.string.about));
                            break;
                    }

                    transactFragment(fragments.get(currentFragmentIndex));
                    return true;
                })
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        drawer.saveInstanceState(outState);


        outState.putInt(CURRENT_FRAGMENT_INDEX, currentFragmentIndex);

        if (!getSupportFragmentManager().executePendingTransactions())
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, fragments.get(currentFragmentIndex));

        super.onSaveInstanceState(outState);
    }

    private void transactFragment(Fragment fragment) {


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_view, fragment);
        fragmentTransaction.commitNow();
        drawer.closeDrawer();
    }
}
