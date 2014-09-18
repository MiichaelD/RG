package com.webs.itmexicali.rg.BattleShock.receivers;

import com.webs.itmexicali.rg.BattleShock.Main;
import com.webs.itmexicali.rg.BattleShock.SecretScreen;

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
