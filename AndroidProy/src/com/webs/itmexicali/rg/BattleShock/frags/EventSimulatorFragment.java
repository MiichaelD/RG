/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webs.itmexicali.rg.BattleShock.frags;

import com.webs.itmexicali.rg.BattleShock.R;
import com.webs.itmexicali.rg.BattleShock.Main;
import com.webs.itmexicali.rg.BattleShock.comm.MessageManager;
import com.webs.itmexicali.rg.BattleShock.users.Player;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * A fragment representing a Event Simulator.
 *
 * <p>This class is used by the {@link SecretReceiver} and {@link
 * ScreenSlide} samples.</p>
 */
public class EventSimulatorFragment extends Fragment {
	
    /** The argument key for the page number this fragment represents.    */
    public static final String ARG_PAGE = "page";

    /** The view from this fragment    */
    public View mView ;
    
    /** This fragment components   */
    private EditText shooterID_et, amountXP_et;
    
    /** The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.    */
    private int mPageNumber;

    /** Factory method for this fragment class. Constructs a new fragment for the given page number.     */
    public static EventSimulatorFragment create(int pageNumber) {
    	EventSimulatorFragment fragment = new EventSimulatorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( getArguments() != null )
        	mPageNumber = getArguments().getInt(ARG_PAGE);
        
        if(Main.D)Log.d(Main.TAG+"-EventSimulator",  "++OnCreate++ page:"+mPageNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
    	if(Main.D)Log.d(Main.TAG+"-EventSimulator",  "new PagerFragment on Create");
    	mView = inflater.inflate(R.layout.event_simulator, container, false);
    	
    	shooterID_et = (EditText)mView.findViewById(R.id.shooterID_et);
    	amountXP_et = (EditText)mView.findViewById(R.id.xp_amount_et);
    	
    	Button btn = (Button)mView.findViewById(R.id.ok1_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			InputMethodManager imm = (InputMethodManager) ScreenSlide.fa.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    			String txt =shooterID_et.getText().toString(); 
    			if( !txt.equals("")){
    				MessageManager.getIns().mBluetoothReceived(txt);
    			}
    			else{
    				MessageManager.getIns().mUIsend("Please insert shooter ID",false);
    			}
    		}
    	});
    	
    	btn = (Button)mView.findViewById(R.id.ok2_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			String txt =amountXP_et.getText().toString(); 
    			if( !txt.equals("")){
    				MessageManager.getIns().mUIsend("Score not implemented",false);
    			}
    			else{
    				MessageManager.getIns().mUIsend("Please insert XP amount to add",false);
    			}
    		}
    	});
    	
    	btn = (Button)mView.findViewById(R.id.shoot_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			Player.current().shoot();
    		}
    	});
    	
    	btn = (Button)mView.findViewById(R.id.reload_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			Player.current().reload();
    		}
    	});
    	
    	btn = (Button)mView.findViewById(R.id.kill_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			String txt =shooterID_et.getText().toString();
    			Player.current().addKill(txt.equals("")?"EventSimulator":txt);
    		}
    	});
    	
    	btn = (Button)mView.findViewById(R.id.suicide_btn);
    	btn.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			String txt =shooterID_et.getText().toString();
    			Player.current().addDeath(txt.equals("")?"EventSimulator":txt);
    		}
    	});
        return mView;
    }

    /** Returns the page number represented by this fragment object.  */
    public int getPageNumber() {
        return mPageNumber;
    }
}
