package com.webs.itmexicali.RealGaming.comm;

import java.net.HttpURLConnection;
import java.util.Locale;

import org.json.*;

import com.webs.itmexicali.RealGaming.Main;
import com.webs.itmexicali.RealGaming.OnLoginListener;
import com.webs.itmexicali.RealGaming.Settings;
import com.webs.itmexicali.RealGaming.R;
import com.webs.itmexicali.RealGaming.users.Player;
import com.webs.itmexicali.RealGaming.users.Team;

import android.util.Log;
import android.widget.Toast;

public class MessageManager {

	// Debuging TAG
	private static final String TAG = Main.TAG + "-msgManager";
	
	// stored responses
	public String BTreceived = null, SRVreceived = null;
	
	private final int RESPONSE_OK = 200, RESPONSE_DENIED = 400;
	// Server communication events;
	public static class Events{
		public static final String  SHOT="shot", KILL="kill", 
				LOGIN="login", RECOVER="recover", UPDATE="update",
				GETINFO="get_my_info";
	}
	

	//Event Listeners
	OnLoginListener mLoginListener;
	
	
	private static MessageManager instance;
	
	/** private constructor to prevent multiple Message Managers. */
	private MessageManager() {}

	/** Singleton 
	 * @return the only instance */
	public static MessageManager getIns() {
		return instance == null? (instance = new MessageManager()) : instance;
	}

	
	
