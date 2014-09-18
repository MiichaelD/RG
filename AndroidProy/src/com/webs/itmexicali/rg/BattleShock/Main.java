package com.webs.itmexicali.rg.BattleShock;

import com.webs.itmexicali.rg.BattleShock.R;
import com.webs.itmexicali.rg.BattleShock.comm.BlueConn;
import com.webs.itmexicali.rg.BattleShock.comm.BluetoothService;
import com.webs.itmexicali.rg.BattleShock.comm.MessageManager;
import com.webs.itmexicali.rg.BattleShock.frags.ScreenSlide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
@SuppressLint("HandlerLeak")
public class Main extends Activity {
	
	//this activity Instance
	public static Main instance;
	
    // Debugging
    public static final String TAG = "RealGaming";
    public static final boolean D = true;

    // Intent request codes
    public static final int	REQUEST_CONNECT_DEVICE = 1, REQUEST_ENABLE_BT = 2, REQUEST_LOGIN = 3;
    
    // Layout Views
	public  PadView PView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        setContentView((PView=new PadView(this)));
        instance=this;
        
        //init Settings
        Settings.getIns(this);
        
        //Apply sensorLandscape for screnOrientation to devices running API level 9+
        if (Build.VERSION.SDK_INT >= 9) {
            setRequestedOrientation(6);
        }    
        
        // get bluetooth adapter, If the BluetoothAdapter is null, then Bluetooth is not supported
        if (BlueConn.getBlueAdapter() == null) {
        	MessageManager.getIns().mUIsend("Bluetooth is not available",true);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        //Check if user is logged in, if not: go to LogIn Screen
        if (SessionManager.requestLogin(this)) {
	        //check if bluetooth is enabled & setupBT
	        BlueConn.requestBluetoothEnabled(this);
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        
        //update android's current session info with server's
        if(SessionManager.isLoggedIn(this))
        	MessageManager.getIns().fetchPlayerData();
        
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        BlueConn.requestBlueServiceStart();
        
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BlueConn.destroyBT();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

     
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
            	//Connect to the selected device
            	if (! BlueConn.connectToDevice(data)){
                	Toast.makeText(getApplicationContext(), R.string.illegaldevice, Toast.LENGTH_SHORT).show();
            	}               
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a BT session
                BlueConn.setupBT();
            } else {
                // User did not enable Bluetooth or an error occured
                if(D)Log.d(TAG, "BT not enabled");
                //MessageManager.getIns().mUIsend(R.string.bt_not_enabled_leaving, false);
                //finish();
            }
            break;
            
        case REQUEST_LOGIN:
        	if (resultCode == Activity.RESULT_OK){
        		Settings.getIns(this).getPref().edit().putBoolean(Settings.USER_ID, true).commit();
        		if(D)Log.d(TAG, "OnActivityResult - User Logged in successfully");
        	} else {
        		if(D)Log.d(TAG, "OnActivityResult - User didn't Logged in");
        		finish();
        	} 
        	break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        if(Build.VERSION.SDK_INT>11){
        	//items on actionBar
        }
        return true;
    }
    
    public boolean onPrepareOptionsMenu(Menu menu){
    	if (BlueConn.getBlueAdapter().isEnabled() && BluetoothService.mBlueService != null){
    		menu.findItem(R.id.scan).setVisible(true);
        	menu.findItem(R.id.discoverable).setVisible(true);	
        }
        else{
        	menu.findItem(R.id.scan).setVisible(false);
        	menu.findItem(R.id.discoverable).setVisible(false);
        	BluetoothService.mBlueService = null;
        }
    	return super.onPrepareOptionsMenu(menu);
    }
    
    
    public void startBluetoothScan(){
    	Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
            // Launch the DeviceListActivity to see devices and do scan
            startBluetoothScan();
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
        	SessionManager.Logout();
    		SessionManager.requestLogin(this);
        	return true;
        case R.id.exit:
        	finish();
        	return true;
        }
        return false;
    }

    /** 
     * Called Before onPause(). 
     * */
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
      	if(D){
    		Log.i(TAG,"**OnSaveInstanceState**");
    	}    	  
   	}

    /** Called Before onResume */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	  super.onRestoreInstanceState(savedInstanceState);
    	  if(D){
      		Log.i(TAG,"*OnRestoreInstanceState*");
      	}
    }
    
}