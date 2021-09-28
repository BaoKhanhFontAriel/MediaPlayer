package com.example.MediaPlayer.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    private static final String INTENT = "INTENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent sendIntent = new Intent("com.example.videometadata");
        sendIntent.putExtra(INTENT, intent.getStringExtra(INTENT));
        context.sendBroadcast(sendIntent);
    }
}