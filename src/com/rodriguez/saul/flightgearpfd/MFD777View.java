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



package com.rodriguez.saul.flightgearpfd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MFD777View extends SurfaceView implements SurfaceHolder.Callback {

	public static final int BASIC = 0;
	public static final int B777 = 1;
	public static final int B787 = 2;
	public static final int B747 = 3;
	public static final int A330 = 4;
	public static final int A380 = 5;
	Context mcontext;
	Plane plane;
	int planeType;
	int mwidth;
	int mheight;
	int centerx;
	int centery;
	float scaleFactor;
	private SurfaceHolder surfaceHolder;
	
	public MFD777View(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mcontext = context;
		// TODO Auto-generated constructor stub
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		
		//plane = new Plane777(mcontext);
	}

	public void updateView(MessageHandlerFGFS[] values) {
		//update NAV
		plane.heading = values[0].getFloat(B777Protocol.HEADING);
		plane.apheading = values[0].getInt(B777Protocol.APHEADING);
	
		//VORL
		plane.vorlid = values[0].getString(B777Protocol.VORLID);
		plane.vorldme = values[0].getFloat(B777Protocol.VORLDME);
		plane.vorldmeinrange = values[0].getBool(B777Protocol.VORLDMEINRANGE);
		plane.vorlinrange = values[0].getBool(B777Protocol.VORLINRANGE);
		plane.vorlfreq = values[0].getFloat(B777Protocol.VORLFREQ);

		plane.switchvorl = values[0].getInt(B777Protocol.SWITCHLEFT);
		plane.vorldirection = values[0].getFloat(B777Protocol.VORLDIR);
	
		//VORRL
		plane.vorrid = values[0].getString(B777Protocol.VORRID);
		plane.vorldme = values[0].getFloat(B777Protocol.VORRDME);
		plane.vorrdmeinrange = values[0].getBool(B777Protocol.VORRDMEINRANGE);
		plane.vorrinrange = values[0].getBool(B777Protocol.VORRINRANGE);
		plane.vorrfreq = values[0].getFloat(B777Protocol.VORRFREQ);

		plane.switchvorr = values[0].getInt(B777Protocol.SWITCHRIGHT);
		plane.vorrdirection = values[0].getFloat(B777Protocol.VORRDIR);
	
		//ADFL
		plane.adflid = values[0].getString(G1000Protocol.ADFLID);
		plane.adflinrange = values[0].getBool(G1000Protocol.ADFLINRANGE);
		plane.adflfreq = values[0].getInt(G1000Protocol.ADFLFREQ);
		plane.adfldirection = values[0].getFloat(G1000Protocol.ADFLDIR);
	
		//ADFR
		plane.adfrid = values[0].getString(G1000Protocol.ADFRID);
		plane.adfrinrange = values[0].getBool(G1000Protocol.ADFRINRANGE);
		plane.adfrfreq = values[0].getInt(G1000Protocol.ADFRFREQ);
		plane.adfrdirection = values[0].getFloat(G1000Protocol.ADFRDIR);
	
		//RADIAL NAV1
		plane.radial = values[0].getFloat(G1000Protocol.RADIALDIR);
		plane.realheading = values[0].getFloat(G1000Protocol.RADIALHEAD);
		plane.radialdef = values[0].getFloat(G1000Protocol.RADIALDEF);
		plane.gsdef = values[0].getFloat(G1000Protocol.GSDEF);
	
		//Modes
		plane.mode = values[0].getInt(G1000Protocol.MODE);
		plane.range = values[0].getInt(G1000Protocol.RANGE);
		plane.modebut = values[0].getBool(G1000Protocol.MODEBUT);
	
		//Speed
		plane.truespeed = values[0].getFloat(G1000Protocol.TRUESPEED);
		plane.groundspeed = values[0].getFloat(G1000Protocol.GROUNDSPEED);
		plane.windspeed = values[0].getFloat(G1000Protocol.WINDHEADING);
		plane.windheading = values[0].getFloat(G1000Protocol.WINDSPEED);
	
		//Position
		plane.lat = values[0].getFloat(G1000Protocol.LATITUDE);
		plane.lon = values[0].getFloat(G1000Protocol.LONGITUDE);
		
		//route
		plane.latwp[0] = values[0].getFloat(G1000Protocol.LATWP1);
		plane.lonwp[0] = values[0].getFloat(G1000Protocol.LONWP1);
		plane.latwp[1] = values[0].getFloat(G1000Protocol.LATWP2);
		plane.lonwp[1] = values[0].getFloat(G1000Protocol.LONWP2);
		plane.latwp[2] = values[0].getFloat(G1000Protocol.LATWP3);
		plane.lonwp[2] = values[0].getFloat(G1000Protocol.LONWP3);
		plane.latwp[3] = values[0].getFloat(G1000Protocol.LATWP4);
		plane.lonwp[3] = values[0].getFloat(G1000Protocol.LONWP4);
		plane.latwp[4] = values[0].getFloat(G1000Protocol.LATWP5);
		plane.lonwp[4] = values[0].getFloat(G1000Protocol.LONWP5);
		plane.latwp[5] = values[0].getFloat(G1000Protocol.LATWP6);
		plane.lonwp[5] = values[0].getFloat(G1000Protocol.LONWP6);
		plane.latwp[6] = values[0].getFloat(G1000Protocol.LATWP7);
		plane.lonwp[6] = values[0].getFloat(G1000Protocol.LONWP7);
		plane.latwp[7] = values[0].getFloat(G1000Protocol.LATWP8);
		plane.lonwp[7] = values[0].getFloat(G1000Protocol.LONWP8);
		plane.latwp[8] = values[0].getFloat(G1000Protocol.LATWP9);
		plane.lonwp[8] = values[0].getFloat(G1000Protocol.LONWP9);
		plane.latwp[9] = values[0].getFloat(G1000Protocol.LATWP10);
		plane.lonwp[9] = values[0].getFloat(G1000Protocol.LONWP10);
		plane.latwp[10] = values[0].getFloat(G1000Protocol.LATWP11);
		plane.lonwp[10] = values[0].getFloat(G1000Protocol.LONWP11);
		plane.latwp[11] = values[0].getFloat(G1000Protocol.LATWP12);
		plane.lonwp[11] = values[0].getFloat(G1000Protocol.LONWP12);
		
		plane.currentwp = values[0].getString(G1000Protocol.CURRENTWP);
		plane.numwp = values[0].getInt(G1000Protocol.NUMWP);
		
		//Default plane is 777m other planes need to rearrange parameters
		if (planeType == MFD777View.A330) {
			rearrangeParamA330();				
		}
		
		//Check if the database needs update
		if (plane.checkUpdateDBNeeded()) {
			plane.updateDB();
		} else {
			draw();
		}
	}

	public void rearrangeParamA330()
	{
		switch (plane.mode) {
			case 0:  //ILS
			case 1: //VOR
				plane.modebut = true; //Circle
					break;
			case 2: //NAV
				plane.modebut = true; //Circle
				if (plane.switchvorl == 2)
					plane.switchvorl = -1;
				if (plane.switchvorr == 2)
					plane.switchvorr = -1;
					break;
			case 3: //ARC
				plane.modebut = false; //Arc
				plane.mode = 2;
					break;
			case 4: break;
			default: break;
					
		}
		
		
		//ARC
		
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
		
		//Log.d("Saul", String.format("width = %d", mwidth));
		//Log.d("Saul", String.format("height = %d", mheight));

		centerx = mwidth/2;
		centery = mheight/2;
		
		plane.centerx = centerx;
		plane.centery = centery;
		//Calculate the scale factor
		int maskHeight = plane.mask.getHeight();
		
		//scaleFactor = (float) 0.5; //Only for test and new features
		scaleFactor = (float)(mheight)/(float)maskHeight;
				
		plane.scaleFactor = scaleFactor;
		
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
	    		if (eventX < centerx && eventY < centery) { // left Up
					plane.shownav = plane.shownav != true;
				}

				if (eventX > centerx && eventY < centery) { //right up
					plane.showcir = plane.showcir != true;
				}

				if (eventX < centerx && eventY > centery) { //left down
					plane.showfix = plane.showfix != true;
				}

				if (eventX > centerx && eventY > centery) { //right down
					plane.showroute = plane.showroute != true;
				}

				break;
	    	case MotionEvent.ACTION_MOVE:
	    		break;
	    	case MotionEvent.ACTION_UP:
	            break;
	    	default:
	    		return false;
	    }
	    	    
	    return true;
	  }
	
	public void draw() {
		
		long time, time2;
		//time = System.currentTimeMillis();
			
		//Lock the canvas and start drawing
	        Canvas canvas = surfaceHolder.lockCanvas();
	        
	        plane.draw(canvas);
	        
	        surfaceHolder.unlockCanvasAndPost(canvas);
	        
	        //time2 = System.currentTimeMillis();
  		//Log.d("777View", String.format("%d", (time2-time)));
       
	}
	
	//Setters
	void setPlane(int planetype) 
	{
		planeType = planetype;
		//Log.d("SELECTED PLANE",String.format("%d",planeType));
		
		
		switch (planeType) {
			case BASIC: 
			case B777:  
			case A330:  
						plane = new Plane777(mcontext);
						setPlaneType(planeType);
						break;
			case B787:  
			case B747: 
			case A380: 
			default:
						break;
		}
	}
	
	void setPlaneType(int planeType)
	{
		plane.planeType = planeType;
	}
}
