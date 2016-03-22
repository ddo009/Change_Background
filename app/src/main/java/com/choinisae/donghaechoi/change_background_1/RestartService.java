package com.choinisae.donghaechoi.change_background_1;

import android.content.Context;
import android.content.Intent;

/**
 * Created by donghaechoi on 2016. 3. 17..
 */


public class RestartService {

    public static String ACTION_RESTART_SERVICE;
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_RESTART_SERVICE)) {
            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }
    }

}
