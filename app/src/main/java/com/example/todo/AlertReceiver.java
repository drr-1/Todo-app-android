package com.example.todo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    public String n;
    @Override
    public void onReceive(Context context, Intent intent) {
        n= intent.getStringExtra("name");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(n);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
