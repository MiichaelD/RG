package com.webs.itmexicali.RealGaming;
import com.webs.itmexicali.RealGaming.R;
import com.webs.itmexicali.RealGaming.drawcomps.*;
import com.webs.itmexicali.RealGaming.frags.InfoUpdateListener;
import com.webs.itmexicali.RealGaming.frags.ScreenSlide;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class PadView extends SurfaceView implements Callback, InfoUpdateListener {

	private SurfaceHolder sh;
	private Paint paints[];
	private int textSize, w ,h, numberOfButtons=3;
	private Rect screen;
	
	DrawButtonContainer dbc;
	
	boolean mPressed[];
	private Bitmap BMPimg;

	public PadView(Context context) {
		super(context);
		sh = getHolder();
		sh.addCallback(this);
		
		dbc = new DrawButtonContainer(numberOfButtons);
		
	}

	
	public void initPaints() {
		paints = new Paint[5];
		
		paints[0]= new Paint();
		paints[0].setColor(Color.WHITE);
		paints[0].setTextAlign(Align.CENTER);
		paints[0].setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/KellySlab-Regular.ttf"));
		paints[0].setTextSize(textSize);
		paints[0].setAntiAlias(true);
		
		paints[1] = new Paint();
		paints[1].setColor(Color.BLUE);
		paints[1].setAntiAlias(true);
		paints[1].setAlpha(210);
		
		paints[2] = new Paint();
		paints[2].setColor(Color.CYAN);
		paints[2].setAntiAlias(true);
		
		paints[3] = new Paint();
		paints[3].setColor(Color.argb(125, 100, 180, 180));
		paints[3].setAntiAlias(true);

		
		paints[4]= new Paint();
		paints[4].setColor(Color.WHITE);
		paints[4].setTextAlign(Align.CENTER);
		paints[4].setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/KellySlab-Regular.ttf"));
		paints[4].setTextSize(textSize*2/3);
		paints[4].setAntiAlias(true);
	}

	public void onDraw(Canvas canvas) {
		try {
			canvas.drawBitmap(BMPimg, 0, 0, null);
			
			//canvas.drawRoundRect(mButton[0], 50.0f, 20.0f, (mPressed[0]?pRed:pBlue));
			canvas.drawRoundRect(dbc.getDButton(0), 25.0f, 20.0f, paints[1]);
			canvas.drawText(Main.instance.getString(R.string.game_monitor), 30*w, 8*h, paints[0]);
			
			canvas.drawRoundRect(dbc.getDButton(1), 25.0f, 20.0f, paints[1]);
			canvas.drawText(Main.instance.getString(R.string.changeValues), 15*w, 35*h, paints[4]);
			
			canvas.drawRoundRect(dbc.getDButton(2), 25.0f, 20.0f, paints[1]);
			canvas.drawText(Main.instance.getString(R.string.logout), 45*w, 35*h, paints[4]);
			
	
			for(int i =0 ; i < dbc.getButtonsCount();i++)
				if( dbc.getDButton(i).isPressed() ){
					canvas.drawRoundRect(dbc.getDButton(i), 25.0f, 20.0f, paints[3]);
				}
		} catch (Exception e) {
			if(Main.D){
				Log.e(Main.TAG, "onDraw - "+ e);
				e.printStackTrace();
			}
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		{
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerId = event.getPointerId(pointerIndex);
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				/*ViewSwitcher.animationOut(Main.instance.PView, Main.instance.getWindowManager(), new ViewSwitcher.AnimationFinishedListener() {
		            public void onAnimationFinished() {
		            	if (Main.D)
							Log.v(Main.TAG,"+PadView LOOOOL ");
						ViewSwitcher.animationIn(Main.instance.PView, Main.instance.getWindowManager());
		            }
		        });	*/
				
				if (Main.D)
					Log.v(Main.TAG,"+PadView OnTouchEvent(Action_Down)\tID:"+ pointerId + " - Index" + pointerIndex);
				
				dbc.onPressUpdate(event, pointerIndex, pointerId);
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:				
				
				if (Main.D)
					Log.d(Main.TAG,	"+PadView OnTouchEvent(Action_Up)\tID:"+ pointerId + " - Index" + pointerIndex);
				
				dbc.onReleaseUpdate(event, pointerIndex, pointerId);
				break;

			case MotionEvent.ACTION_MOVE:
				dbc.onMoveUpdate(event, pointerIndex);
				break;
			}
		}
		refreshUI();
		return true;
	}
	
		
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		w = getWidth() / 60;		
		h = getHeight() / 40;
		textSize = 6*h;
		initPaints();
		screen = new Rect(0, 0, getWidth(), getHeight());		
		BMPimg=BitmapLoader.resizeImage(this.getContext(),R.drawable.realgaming,getWidth(),getHeight());
		
		
		dbc.initDrawButton(0, 15.0f*w, 1.75f*h, 45.0f*w, 10.0f*h);
		dbc.initDrawButton(1, 5.0f*w, 30f*h, 26.0f*w, 36.5f*h);
		dbc.initDrawButton(2, 34.0f*w, 30f*h, 55.0f*w, 36.5f*h);
		
		//register an action to button 0:
		dbc.setOnActionListener(0, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override
			public void onActionPerformed() {
				Main.instance.startActivity(new Intent(Main.instance, ScreenSlide.class));
			}
		});
		
		dbc.setOnActionListener(1, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override
			public void onActionPerformed() {
				Main.instance.startBluetoothScan();
			}
		});
		
		dbc.setOnActionListener(2, DrawButtonContainer.RELEASE_EVENT, new DrawButton.ActionListener(){
			@Override
			public void onActionPerformed() {
				SessionManager.Logout();
	    		SessionManager.requestLogin(Main.instance);
			}
		});
		
		/*
		mPressed = new boolean[numberOfButtons];
		mButton = new RectF[numberOfButtons];
		mButton[0] = new RectF(15.0f*w, 3.5f*h/2.0f, 45.0f*w, 10.0f*h);
		*/
		
		refreshUI_newThread();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (Main.D)
			Log.v(Main.TAG, "+PGVIEW surfaceDestroyed");
	}


	@Override
	public final void refreshUI_newThread() {
		new Thread(new Runnable(){
			public void run(){
				refreshUI();
			}
		}).start();
	}
	
	@SuppressLint("WrongCall")
	public synchronized void refreshUI() {
		Canvas canvas = null;
		try {
			canvas = sh.lockCanvas(null);
			synchronized (sh) {
				onDraw(canvas);
			}
		} finally {
			if (canvas != null)
				sh.unlockCanvasAndPost(canvas);
		}
	}

}
