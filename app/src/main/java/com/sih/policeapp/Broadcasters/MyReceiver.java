package com.sih.policeapp.Broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.sih.policeapp.Services.MyService;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "bootcomplete");
        Toast.makeText(context, "Service started after boot", Toast.LENGTH_SHORT).show();
        //context.startService(new Intent(context, MyService.class));
        // PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_NO_CREATE);


            context.startService(new Intent(context, MyService.class));

    }
}
