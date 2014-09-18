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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see BaseFragment
 */
public class ScreenSlide extends FragmentActivity {
    
	/** The number of pages (wizard steps) to show in this demo.    */
	private final String TAG = Main.TAG+"-ScreenSlide";
    private static int NUM_PAGES = 2;
    
    private Fragment fragList[];
    
    public static int currentPage = 0; 
    
    public static FragmentActivity fa;

    /**
     * The pager widget, which handles animation and allows swiping
     * horizontally to access previous and next wizard steps.   */
    private ViewPager mPager;

    /** The pager adapter, which provides the pages to the view pager widget.   */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fa = this;
        setContentView(R.layout.activity_screen_slide);
        
        //Apply sensorLandscape for screnOrientation to devices running API level 9+
        if (Build.VERSION.SDK_INT >= 9) {
            setRequestedOrientation(6);
        }
        
        if(Main.D)
        	NUM_PAGES=3;
        
        fragList=new Fragment[NUM_PAGES];
        
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	
        	public void onPageScrollStateChanged(int state){
        		switch(state){
        		case ViewPager.SCROLL_STATE_IDLE:
        			/*
        			if(Main.D)
        				Log.d(TAG,"onPageScrollChanged( idle ); - "+state);
        				
        			//when we arrive to selected page, we start it's thread - for example the 1st screen
                	if(fragList[currentPage] != null && fragList[currentPage] instanceof BaseFragment){
                		((BaseFragment)fragList[currentPage]).p1.startThread();
                	} 	*/
        			break;
        			
        		case ViewPager.SCROLL_STATE_DRAGGING:
                	if(fragList[currentPage] instanceof EventSimulatorFragment){
            			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(((EventSimulatorFragment)fragList[currentPage]).mView.getWindowToken(), 0);
        			}
        			/*
        			if(Main.D)
        				Log.d(TAG,"onPageScrollChanged( dragging ); - "+state);
        				
        			// while changing screen stop all running threads if they exist
            		for(int i = 0; i < NUM_PAGES; i++){
	                	if (fragList[i] != null && fragList[i] instanceof BaseFragment){
	                		((BaseFragment)fragList[i]).mView.stopThread();
	                	}
                	}*/
        			break;
        			
        		case ViewPager.SCROLL_STATE_SETTLING:
        			/*if(Main.D)		Log.d(TAG,"onPageScrollChanged( settling ); - "+state);     			*/
        			break;
        		}
        	}
        	
        	@Override
        	public void  onPageScrolled (int position, float positionOffset, int posOffPixels){
        		/*Log.v("SlideActivity",String.format("onPageScrolled( pos %d, posOffset %f, posOffsetPixels %d); - ",position,positionOffset,posOffPixels));
        		if(position == currentPage && posOffPixels > 10){
        			//whe are moving to next page
        			Log.v("SlideActivity","we are in ");
        			if(fragList[currentPage+1] != null)
                		fragList[currentPage+1].p1.startThread();
        		}*/
        	}
        	        	
            @Override
            public void onPageSelected(int position) {
            	currentPage = position;
            }
        });
    }
   

    /** A simple pager adapter that represents 5 {@link BaseFragment} objects, in sequence.   */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	if(Main.D)
        		Log.d(TAG,"Fragment GetItem("+position+");");
        	if(fragList[position] == null){
        		switch(position){
        		case 0:
        		case 1:
        			fragList[position] = BaseFragment.create(position);
        			break;
        		case 2:
        			fragList[position] = EventSimulatorFragment.create(position);
        			break;        		
        		}
        	}
            return fragList[position];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