	/**This method sends a toast to the device's screen in a new thread
	 * @param msg message to be print on screen
	 * @param Long 0 for short, 1 for long */
	public void mUIsend(final String msg, final boolean Long){
		Main.instance.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(Main.instance, msg, Long?Toast.LENGTH_LONG:Toast.LENGTH_SHORT).show();
			}
		});		
	}
	
	/**This method sends a toast to the device's screen in a new thread
	 * given a String ID 
	 * @param msgID String's ID 
	 * @param Long false for short, true for long */
	public void mUIsend(final int msgId, boolean Long){
		mUIsend(Main.instance.getString(msgId),Long);
	}
	
	/**This method sends a toast to the device's screen in a new thread
	 * given a many StringIDs, concatenating it and adding a space between strings.
	 * @param msgIds String's IDs  to be concatenated
	 * @param Long false for short, true for long */
	public void mUIsend(boolean Long, final int... msgIds){
		int i = 0, msgsLen = msgIds.length;
		StringBuilder sb = new StringBuilder();
		for(; i < msgsLen; i++){
			sb.append(Main.instance.getString(msgIds[i]));
			sb.append(' ');
		}
		sb.deleteCharAt(msgsLen-1);
		mUIsend(sb.toString(),Long);
	}
	
	/** This method handles the message received through the bluetooth interface
	 * on a new thread
	 * @param bytes total of bytes received
	 * @param msg byteArray storing the msg	 */
	public void  mBluetoothReceived(final int bytes, final byte[] msg) {
		new Thread(new Runnable(){
			public void run(){
				byte[] readBuf = (byte[]) msg;
				//construct a string from the valid bytes in the buffer
				if( bytes > 0 ){
					String bt = new String(readBuf, 0, bytes);
					bt = Player.current().getID() == 24 ?	"14":"24";
					processReceivedBT(bt);
				}
			}
		}).start();
	}

	
	/** This method handles the message received through the bluetooth interface
	 * on a new thread
	 * @param msg String storing the msg	 */
	public void  mBluetoothReceived(final String msg) {
		new Thread(new Runnable(){
			public void run(){
				processReceivedBT(msg);
			}
		}).start();
	}
	
	
	/** Do an action based on the bluetooth message received.	 */
	private void processReceivedBT(String msg){
		if (Main.D)
			Log.i(TAG, "Bluetooth received: \"" + msg + "\"");
		try{
			Integer.parseInt(msg);
			Player.current().gotShot(msg);
		}catch(Exception e){
			if(msg.charAt(0) == 'S'){
				Player.current().shoot();
			}
			else if(msg.charAt(0) == 'R'){
				Player.current().reload();
			}
		}	
	}
	
	
	
	/** This method sends a message to another device already connected via
	 * bluetooth if the message is not empty, on a new thread 
	 * @param msg  Message to be sent
	 * @return true if the message was sent, false if there was no connection	 */
	public boolean mBluetoothSend(final String msg) {
		// Check that we're actually connected before trying anything
		if (BluetoothService.mBlueService.getState() == BluetoothService.STATE_CONNECTED && msg.length() > 0) {
			new Thread(new Runnable() {
				public void run() {
					//Get the message bytes and tell the BlueInterfaceService to write
					byte[] send = msg.getBytes();
					BluetoothService.mBlueService.write(send);
					if (Main.D)
						Log.d(TAG, "(BT)Message Sent: " + msg);
				}
			}).start();
			return true;
		}
		return false;
	}

	/**This method handles the message received through the wifi interface
	 * @param msg string storing the message */
	public void mInternetReceived(final String response, int responseCode) {
		if (response == null || response.equals(""))
			return;
		
		if(Main.D){
			Log.d(TAG, "SRVR response: " + responseCode + " - "+ response);
		}
		
		//mUIsend(response,true);
		
		switch(responseCode){
		case RESPONSE_OK: // OK
			try {
				JSONObject res= new JSONObject(response);
				String action = res.getString("event");
				if(action.equals(Events.LOGIN)){
					if( mLoginListener != null)
						mLoginListener.onLoginSuccess(response);
				}
				else if(action.equals(Events.GETINFO)){
					Player.current().initPlayer( res.getJSONObject("player") );
					Team.current().initTeam( res.getJSONObject("team") );
				}
				
				else if(action.equals(Events.UPDATE)){
					
				}
				/*
				else if(action.equals(Events.KILL)){	}
				else if(action.equals(Events.RECOVER)){	}
				else if(action.equals(Events.SHOT)){	}

				Iterator<String> keys;
				Player p = Player.curPlayer();
				String name;
				keys = res.keys();
				while( keys.hasNext() ){
					name = keys.next();
					if( name.equals( Events.SHOT ) ){
						p.gotShot();
					}
				}
				*/
			} catch (JSONException e) {
				Log.e(TAG, e.getLocalizedMessage());
				mUIsend("Response not parceable",false);
				if( mLoginListener != null) //in case LoginListener is waiting for a response
					mLoginListener.onLoginError(response);
			}
			break;
			
		case RESPONSE_DENIED: // Wrong Request
		default:
			try {
				JSONObject res= new JSONObject(response);
				String event = res.getString("event");
				String error = res.getString("error");
				if(event.equals(Events.LOGIN)){
					if (mLoginListener != null)
						mLoginListener.onLoginError(error);
				}
				
			} catch (JSONException e) {
				Log.e(TAG, e.getLocalizedMessage());
				mUIsend("Response not parceable",false);
				if( mLoginListener != null) //in case LoginListener is waiting for a response
					mLoginListener.onLoginError(response);
			}
			break;
		
		}
	}


	/**Connect and send an event to the server, wait for it's
	 * response and send it to the mInternetReceived(response) method
	 * on a new thread
	 * @param query to the server
	 * @param event the message event, used to notify this event listener if there is an error
	 * @return false if there is no network available
	 */
	public boolean mInternetSend(final String query, final String event) {
		if( !ServerConn.isNetworkAvailable() )
			return false;
				
		new Thread(new Runnable() {
			public void run() {
				try {
					// Connect To Server
					HttpURLConnection con = ServerConn.Connect(ServerConn.metGET, Settings.getIns(null).getRegEventAdd(), query);
					// getResponse & process it
					SRVreceived = ServerConn.getResponse(con);
					mInternetReceived(SRVreceived,con.getResponseCode());
				} catch (Exception e1) {
					if (Main.D);
						e1.printStackTrace();
					try {
						//notify the server's response receiver that there was an error
						JSONObject error =new JSONObject();
						error.put("error", "" + e1.getMessage());
						error.put("event", event);
						mInternetReceived(error.toString(), 400);
					} catch (JSONException e) {	Log.e(TAG, " INNER TRY - " + e.getMessage());}
					
					
				}
			}
		}).start();
		return true;
	}
	
	/*************************	LISTENERS	***********************************/
	
	/**This method sets an specific onLoginListener
	 * to notify the servers response
	 * @param oll onLoginListener to callback
	 */
	public void setOnLoginListener(OnLoginListener oll){
		mLoginListener = oll;
	}
	
	
	/*************************	PREDEFINED SERVER MESSAGES	******************/
	
	/**This method sends a queryString to the server, informing that
	 * current player got shot by shooterID.
	 * @param shooterID the player who shoot current player
	 * @param died must be true if the shot killed the player
	 * @return true if there was a network connection available	 */
	public boolean sendGotShot(String shooterID, boolean died){
		String query = String.format(Locale.US,"p1=%s&p2=%d&hp=%d&event=%s", shooterID, Player.current().getID(), Player.current().getLife(), (died?Events.KILL:Events.SHOT));
		return mInternetSend(query, (died?Events.KILL:Events.SHOT));
	}
	
	
	/**This method sends a queryString to the server, informing that
	 * current player got shot by shooterID - IT SHOULD ONLY BE USED FOR TESTING -.
	 * @param shooterID the player who shoot current player
	 * @param died must be true if the shot killed the player
	 * @return true if there was a network connection available	 */
	public boolean sendIShot(String pShot, boolean died){
		String query = String.format(Locale.US,"p1=%d&p2=%s&hp=%d&event=%s", Player.current().getID(), pShot, Player.current().getLife(), (died?Events.KILL:Events.SHOT));
		return mInternetSend(query, (died?Events.KILL:Events.SHOT));
	}	
	
	/**Login into RealGaming server to fetch players data 
	 * @param usu to login
	 * @param pas for specified username
	 * @return true if there was a network connection available	 */
	public boolean sendLogIn(String usu, String pas){
		String query = String.format(Locale.US,"usu=%s&pw=%s&event=%s", usu, pas, Events.LOGIN);
		boolean ret = mInternetSend(query, Events.LOGIN);
		if( !ret && mLoginListener != null)
			mLoginListener.onLoginError(Main.instance.getString(R.string.not_connected));
		return ret;
	}
	
	/**Send recover notification to RealGaming server
	 * @return true if there was a network connection available */
	public boolean sendRecover(){
		String query =String.format("p1=%s&event=%s", Player.current().getID(), MessageManager.Events.RECOVER);
		return mInternetSend(query, Events.RECOVER);
	}
	
	/**Constant update query to obtain the latest game play information
	 * @return true if there was a network connection available	 */
	public boolean fetchUpdate(){
		String query = String.format(Locale.US,"p1=%d&event=%s", Player.current().getID(), Events.UPDATE);
		return mInternetSend(query, Events.UPDATE);
	}
	
	/**fetch user basic information (picture url, name, kills, deaths, etc)
	 * @return true if there was a network connection available
	 */
	public boolean fetchPlayerData(){
		String query = String.format(Locale.US,"p1=%d&event=%s", Player.current().getID(), Events.GETINFO);
		return mInternetSend(query, Events.GETINFO);
	}
	
	/*************************	PREDEFINED SERVER MESSAGES	******************/
	
	
	
}
