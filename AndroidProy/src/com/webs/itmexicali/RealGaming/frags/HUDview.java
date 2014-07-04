package com.webs.itmexicali.RealGaming.frags;

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

import com.webs.itmexicali.RealGaming.BitmapLoader;
import com.webs.itmexicali.RealGaming.Main;
import com.webs.itmexicali.RealGaming.R;
import com.webs.itmexicali.RealGaming.frags.BaseFragView;
import com.webs.itmexicali.RealGaming.users.Player;

public class HUDview extends BaseFragView {
	
	private final String TAG = Main.TAG+"-HUDview";
	
	/******************************	Constructors ******************************/
	public HUDview(Context context) {
		super(context);
		Log.i(TAG,"constructor 1 params");
	}
	
	public HUDview(Context context, AttributeSet attrs, int defStyle){
        super( context , attrs , defStyle );
        Log.i(TAG,"constructor 3 params");
    }

    public HUDview ( Context context , AttributeSet attrs ){
        super( context , attrs );
        Log.i(TAG,"constructor 2 params");
        
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
		mRectFs[0] = new RectF(2.0f*w, 2.0f*w, 40.0f*w, 20.0f*h);
		
		mRectFs[1] = new RectF(44.2f*w, 2.2f*w, 57.8f*w, 15.8f*w);
		
		mBitmaps = new Bitmap[1];
		//this task must be on a separated thread because of it's lag while fetching the img from server
				new Thread(new Runnable(){
					@Override
					public void run(){
						float val = 20.0f*h - 4.0f*w;
						mBitmaps[0] = BitmapLoader.resizeImage(Main.instance,Player.current().getPicURL(), val, val);
						refreshUI();
					}
				}).start();
	}
	
	@Override
	/** Init the paints to be used on canvas */
	public void initPaints() {
		bgColor= Color.DKGRAY;
		
		mPaints = new Paint[5];
		mPaints[0] = new Paint();
		mPaints[0].setColor(Color.WHITE);
		mPaints[0].setTextAlign(Align.LEFT);
		mPaints[0].setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/KellySlab-Regular.ttf"));
		mPaints[0].setTextSize(textSize*3/2);
		mPaints[0].setAntiAlias(true);
		
		mPaints[1] = new Paint();
		mPaints[1].setARGB(200, 100, 180, 180);
		mPaints[1].setStyle(Paint.Style.FILL);
		mPaints[1].setAntiAlias(true);
		
		mPaints[2] = new Paint();
		mPaints[2].setColor(Color.BLACK);
		mPaints[2].setTextSize(textSize);
		mPaints[2].setAntiAlias(true);
		
		mPaints[3] = new Paint();
		mPaints[3].setColor(bgColor);
		mPaints[3].setStyle(Paint.Style.FILL);
		mPaints[3].setAntiAlias(true);
		
		mPaints[4] = new Paint();
		mPaints[4].setColor(Color.RED);
		mPaints[4].setStyle(Paint.Style.FILL);
		mPaints[4].setAntiAlias(true);
	}
    
    /** This is what is going to be shown on the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas) */
	public void onDraw(Canvas canvas) {
		try {
			int life = Player.current().getLife();
			
			//Background
			canvas.drawColor(bgColor);
			
			//Color Div
			canvas.drawRoundRect(mRectFs[0], 25.0f, 20.0f, mPaints[1]);
			
			//Picture  - 2.0f*w, 2.0f*w, 40.0f*w, 20.0f*h
			if ( mBitmaps[0] != null )
				canvas.drawBitmap(mBitmaps[0], 3.0f*w, 3.0f*w, null);
			
			
			if ( life  < 30 ){
				mPaints[4].setColor(Color.RED);
			}else if (life < 70){
				mPaints[4].setColor(Color.YELLOW);
			}else{
				mPaints[4].setColor(Color.GREEN);
			}
			
			mPaints[4].setAlpha(life * 2 + 55);
			
			//Life graphic
			//(44.0f*w, 2.0f*w, 58.0f*w, 16.5f*w);
			canvas.drawCircle(51.0f*w, 9.0f*w, 7.0f*w, mPaints[4]);//life
			canvas.drawCircle(51.0f*w, 9.0f*w, 5.0f*w, mPaints[3]);//center point
			canvas.drawArc(mRectFs[1], -90, (int)((100 - life) * 3.6), true, mPaints[3]);//moving arc
			
			//Subtitles
			canvas.drawText(mContext.getString(R.string.rank), 16.0f*w, 12.0f*h, mPaints[2]);
			canvas.drawText(mContext.getString(R.string.life), 48.8f*w, 14.5f*h, mPaints[2]);
			canvas.drawText(mContext.getString(R.string.ammo), 40.0f*w, 25.0f*h, mPaints[2]);
			
			//Values
			//canvas.drawText(Player.current().getUsername(), 23 * w, 7 * h, mPaints[0]);
			canvas.drawText(Integer.toString(Player.current().getLife())+"%", 47.5f*w, 11.5f*h, mPaints[0]);
			canvas.drawText(Player.current().getAmmo()+"/160", 45*w, 30*h, mPaints[0]);
			if (Player.current().getUsername() != null ){
				canvas.drawText(Player.current().getUsername(), 17 * w, 7 * h, mPaints[0]);
				canvas.drawText(Player.current().getRank(), 19*w, 16*h, mPaints[0]);
			}
			
			
		} catch (Exception e) {
			Log.e(TAG, "onDraw(canvas)");
			e.printStackTrace();
		}
	}
	
}
