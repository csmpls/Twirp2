package com.csmpls.twirp2;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    
        Intent scheduledIntent = new Intent(context, MainActivity.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
    }
}
