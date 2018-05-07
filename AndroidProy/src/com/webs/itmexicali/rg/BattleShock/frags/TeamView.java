package com.webs.itmexicali.rg.BattleShock.frags;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.webs.itmexicali.rg.BattleShock.R;
import com.webs.itmexicali.rg.BattleShock.BitmapLoader;
import com.webs.itmexicali.rg.BattleShock.Main;
import com.webs.itmexicali.rg.BattleShock.users.Player;
import com.webs.itmexicali.rg.BattleShock.users.Team;

public class TeamView extends BaseFragView {
	
	private final String TAG = Main.TAG+"-TeamView";

	/******************************	Constructors ******************************/
	public TeamView(Context context) {
		super(context);
		Log.i(TAG,"constructor 1 params");
		Team.current().setOnInfoUpdateListener(this);
	}
	
	public TeamView(Context context, AttributeSet attrs, int defStyle){
        super( context , attrs , defStyle );
        Log.i(TAG,"constructor 3 params");
        Team.current().setOnInfoUpdateListener(this);
    }

    public TeamView ( Context context , AttributeSet attrs ){
        super( context , attrs );
        Log.i(TAG,"constructor 2 params");
        Team.current().setOnInfoUpdateListener(this);
    }
    
    /******************************* SURFACE HOLDER *****************************/
	
   	@Override
   	public void surfaceCreated(SurfaceHolder holder) {
   		Log.d(TAG,"SurfaceCreated "+window);
   		super.surfaceCreated(holder);
   		Player.current().addOnInfoUpdateListener(this);
   	}

   	
   	@Override
   	public void surfaceDestroyed(SurfaceHolder holder) {
   		Log.v(TAG, "+surfaceDestroyed "+window);
   		super.surfaceDestroyed(holder);
   		Player.current().removeOnInfoUpdateListener(this);
   	}
    
    
	/***************************************************************************/
   	@Override
	protected void reloadByResize() {
 		mRectFs = new RectF[2];
 		mRectFs[0] = new RectF(2.0f*w,  2.0f*w, 30.0f*w, 35.0f*h );
 		mRectFs[1] = new RectF(32.0f*w, 2.0f*w, 60.0f*w, 35.0f*h );
 		
 		mBitmaps = new Bitmap[2];
		// This task must be on a separated thread because it takes time fetching the img from server
		new Thread(new Runnable(){
			@Override
			public void run(){
				mBitmaps[0]= BitmapLoader.resizeImage(Main.instance,Team.current().getLogo(),26*w,35*h-4*w);
				mBitmaps[1]= BitmapLoader.resizeImage(Main.instance,R.drawable.realgamingh,26*w,35*h-4*w);
				refreshUI();
			}
		}).start();
	}
   	
   	
	@Override
	/** Init the paints to be used on canvas */
	public void initPaints() {
		mPaints = new Paint[4];
		mPaints[0] = new Paint();
		mPaints[0].setColor(Color.WHITE);
		mPaints[0].setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/KellySlab-Regular.ttf"));
		mPaints[0].setTextSize(3*textSize/2);
		mPaints[0].setTextAlign(Align.CENTER);
		mPaints[0].setAntiAlias(true);
		
		mPaints[1] = new Paint();
		mPaints[1].setColor(Color.BLUE);
		mPaints[1].setStyle(Paint.Style.FILL);
		mPaints[1].setAlpha(75);
		mPaints[1].setAntiAlias(true);
		
		mPaints[2] = new Paint();
		mPaints[2].setColor(Color.RED);
		mPaints[2].setAlpha(145);
		mPaints[2].setStyle(Paint.Style.FILL);
		mPaints[2].setAntiAlias(true);
		
		mPaints[3] = new Paint();
		mPaints[3].setColor(Color.WHITE);
		mPaints[3].setTextAlign(Align.LEFT);
		mPaints[3].setAntiAlias(true);
		mPaints[3].setTextSize(textSize);
		
		bgColor= Color.DKGRAY;
	}

  /** This is what is going to be shown on the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas) */
	public void onDraw(Canvas canvas) {
		try {
			//Background
			canvas.drawColor(bgColor);
			
			//Color Div
			canvas.drawRoundRect(mRectFs[0], 25.0f, 20.0f, mPaints[1]);
			
			if ( mBitmaps[0] != null ){
				canvas.drawBitmap(mBitmaps[0], w*3, 3*w, null);
			}
			if ( mBitmaps[1] != null ){
				canvas.drawBitmap(mBitmaps[1], w*33, 3*w, null);
			}
			canvas.drawRoundRect(mRectFs[1], 25.0f, 20.0f, mPaints[2]);
						
			String tName = "Team1";
			if (Team.current().getName() != null)
				tName = Team.current().getName();
			
			canvas.drawText(tName, 16*w, 9*h, mPaints[0]);
			canvas.drawText("Team2", 46*w, 9*h, mPaints[0]);

			mPaints[3].setTextAlign(Align.LEFT);
			//subtitles team1
			canvas.drawText(mContext.getString(R.string.kills),	5*w, 16*h,							  mPaints[3]);
			canvas.drawText(mContext.getString(R.string.deaths),	5*w, 16*h+mPaints[3].getTextSize(),	  mPaints[3]);
			canvas.drawText(mContext.getString(R.string.ratio),	5*w, 16*h+2*mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(mContext.getString(R.string.score),	5*w, 16*h+mPaints[3].getTextSize()*3, mPaints[3]);
			//subtitles team2
			canvas.drawText(mContext.getString(R.string.kills), 35*w, 16*h, mPaints[3]);
			canvas.drawText(mContext.getString(R.string.deaths), 35*w, 16*h+mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(mContext.getString(R.string.ratio), 35*w, 16*h+2*mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(mContext.getString(R.string.score), 35*w, 16*h+mPaints[3].getTextSize()*3, mPaints[3]);
			
			//subtitle values team1
			Player p = Player.current();
			mPaints[3].setTextAlign(Align.RIGHT);
			canvas.drawText(String.format("%,d",p.getKills()),	27*w, 16*h, mPaints[3]);
			canvas.drawText(String.format("%,d",p.getDeaths()),	27*w, 16*h+mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(String.format("%.2f",((double)p.getKills()/Math.max(p.getDeaths(),1))),
																27*w, 16*h+2*mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(String.format("%,d",p.getScore()),	27*w, 16*h+mPaints[3].getTextSize()*3, mPaints[3]);
			
			//subtitles values team2
			canvas.drawText(String.format("%,d",p.getKills()),	57*w, 16*h, mPaints[3]);
			canvas.drawText(String.format("%,d",p.getDeaths()), 57*w, 16*h+mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(String.format("%.2f",((double)p.getKills()/Math.max(p.getDeaths(),1))),
																57*w, 16*h+2*mPaints[3].getTextSize(), mPaints[3]);
			canvas.drawText(String.format("%,d",p.getScore()),	57*w, 16*h+mPaints[3].getTextSize()*3, mPaints[3]);
			
		} catch (Exception e) {
			Log.e(TAG, "onDraw - TeamView ");
			e.printStackTrace();
		}
	}
	
}
