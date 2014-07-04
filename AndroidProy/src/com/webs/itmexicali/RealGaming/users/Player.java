package com.webs.itmexicali.RealGaming.users;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.webs.itmexicali.RealGaming.*;
import com.webs.itmexicali.RealGaming.comm.MessageManager;
import com.webs.itmexicali.RealGaming.frags.InfoUpdateListener;

import android.util.Log;

public class Player {

	//player info listener:
	ArrayList<InfoUpdateListener> infoListeners;
	
	//Player properties
	protected 	int		pID, pLife, pAmmo, pScore, pAssists, pKills, pDeaths;
	protected	double	pAccuracy;
	protected 	String	pName, pLastName, pUserName, pMail, pPicURL, pRank;
	protected	Date 	pDOB;
	
	// gun properties
	private 	int		gunDamage = 15, gunMagazine = 160;
	
	private final static String TAG = Main.TAG + " - Player";
	
	
	//JSON proccessing constants IDs
	private final static String ID = "ID", 				LOGIN = "login",
			LASTNAME = "lastname", 	 MAIL = "mail", 	PIC = "picture", 
			SCORE = "score", 		 KILLS = "kills",	DEATHS = "deaths",
			RANK = "rank",		 	 DOB = "birthday",	 NAME = "name",
			ACCURACY = "accuracy",	 ASSISTS = "assists";
	
	private static Player currentP;
	
	/** Get current Player logged in */
	public static final Player current(){
		if( currentP == null){
			String playerJson = Settings.getIns(null).getPref().getString(Settings.PLAYER_JSON, null);
			if( playerJson == null)
				currentP = new Player( 1, "default", 60);
			else
				currentP = new Player(playerJson);
		}
		return  currentP;
	}
	
	/**Once the player has logged in, we initialize the 
	 * player's  basic parameters.
	 * @param id the user id
	 * @param name the user name
	 * @param life startinf life %	 */
	private Player(int id, String name, int life){
		infoListeners = new ArrayList<InfoUpdateListener>();
		initPlayer(id,name,life);
	}
	
	/**Once the player has logged in, we initialize the 
	 * player's  basic parameters.
	 * @param id the user id
	 * @param name the user name
	 * @param life startinf life %	 */
	public void initPlayer(int id, String name, int life){
		pID 	= id;
		pName 	= name;
		pLife 	= life;
		pAmmo   = 100;
		notifyListener();
	}
	
	
	
	/** Once the player has logged in, we initialize the 
	 * player's  basic parameters.
	 * @param playerJSON the servers response with current player profile as STRING*/
	private Player(String playerJSON){
		infoListeners = new ArrayList<InfoUpdateListener>();
		try {
			initPlayer(new JSONObject(playerJSON));
		} catch (JSONException e) {	if(Main.D) e.printStackTrace();	} 
	}
	
	/** Once the player has logged in, we initialize the 
	 * player's  basic parameters.
	 * @param playerJSON the servers response with current player profile as JSON*/
	private Player(JSONObject playerJSON){
		infoListeners = new ArrayList<InfoUpdateListener>();
		initPlayer(playerJSON);
	}
	
		
	/** we initialize the player's  basic parameters from a json.
	 * @param playerJSON the servers response with current player profile*/
	public void initPlayer(JSONObject playerJSON){
		Log.v(TAG, "PlayerInit");
		
		Settings.getIns(null).getPref().edit().putString(Settings.PLAYER_JSON, playerJSON.toString()).commit();
		
		pLife	= 100;
		pAmmo	= gunMagazine;
		try {
			pID			= playerJSON.getInt(ID);
			pUserName	= playerJSON.getString(LOGIN);
			pName		= playerJSON.getString(NAME);
			pLastName	= playerJSON.getString(LASTNAME);
			pMail		= playerJSON.getString(MAIL);
			pPicURL		= playerJSON.getString(PIC);
			pScore		= playerJSON.getInt(SCORE);
			pKills		= playerJSON.getInt(KILLS);
			pDeaths		= playerJSON.getInt(DEATHS);
			pAssists	= playerJSON.getInt(ASSISTS);
			pRank		= playerJSON.getString(RANK);
			pAccuracy	= playerJSON.getDouble(ACCURACY);
			pDOB		= new Date(Date.parse(playerJSON.getString(DOB)));
		} catch (Exception e) {	if(Main.D)e.printStackTrace(); }
		if(Main.D){
			Log.v(TAG, "PlayerInitComplete");
		}
		notifyListener();
	}	
	
