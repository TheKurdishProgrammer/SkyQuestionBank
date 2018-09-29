package com.example.mohammed.skyquestionbank.oneSignal;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {

        // here say player got invitation
    }
}