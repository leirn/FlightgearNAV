/*
 * Copyright (C) 2014  Saul Rodriguez
   Copyright (C) 2015  Laurent Vromman

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

/***
 * Garmin G1000 Color
 * Text : 
 * 	White : 255, 255, 255
 * 	Magenta : 255, 0, 255
 * 	Cyan : 0, 255, 255
 * 	Inactive : 128, 128, 128
 * 	Background : 32, 32, 32
 * Gauge :
 * Green : 0, 128, 0
 * Yellow : 255, 255, 0
 * Red : 255, 0, 0
 * 
 * Softkey :
 * 	Inactive :
 * 		Text : 255, 255, 255
 * 		Background : 32, 32, 32
 * 	Active : 
 * 		Text : 32, 32, 32
 * 		Background : 128, 128, 128
 */


package com.rodriguez.saul.flightgearpfd;

import com.rodriguez.saul.flightgearpfd.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MFDG1000View extends SurfaceView implements SurfaceHolder.Callback {

	boolean ongoingMotion;
	int startMotionX;

	private SurfaceHolder surfaceHolder;
	Context mcontext;
	Plane plane;
	int planeType;
	
	G1000Panels panels;
	
	int mwidth;
	int mheight;	
	int centerx;
	int centery;
	float scaleFactor;
	float g1000scaleFactor;
	int pfdormfd;
	static int PFD = 0;
	static int MFD = 1;
	

	
	public MFDG1000View(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ongoingMotion = false;
		
		panels = new G1000Panels(this.getWidth(), this.getHeight());
				
		mcontext = context;
		// TODO Auto-generated constructor stub
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		
		pfdormfd = MFD;
	}

	public updateView(values) {
		//update NAV
		plane.update(values);
		draw();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mwidth = this.getWidth();
		mheight = this.getHeight();
		
		if()
		
		//Log.d("Saul", String.format("width = %d", mwidth));
		//Log.d("Saul", String.format("height = %d", mheight));

		centerx = mwidth/2;
		centery = mheight/2;
		
		//Calculate the scale factor
		int maskHeight = plane.mask.getHeight();
		
		//scaleFactor = (float) 0.5; //Only for test and new features
		scaleFactor = (float)(mheight)/(float)maskHeight;
		
		//Calculate the scale factor
		int g1000enginedisplayHeight = plane.g1000enginedisplay.getHeight();
		
		//scaleFactor = (float) 0.5; //Only for test and new features
		g1000scaleFactor = (float)(mheight)/(float)g1000enginedisplayHeight;
		
		//plane.
		plane.reflat = 0;
		plane.reflon = 0;
		//Draw the view
		draw();	
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    float eventX = event.getX();
	    float eventY = event.getY();

	    switch (event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    		ongoingMotion = true;
	    		startMotionX = event.getX();
	    		break;
	    	case MotionEvent.ACTION_MOVE:
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		ongoingMotion = false;
	    		//Switch from MFD to PFD only if motion covers 30% of screen width
	    		if(Maths.abs(startMotionX - event.getX()) / mwidth > 0.3)
	    			pfdormfd = !pfdormfd;
	    		// Detect if softkeys pressed
	    		if(event.getY() > (1 - G1000Panels.SOFTKEYS_HEIGHT) * mheight)
	    			//TODO : Managed softkey actions
	    			return true;
	            break;
	    	default:
	    		return false;
	    }
	    	    
	    return true;
	}
	
	public void draw(Canvas canvas) {
		Canvas canvas = surfaceHolder.lockCanvas();
	        
		Paint paint = new Paint();
        	paint.setAntiAlias(true);
	        paint.setFilterBitmap(true);
	        
	        drawG1000Background(canvas, paint);
	        
	        if (pfdormfd == MFD;)
	        {
		        drawG1000EngineDisplay(canvas,paint);
	        }
	        else {
	        }
		//drawHsiArc(canvas, paint);
		
        	drawG1000TopBar(canvas,paint);
	        drawG1000SoftKeys(canvas,paint);
	        
	        surfaceHolder.unlockCanvasAndPost(canvas);
	}
	
	public void drawG1000Background(canvas, paint)
	{
		paint.setColor(Color.rgb(32, 32, 32));
		paint.setStyle(Paint.Style.FILL_AND_STROKE);		
		canvas.drawRect(0, 0, mwidth, mheight, paint);
	}
	
	public void drawG1000TopBar(canvas,paint)
	{
		Matrix m = new Matrix();
		m.reset();
		//Paint empty panel
		canvas.drawBitmap(panels.getTopBar(),m,paint);
		
		// Top bar uses roughly 10% of total height
		
		
		//Display NAV1 / NAV2
		//Display COM1 / COM2
		//Display GS / TRK / BRG / ETE
	}
	
	public void drawG1000SoftKeys(canvas,paint)
	{
		// SoftKey uses roughly 4.5% of total height
		Matrix m = new Matrix();
		m.reset();
		//Paint empty panel
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(panels.getSoftKeys(),m,paint);
	}
	
	public void drawG1000EngineDisplay(canvas,paint)
	{
		// EIS uses roughly 86% of total height
		
		float EISscale = 0.86 * g1000scaleFactor;
		
		Matrix m = new Matrix();
		m.reset();
		//Paint panel
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(g1000enginedisplay,m,paint);
		//Paint right vertical white line
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float)(2 * g1000scaleFactor);
		paint.setStyle(Style.STROKE);
		canvas.drawLine(g1000enginedisplay.getHeight() * EISscale, 0.10 * height, g1000enginedisplay.getHeight() * EISscale, 0.96 * height, paint);
		//Paint left load gauge
		m.reset();
		m.postTranslate(32.0, (31.0 + (100 - plane.leftload) * 246);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(leftgauge,m,paint);
		//Paint right load gauge
		m.reset();
		m.postTranslate(287.0, (31.0 + (100 - plane.rightload) * 246);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(rightgauge,m,paint);
		//Paint left rpm gauge
		m.reset();
		m.postTranslate(32.0, (367.0 + (100 - plane.leftrpm) * 270);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(leftgauge,m,paint);
		//Paint right rpm gauge
		m.reset();
		m.postTranslate(287.0, (367.0 + (100 - plane.rightrpm) * 270);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(rightgauge,m,paint);
		//Paint left fuel flow
		drawG1000Numbers(canvas, paint, value, x, y)
		//Paint right fuel flow
		drawG1000Numbers(canvas, paint, value, x, y)
		//Paint left oil tempm.reset();
		m.postTranslate((25.0 + plane.leftoiltemp * 300, 866.0);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(lefttriangle,m,paint);
		//Paint right oil temp
		m.postTranslate((25.0 + plane.rightoiltemp * 300, 915.0);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(righttriangle,m,paint);
		//Paint left oil pres
		m.postTranslate((25.0 + plane.leftoilpres * 300, 1015.0);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(lefttriangle,m,paint);
		//Paint right oil pres
		m.postTranslate((25.0 + plane.rightoilpres * 300, 1064.0);
		m.postScale(EISscale, EISscale);
		m.postTranslate(0.1 * height, 0);
		canvas.drawBitmap(righttriangle,m,paint);
		//Paint left coolant temp
		canvas.drawBitmap(lefttriangle,m,paint);
		//Paint right coolant temp
		canvas.drawBitmap(righttriangle,m,paint);
		//Paint left fuel temp
		canvas.drawBitmap(lefttriangle,m,paint);
		//Paint right fuel temp
		canvas.drawBitmap(righttriangle,m,paint);
		//Paint left fuel qty gal
		canvas.drawBitmap(lefttriangle,m,paint);
		//Paint right fuel qty gal
		canvas.drawBitmap(righttriangle,m,paint);
	}
	
	private drawG1000Numbers(Canvas canvas, Paint paint, float value, int x, int y) {
		
	}
	
	//Setters
	void setPlane(int planetype) 
	{
		planeType = planetype;
		//Log.d("SELECTED PLANE",String.format("%d",planeType));
		
		switch (planeType) {
			case G1000:  
						plane = new PlaneG1000(mcontext);
						setPlaneType(planeType);
						break;
			// This view only managed G1000
			default:	
				//		plane = new PlaneFree(mcontext);
						break;
		}
	}
	
	void setPlaneType(int planeType)
	{
		plane.planeType = planeType;
	}
}
