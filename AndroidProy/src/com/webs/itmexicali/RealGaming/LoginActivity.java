package com.webs.itmexicali.RealGaming;

import org.json.JSONException;
import org.json.JSONObject;

import com.webs.itmexicali.RealGaming.R;
import com.webs.itmexicali.RealGaming.comm.MessageManager;
import com.webs.itmexicali.RealGaming.comm.MessageManager.Events;
import com.webs.itmexicali.RealGaming.users.Player;
import com.webs.itmexicali.RealGaming.users.Team;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * This is the main Activity that displays the current chat session.
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends AccountAuthenticatorActivity implements OnLoginListener {
	
    // Debugging
    public static final String TAG = "RealGaming-LogIN";
    public static final Boolean D = Main.D;
    
    //Login Layout InnerViews
    TextView user_tv, pass_tv;
    Button 	login_btn;
    ProgressBar	progbar;
    
    String mUsername, mPassword;
    public final String credential_type = "com.webs.itmexicali.RealGaming";
        
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        MessageManager.getIns().setOnLoginListener(this);
        
        user_tv = (TextView) findViewById(R.id.username);
        pass_tv = (TextView) findViewById(R.id.password);
        pass_tv.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId,	KeyEvent event) {
				tryLogin();				
				return false;
			}
        	
        });
        
        progbar = (ProgressBar) findViewById(R.id.progressBar1);
        
        //give functionality to second button
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
           	 		if(D)
           	 			Log.i(TAG,"Change - CANCEL");
           	 		tryLogin();
                }
            });
        
    }    
    
	
	private void buttonVisibility(final boolean visible){
		Main.instance.runOnUiThread(new Runnable() {
			public void run() {
				if(visible){
					login_btn.setVisibility(View.VISIBLE);
		    		progbar.setVisibility(View.GONE);
				}
				else{
					login_btn.setVisibility(View.GONE);
		    		progbar.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
	private void tryLogin(){
		if(login_btn.getVisibility() == View.VISIBLE)
			try {
	        	mUsername = user_tv.getText().toString();
	        	mPassword = pass_tv.getText().toString();
	        	if(mUsername.equals("")){
	        		MessageManager.getIns().mUIsend(true,R.string.please_input,R.string.username);
	        	}else if( mPassword.equals("")){
	        		MessageManager.getIns().mUIsend(true,R.string.please_input,R.string.password);
	        	}
	        	else{
	        		buttonVisibility(false);
	        		MessageManager.getIns().sendLogIn(mUsername, mPassword);
	        	}
			} catch (Throwable e) {	if(D) 		Log.e(TAG,e.getMessage()+" - Click");}
	}

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");        
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");
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
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

	@Override
	public void onBackPressed() {
		if(D) Log.i(TAG,"***OnBackPressed****");
		//finish parent
		//Main.instance.finishFromChild(this);
		Intent returnIntent = new Intent();
		setResult(RESULT_CANCELED, returnIntent);
		//finish this activity
		finish();
	}


	@Override
	public void onLoginSuccess(String json) {
		MessageManager.getIns().mUIsend(R.string.login_success, false);
		buttonVisibility(true);
		/// PROCESS JSON
		
		JSONObject res;
		try {
			res = new JSONObject(json);
			Player.current().initPlayer( res.getJSONObject("player") );
			//Team.current().initTeam( res.getJSONObject("team") );
		} catch (JSONException e) {
			if(Main.D)
				Log.e(TAG, "tronix at onLoginSuccess:");
			e.printStackTrace();
		}
		
		
		
		/////
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		//finish this activity
		finish();
	}


	@Override
	public void onLoginError(String error) {
		if (error.startsWith("http://") || error.startsWith("failed to connect"))
			MessageManager.getIns().mUIsend(R.string.server_down, false);
		else
			MessageManager.getIns().mUIsend(error, false);
		
		if(Main.D)
			Log.e(TAG,"Login Error - "+error);
		
		
		Account[] acs = AccountManager.get(this).getAccounts();
        for (Account a: acs)
			Log.d(TAG,a.name+" - "+a.type+" * ");
        
        Log.v(TAG,"********* Adding new account **************");
        //final Account account = new Account(mUsername, credential_type);
        //AccountManager.get(this).addAccountExplicitly(account, mPassword, null);
        buttonVisibility(true);
	}

}

