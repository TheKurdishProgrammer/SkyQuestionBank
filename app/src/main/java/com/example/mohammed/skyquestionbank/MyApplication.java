package com.example.mohammed.skyquestionbank;

import android.app.Application;

import com.example.mohammed.skyquestionbank.oneSignal.MyNotificationOpenedHandler;
import com.example.mohammed.skyquestionbank.oneSignal.MyNotificationReceivedHandler;
import com.example.mohammed.skyquestionbank.utils.MyDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.onesignal.OneSignal;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DrawerImageLoader.init(new MyDrawerImageLoader());
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(getApplicationContext()))
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }


}
