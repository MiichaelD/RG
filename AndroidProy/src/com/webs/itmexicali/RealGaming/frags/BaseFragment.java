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

package com.webs.itmexicali.RealGaming.frags;

import com.webs.itmexicali.RealGaming.Main;
import com.webs.itmexicali.RealGaming.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/** A fragment showing the UI depending on the page it is. This fragment
 * contains 2 different views, one is a normal HUD and the other one is a 
 * Team View */
public class BaseFragment extends Fragment {
	
	private static final String TAG = Main.TAG+"-BaseFragment";
	
    /** The argument key for the page number this fragment represents.    */
    public static final String ARG_PAGE = "page";

    /** The view from this fragment   */
    public BaseFragView mView ;
    
    /** The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.    */
    private int mPageNumber;

    /** Factory method for this fragment class. Constructs a new fragment for the given page number.   */
    public static BaseFragment create(int pageNumber) {
        BaseFragment fragment = new BaseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public BaseFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        Log.d(TAG,"++OnCreate++ page:"+mPageNumber);
    }

    
    /** Inflate the layout containing the UI for the specified page number*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	switch(mPageNumber){
    	case 0:
    		mView = (BaseFragView) inflater.inflate(R.layout.fragment_hud_view, container, false);
    		break;
    	case 1:
    		mView = (BaseFragView) inflater.inflate(R.layout.fragment_team_view, container, false);
    		break;
    	}
        mView.window = mPageNumber;
        return mView;
    }

    /** Returns the page number represented by this fragment object.   */
    public int getPageNumber() {
    	Log.d(TAG,"getPageNumber "+ mPageNumber);
        return mPageNumber;
    }
}
