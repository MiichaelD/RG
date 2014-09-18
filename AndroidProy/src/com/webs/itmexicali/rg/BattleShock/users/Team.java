package com.webs.itmexicali.rg.BattleShock.users;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.webs.itmexicali.rg.BattleShock.Main;
import com.webs.itmexicali.rg.BattleShock.Settings;
import com.webs.itmexicali.rg.BattleShock.frags.InfoUpdateListener;

public class Team {

	private static Team currentT;
	private String tName=null, tLogo, tRank, tCapt;
	private int tWins, tLoses, tID;
	
	//player info listener:
	InfoUpdateListener infoListener;
	
	//JSON proccessing constants IDs
	@SuppressWarnings("unused")
	private final static String 	ID = "ID", 				NAME = "name",
				LOGO = "logo",		DOB = "date_created",	WINS = "wins",
				CAPT = "captain",	LOSES = "looses",		RANK = "rank";
	
	private final static String TAG = Main.TAG + " - Team";
	
	/** Get current team */
	public static final Team current(){
		if( currentT == null){
			String teamJson = Settings.getIns(null).getPref().getString(Settings.TEAM_JSON, null);
			if( teamJson == null)
				currentT = new Team( 1, "default");
			else
				currentT = new Team(teamJson);
		}
		return  currentT;
	}
	
	/** init the team ID and name 
	 * @param id  
	 * @param name */
	public Team(int id, String name){
		initTeam(id,name);
	}

	/** init the team ID and name 
	 * @param id 
	 * @param name*/
	public void initTeam(int id, String name){
		tID = id;		tName = name;		
		notifyListener();
	}
	
	
	/** initialize the team's  basic parameters.
	 * @param teamJSON the servers response with current player profile as STRING*/
	private Team(String teamJSON){
		try {
			initTeam(new JSONObject(teamJSON));
		} catch (JSONException e) {	if(Main.D) e.printStackTrace();	} 
	}
	
	/** initialize the team's  basic parameters.
	 * @param teamJSON the servers response with current player profile as JSON*/
	@SuppressWarnings("unused")
	private Team(JSONObject teamJSON){
		initTeam(teamJSON);
	}
		
			
	/** we initialize the team's basic parameters from a json.
	 * @param playerJSON the servers response with current player profile*/
	public void initTeam(JSONObject teamJSON){
		Log.v(TAG, "TeamInit");
		
		Settings.getIns(null).getPref().edit().putString(Settings.TEAM_JSON, teamJSON.toString()).commit();
		
		try {
			tID		= teamJSON.getInt(ID);
			tName	= teamJSON.getString(NAME);
			tLogo	= teamJSON.getString(LOGO);
			tCapt	= teamJSON.getString(CAPT);
			tRank	= teamJSON.getString(RANK);
			tWins	= teamJSON.getInt(WINS);
			tLoses	= teamJSON.getInt(LOSES);
		} catch (JSONException e) {	if(Main.D)e.printStackTrace(); }
		if(Main.D){
			Log.v(TAG, "TeamInitComplete");
		}
		notifyListener();
	}
	
	
	/***************************** Members getters  *************************/
	/**@return the tName */
	public final String getName() {
		return tName;
	}	
	/** @return the tID */
	public final int getID() {
		return tID;
	}
	/** @return the Captain's name */
	public final String getCaptain(){
		return tCapt;
	}
	/** @return the Rank */
	public final String getRank(){
		return tRank;
	}
	/** @return the matches this team has won */
	public final int getWins(){
		return tWins;
	}
	/** @return the matches this team has Lost */
	public final int getLost(){
		return tLoses;
	}
	/** @return Logo's URL */
	public final String getLogo(){
		return tLogo;
	}
	/************************* InfoUpdateListener Implementations  *************************/
	
	/** add a listener that will be notified when info has been updated */
	public void setOnInfoUpdateListener(InfoUpdateListener iul){
		infoListener = iul;
	}
	
	/** Notify the listener so it will refresh it's UI */
	public void notifyListener(){
		if(infoListener != null)
			infoListener.refreshUI_newThread();
	}	
	
	
}
