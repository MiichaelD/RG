package com.webs.itmexicali.rg.BattleShock.comm;

import com.webs.itmexicali.rg.BattleShock.Main;
import com.webs.itmexicali.rg.BattleShock.Settings;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ServerConn extends server.ServerCom{

private static ServerConn instance;
	
	private ServerConn(){
		setUrl(Settings.getIns(Main.instance).getRegEventAdd());
	}
	
	public void setUrl(String url){
		m_mainUrl = url;
	}
	
	public static ServerConn shared(){
		if(instance == null){
			instance = new ServerConn();
		}
		return instance;
	}

	@Override
	public boolean isNetworkAvailable() {
		boolean ret;
		ConnectivityManager connectivityManager = (ConnectivityManager) Main.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		ret = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
		if(Main.D & !ret){
			Log.e(Main.TAG+"-srvConn","there is no network Available ");	
		}
		return ret;
	}
}