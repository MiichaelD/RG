package com.webs.itmexicali.RealGaming;

import com.webs.itmexicali.RealGaming.R;
import com.webs.itmexicali.RealGaming.comm.BlueConn;
import com.webs.itmexicali.RealGaming.frags.ScreenSlide;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
public class SecretScreen extends FragmentActivity{

	private final String TAG = Main.TAG+"-SecretScreen";
	
	static SecretScreen instance;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        instance = this;
	        
	        //Apply sensorLandscape for screnOrientation to devices running API level 9+
	        if (Build.VERSION.SDK_INT >= 9) {
	            setRequestedOrientation(6);
	        }
	        
	        if(Main.D)Log.d(TAG,  "++OnCreate++");
	        setContentView(R.layout.fragment_event_simulator);
	 }
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.option_menu, menu);
			return true;
	 }
	 
	 public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.scan:
	            // Launch the DeviceListActivity to see devices and do scan
	            Intent serverIntent = new Intent(this, DeviceListActivity.class);
	            startActivityForResult(serverIntent, Main.REQUEST_CONNECT_DEVICE);
	            return true;
	        case R.id.discoverable:
	            // Ensure this device is discoverable by others
	            BlueConn.ensureDiscoverable(this);
	            return true;
	        case R.id.game_monitor:
	        	startActivity(new Intent(this, ScreenSlide.class));
	        	return true;
	        case R.id.options:
	        	//show options menu dialog
	        	Settings.getIns(this).createMenuDialog(this).show();
	        	return true;
	        case R.id.logout:
	        	if (Settings.getIns(this).getPref().contains(Settings.USER_ID))
	        		SessionManager.Logout();
	        	else
	        		Settings.getIns(this).getPref().edit().putBoolean(Settings.USER_ID, true).commit();
	        	return true;
	        case R.id.exit:
	        	finish();
	        	return true;
	        }
	        return false;
	    }	        
	
}
