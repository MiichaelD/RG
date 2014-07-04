package com.webs.itmexicali.RealGaming.receivers;

import com.webs.itmexicali.RealGaming.Main;
import com.webs.itmexicali.RealGaming.SecretScreen;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SecretReceiver extends BroadcastReceiver{

	@Override
    public void onReceive(Context context, Intent intent) {
		if (Main.D) 
			Log.i("RealGaming-SecretReceiver", "Secret Combination received");
        if ("android.provider.Telephony.SECRET_CODE".equals(intent.getAction())) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, SecretScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
     }

}
