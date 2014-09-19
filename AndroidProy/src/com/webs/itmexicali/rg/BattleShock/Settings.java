package com.webs.itmexicali.rg.BattleShock;

import com.webs.itmexicali.rg.BattleShock.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings {

	private static Settings instance;
	private final String TAG = Main.TAG+"-Settings";
	
	//this class context. must have for shared preferences
    private Context mContext;
	
	//Setting's KEY names
    public static final String SERVER_IP = "serverIP", USER_ID = "userID",
    		PLAYER_JSON = "playerJSON", TEAM_JSON = "teamJSON";
    
    //Server's address Cons
	private static final String regEvent="/realgaming/battleshock/reg_event.php", defaultSRVAddress = "http://pcmxl.com";
	
	private String SRVaddress = defaultSRVAddress;
	
	private SharedPreferences prefs;// To save preferences accesible by other Apps, just 
    private SharedPreferences.Editor prefsEditor;
	
	/** Constructor */
	public Settings(Context ctx){
		mContext = ctx;
		SRVaddress = getPref().getString(SERVER_IP, defaultSRVAddress);
	}
	
	/** Singleton */
	public static synchronized Settings getIns(Context ctx) {
		if(instance == null)
			instance = new Settings(ctx);
		return instance;
	}
	
	/**  get the sharedPreferences for this app*/ 
	public SharedPreferences getPref(){
		try{
			if (prefs == null || prefs.getAll() == null){
				prefs = mContext.getSharedPreferences(Main.TAG, 0);	//to read
				prefsEditor = prefs.edit();						//to write
			}
		}catch (Exception e){
			prefs = mContext.getSharedPreferences(Main.TAG, 0);	//to read
			prefsEditor = prefs.edit();						//to write
		}
		return prefs;
	}
	
	
	/** @return the saved server address */
	public String getSRVads(){
		return SRVaddress;
	}
	

	/**Change server address
	 * @param sIP the new server address */
	public void setSRVads(String sIP){
		if(sIP != null){
			SRVaddress = sIP;
			prefsEditor.putString(SERVER_IP, sIP);
        	prefsEditor.commit();
		}
	}
	
	/**
	 * @return Server address +event-register php's path. If the server
	 * address is different from default address it will just return the SRVaddress
	 * saved (NOT INCLUIDING THE PATH TO REG_EVENT*/
	public String getRegEventAdd(){
		if( SRVaddress.equals(defaultSRVAddress))
			return SRVaddress+regEvent;
		return SRVaddress;
	}
	
	public Dialog createMenuDialog(Context context){
		
	    final Dialog menu = new Dialog(context);
        menu.setContentView(R.layout.options);
        menu.setTitle(R.string.changeValues);
        menu.setCancelable(true);
        
        ((EditText)menu.findViewById(R.id.editText1)).setText(getSRVads());
        
        //give functionality to button1;
        Button button = (Button) menu.findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
       	 		if(Main.D) Log.i(TAG,"Change - OK");
            	String serverIP =(((EditText)menu.findViewById(R.id.editText1)).getText()).toString();
            	setSRVads(serverIP);
            	Toast.makeText(Main.instance, R.string.confUpdated, Toast.LENGTH_SHORT).show();
            	menu.dismiss();
            	if (Main.D){  Log.i(TAG,"SettingsEditor Commited Succesfully"); 	}
            }
        });
        
        //give functionality to second button
        button = (Button) menu.findViewById(R.id.button3);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
       	 		if(Main.D)	Log.i(TAG,"Change - CANCEL");
                menu.dismiss();
            }
        });
        
        return menu;
	}
}
