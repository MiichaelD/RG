package com.webs.itmexicali.rg.BattleShock;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class SessionManager {

	private static final String TAG = Main.TAG+"-Session";
	
	/** If User is not logged in, request to login 
	 * @param activity current activity
     * @return true if user is logged in*/
	public static boolean requestLogin(Activity activity){
	    if( !Settings.getIns(activity).getPref().getBoolean(Settings.USER_ID, false) ){
	    	if(Main.D) Log.e(TAG, "++ ON START ++ - USER IS NOT LOGGED IN");
	    	Intent intent = new Intent(activity, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivityForResult(intent,Main.REQUEST_LOGIN);
			return false;
	    }
	    return true;
	}
	
	/** Check if user has logged in
	 * @param activity current activity
     * @return true if user is logged in*/
	public static boolean isLoggedIn(Activity activity){
	    if( !Settings.getIns(activity).getPref().getBoolean(Settings.USER_ID, false) ){
			return false;
	    }
	    return true;
	}
	
	/** delete current player info from android's session */
	public static void Logout(){
		Settings.getIns(null).getPref().edit()
		.remove(Settings.USER_ID)
		.remove(Settings.PLAYER_JSON)
		.remove(Settings.TEAM_JSON)
		.commit();
	}
	
}