	/***************************** Members getters  *************************/
	/** @return the pID */
	public int getID() {
		return pID;
	}
	/** @return the pLive */
	public int getLife() {
		return pLife;
	}
	/**@return the pAmmo */
	public int getAmmo() {
		return pAmmo;
	}
	/** @return the pScore */
	public int getScore() {
		return pScore;
	}
	/** @return the pRank */
	public String getRank() {
		return pRank;
	}
	/** @return the pAssistances  */
	public int getAssistances() {
		return pAssists;
	}
	/** @return the pKills */
	public int getKills() {
		return pKills;
	}
	/** @return the deaths */
	public final int getDeaths() {
		return pDeaths;
	}
	/** @return the first name	 */
	public final String getName() {
		return pName;
	}
	/** @return the username */
	public final String getUsername() {
		return pUserName;
	}
	/** @return the email address	 */
	public final String getMail() {
		return pMail;
	}
	/** @return the picture URL */
	public final String getPicURL() {
		return pPicURL;
	}
	
	
	/************************* InfoUpdateListener Implementations  *************************/
	
	/** add a new listener that will be notified when info has been updated */
	public void addOnInfoUpdateListener(InfoUpdateListener iul){
		if(Main.D)
			Log.i(TAG,"adding infoListeners: ");
		if (!infoListeners.contains(iul))
			infoListeners.add(iul);
	}
	
	/** Notify the listeners so it will refresh it's UI */
	public void notifyListener(){
		if ( infoListeners.size() > 0)
			if(Main.D)
				Log.i(TAG,"notifying infoListeners, total: "+ infoListeners.size());
		for(int i =0 ; i < infoListeners.size() ; i++){
			infoListeners.get(i).refreshUI_newThread();
		}
	}
	/** remove a listener, if it's known that it won't be used any more */
	public void removeOnInfoUpdateListener(InfoUpdateListener iul){
		if(Main.D)
			Log.i(TAG,"removing infoListeners: ");
		infoListeners.remove(iul);
	}

	
	/***************************** Player actions ******************************************/

	/** Add a Death, if the life is lower or equals to 0	 
	 * @param p2ID the player who shot current player*/
	public void addDeath(String p2ID){
		pDeaths++;
		MessageManager.getIns().sendGotShot(p2ID, true);
		if(Main.D)
			Log.i(TAG, pName+" got killed by: "+p2ID);
		notifyListener();
	}
	
	/** - FOR TEST ONLY - Add a Kill	 */
	public void addKill(String p2){
		pKills++;
		pScore +=  25;
		MessageManager.getIns().sendIShot(p2, true);
		if(Main.D)
			Log.i(TAG, pName+" got killed by: "+p2);
		notifyListener();
	}
	
	/** Recover Life and Ammo	 */
	public void recover(){
		pLife = 100;
		pAmmo = gunMagazine;
		MessageManager.getIns().sendRecover();
		if(Main.D)
			Log.i(TAG, pName+" recovered");
		notifyListener();
	}
	
	/** Reload the players magazine */
	public void reload(){
		pAmmo = gunMagazine;
		notifyListener();
	}
	
	/**Proccess the shot received, send to server if possible
	 * @param p2ID the player who shoot me's ID */
	public void gotShot(String p2ID){
		pLife -= gunDamage;
		if(isDead()){
			if(!isRecovering()){//notify kill & recover
				Recover.startRecover();
				addDeath(p2ID);
				if(Main.D)
					Log.i(TAG, pName+" got killed by: "+p2ID);
			}
			//if we are dead but recovering, then do nothing
			if(Main.D)
				Log.e(TAG, pName+" got killed by: "+p2ID+" but im already fucking DEAD!!!!!!");
			return;
		}
		else{//notify shot			
			MessageManager.getIns().sendGotShot(p2ID, false);
			if(Main.D)
				Log.i(TAG, pName+" got Shot by: "+p2ID);
		}
		notifyListener();
	}
	
	/** reduce the ammo amount by one */
	public void shoot(){
		if (pAmmo > 0)
			pAmmo --;
		else
			MessageManager.getIns().mUIsend(R.string.reload,false);
		notifyListener();
	}
	/** check if player is dead */
	public boolean isDead(){
		if( pLife < 1 ){
			pLife =0;
			return true;
		}
		return false;
		
	}
	
	/** check if the player is currently recovering */
	public boolean isRecovering(){
		return Recover.recovering;
	}
	
	
	/*****************************************************************************************
	 * This class implements a thread that will be used to recover the current
	 * userm 5 secs after his dead
	 * 
	 * @author michael.duarte	 */
	private static class Recover implements Runnable{
		
		static boolean recovering = false;
		Player p;
		int timeout = 5000;//5 secs
		
		/** start recovering current player, only once at a time */
		public static synchronized boolean startRecover(){
			if( !recovering ){
				recovering = true;
				Thread r=new Thread(new Recover());
				r.start();
				return true;
			}
			return false;
		}
		
		public void run(){
			p = Player.current();
			try {
				if(Main.D)
					Log.i(Player.TAG, p.pName+" is recovering ...");
				Thread.sleep(timeout);
			} catch (InterruptedException e) {	e.printStackTrace();	}
			p.recover();
			recovering = false;
		}
	}
	
	/**************************************************************************************/
}