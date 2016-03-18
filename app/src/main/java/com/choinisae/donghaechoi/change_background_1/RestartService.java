package com.choinisae.donghaechoi.change_background_1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by donghaechoi on 2016. 3. 17..
 */


public class RestartService {

    public static String ACTION_RESTART_SERVICE;

    public void onReceive(Context context, Intent intent) {
        //TODO Auto-generated method stub
        Log.d("ImmortalService", "RestartService called!@!@@@@@#$@$@#$@#$@#");

        if (intent.getAction().equals(ACTION_RESTART_SERVICE)) {
            Intent i = new Intent(context, MyService.class);
//Intent i = new Intent(this, PersistentService.class);
            context.startService(i);
        }
    }

}
