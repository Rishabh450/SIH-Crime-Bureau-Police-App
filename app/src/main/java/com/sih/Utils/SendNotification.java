package com.sih.Utils;


import android.util.Log;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class SendNotification {

    public SendNotification(String message, String heading, String notificationKey){


        try {
            JSONObject notificationContent = new JSONObject(
                    "{'contents':{'en':'" + message + "'},"+
                            "'include_player_ids':['" + notificationKey + "']," +
                            "'headings':{'en': '" + heading + "'}}");
            OneSignal.enableVibrate(true);
            OneSignal.enableSound(true);

            OneSignal.postNotification(notificationContent, null);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("sentNotif","net nhi hai");
        }
    }
}
