package com.webs.itmexicali.RealGaming.frags;

import com.webs.itmexicali.RealGaming.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

@SuppressLint("WrongCall")
public abstract class BaseFragView extends SurfaceView implements Callback, Runnable, InfoUpdateListener{
	
	protected String TAG = Main.TAG + "-BaseFragView";
	
	public 	  boolean run, selected, surfaceCreated=false;
	protected SurfaceHolder sh;
	
	//Paints to be used to Draw text and shapes
	protected Paint 	mPaints[];
	protected Bitmap	mBitmaps[];
	protected Rect		mRects[], screen;
	protected RectF		mRectFs[];
	private Thread		tDraw;
	
	//this screen (window) number
	public 	  int window;
	
	protected int bgColor=Color.WHITE;
	
	protected float w, h, textSize, ratio;
	
	//canvas to be drawn on
	protected Canvas canvas;
	
	//Context of the instance that instantiated this class
	protected Context  mContext;

	
	/********************************************CONSTRUCTORS*****************************/
	public BaseFragView(Context context) {
		super(context);
		mContext = context;
		initHolder(context);
	}
	
	public BaseFragView(Context context, AttributeSet attrs, int defStyle){
        super( context , attrs , defStyle );
        mContext = context;
        initHolder(context);
    }

    public BaseFragView ( Context context , AttributeSet attrs ){
        super( context , attrs );
        mContext = context;
        initHolder(context);
    }
    /**************************************************************************************/
	
    
    /** Init this SurfaceView's Holder    */
	public final void initHolder(Context context){
		sh = getHolder();
		//sh.setFormat(PixelFormat.TRANSLUCENT);
		sh.addCallback(this);
	}

	/** Init the paints to be used on canvas */
	public abstract void initPaints();
	
	/** This is what is going to be shown on the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)	 */
	public abstract void onDraw(Canvas canvas);
	

	/** This method will be called when the surface has been resized, so all
	 * screen width and height dependents must be reloaded - DO NOT INCLUDE initPaints()*/
	protected abstract void reloadByResize();
	
	
	/** resize window parameters each time the screen changes from resolution */
	protected final void resize(){
		w = getWidth() / 60;		h = getHeight() / 40;
		ratio = getWidth() / getHeight();
		textSize = (int)3*h;
		screen = new Rect(0, 0, getWidth(), getHeight());
		
		initPaints();
		reloadByResize();
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//Log.d(TAG,"SurfaceCreated "+window);
		canvas = null;
		surfaceCreated=true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		//Log.v(TAG, "SurfaceChanged "+window );
		resize();
		refreshUI();
		//startThread();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//Log.v(TAG, "surfaceDestroyed "+window);
		surfaceCreated = false;
		stopThread();

	}
	
	/** this thread is responsible for updating the canvas
	 * @see java.lang.Runnable#run() */
	public final void run() {		
		while (run && surfaceCreated && ScreenSlide.currentPage==window) {
			refreshUI();
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			refreshUI();
			break;
		default:
			break;
		}
		
		return true;
	}
	
	
	public final synchronized void startThread(){
		/* restrict to create and start a thread JUST when:
		 * There is no other thread running,
		 * The current page is this window and
		 * The SurfaceView has been crated
		 */
		if(run == false && ScreenSlide.currentPage==window && surfaceCreated){
			tDraw = new Thread(this);
			run = true;
			tDraw.start();
		}
	}
	
	public final synchronized void stopThread(){
		if(run){
			run = false;
			boolean retry = true;
			while (retry) {
				try {
					if (tDraw != null)
						tDraw.join();
					retry = false;
				} catch (InterruptedException e) {
					Log.e(TAG, "stopThread: " + e.getMessage());
				}
			}
		}
	}

	@Override
	public final synchronized void refreshUI() {
		if(Main.D)
			Log.v(TAG,"Refreshing UI"+(this instanceof HUDview? "HUDview":"TeamView"));
		canvas = null;
		if (surfaceCreated && sh != null){
			try {
				canvas = sh.lockCanvas(null);
				if(canvas != null)
					synchronized (sh) {
						onDraw(canvas);
					}
			} finally {
				if (canvas != null)
					sh.unlockCanvasAndPost(canvas);
			}	
		}
		else{
			if(Main.D)
				Log.e(TAG,"Refreshing UI"+(this instanceof HUDview? "HUDview":"TeamView")+" CANCELLED because surface is not created");
		}
		canvas = null;
	}
	
	
	@Override
	public final void refreshUI_newThread() {
		new Thread(new Runnable(){
			public void run(){
				refreshUI();
			}
		}).start();
	}

}
