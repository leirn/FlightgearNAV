/*
 * Copyright (C) 2014  Saul Rodriguez

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

	private SurfaceHolder surfaceHolder;
	Context mcontext;
		
	
	public static final int BASIC = 0;
	public static final int B777 = 1;
	public static final int B787 = 2;
	public static final int B747 = 3;
	public static final int A330 = 4;	
	public static final int A380 = 5;	
	public static final int G1000 = 6;
		
	Plane plane;
	int planeType;
	
	int mwidth;
	int mheight;	
	int centerx;
	int centery;
	float scaleFactor;
			
	
	public MFD777View(Context context, AttributeSet attrs) {
		super(context, attrs);
				
		
		mcontext = context;
		// TODO Auto-generated constructor stub
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		
		//plane = new Plane777(mcontext);
				
		
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
	    			if (plane.shownav == true) {
	    				plane.shownav = false;
	    			} else {
	    				plane.shownav = true;	    			
	    			}
	    		}
	    		
	    		if (eventX > centerx && eventY < centery) { //right up
	    			if (plane.showcir == true) {
	    				plane.showcir = false;
	    			} else {
	    				plane.showcir = true;	    			
	    			}
	    		}
	    		
	    		if (eventX < centerx && eventY > centery) { //left down
	    			if (plane.showfix == true) {
	    				plane.showfix = false;
	    			} else {
	    				plane.showfix = true;	    			
	    			}
	    		}
	    		
	    		if (eventX > centerx && eventY > centery) { //right down
	    			if (plane.showroute == true) {
	    				plane.showroute = false;
	    			} else {
	    				plane.showroute = true;	    			
	    			}
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
		//				plane = new PlaneFree(mcontext);
						plane = new Plane777(mcontext);
						setPlaneType(planeType);
						break;
			case B777:  
						plane = new Plane777(mcontext);
						setPlaneType(planeType);
						break;
			case B787:  
			//			plane = new Plane787(mcontext);
						break;
			case B747:  
				//		plane = new Plane747(mcontext);
						break;
						
			case A330:  
				//		plane = new PlaneA330(mcontext);
						plane = new Plane777(mcontext);
						setPlaneType(planeType);
						break;
						
			case A380:  
					//	plane = new PlaneA380(mcontext);
						break;
						
			case G1000:  
				//		plane = new PlaneA330(mcontext);
						plane = new PlaneG1000(mcontext);
						setPlaneType(planeType);
						break;
			default:	
				//		plane = new PlaneFree(mcontext);
						break;
						
		}
		
	}
	
	void setPlaneType(int planeType)
	{
		plane.planeType = planeType;
	}
	
	void setHeading(float newHeading)
	{
		plane.heading = newHeading;		
	}
	
	void setAPheading(int newAPheading)
	{
		plane.apheading = newAPheading;		
	}
	
	void setNAV1ID(String newvorid)
	{
		plane.vorlid = newvorid;
	}
	
	void setNAV1DME(float newdme)
	{
		plane.vorldme = newdme;
	}
	
	void setNAV1DMEinrange(boolean newdmeinrange)
	{
		plane.vorldmeinrange = newdmeinrange;
	}
	
	void setNAV1inrange(boolean newinrange)
	{
		plane.vorlinrange = newinrange;
	}
	
	void setNAV1freq(float newfreq)
	{
		plane.vorlfreq = newfreq;
	}
	
	void setSwitchleft(int newswitch)
	{
		plane.switchvorl = newswitch;
	}
	
	void setNAV1dir(float newdir)
	{
		plane.vorldirection = newdir;
	}
	

	void setNAV2ID(String newvorid)
	{
		plane.vorrid = newvorid;
	}
	
	void setNAV2DME(float newdme)
	{
		plane.vorrdme = newdme;
	}
	
	void setNAV2DMEinrange(boolean newdmeinrange)
	{
		plane.vorrdmeinrange = newdmeinrange;
	}
	
	void setNAV2inrange(boolean newinrange)
	{
		plane.vorrinrange = newinrange;
	}
	
	void setNAV2freq(float newfreq)
	{
		plane.vorrfreq = newfreq;
	}
	
	void setSwitchright(int newswitch)
	{
		plane.switchvorr = newswitch;
	}
	
	void setNAV2dir(float newdir)
	{
		plane.vorrdirection = newdir;
	}
	
	void setADF1ID(String newadfid)
	{
		plane.adflid = newadfid;
	}
	
	void setADF1inrange(boolean newinrange)
	{
		plane.adflinrange = newinrange;
	}
	
	void setADF1freq(int newfreq)
	{
		plane.adflfreq = newfreq;
	}
	
	void setADF1dir(float newdir)
	{
		plane.adfldirection = newdir;
	}
	
	void setADFrID(String newadfid)
	{
		plane.adfrid = newadfid;
	}
	
	void setADFrinrange(boolean newinrange)
	{
		plane.adfrinrange = newinrange;
	}
	
	void setADFrfreq(int newfreq)
	{
		plane.adfrfreq = newfreq;
	}
	
	void setADFrdir(float newdir)
	{
		plane.adfrdirection = newdir;
	}
	
	void setRaddir(float rad)
	{
		plane.radial = rad;
	}
	
	void setRadhead(float head)
	{
		plane.realheading = head;
	}
	
	void setRaddef(float def)
	{
		plane.radialdef = def;
	}
	
	void setGSdef(float def)
	{
		plane.gsdef = def;
	}
	
	void setMode(int newmode)
	{
		plane.mode = newmode;
	}
	
	void setRange(int newrange)
	{
		plane.range = newrange;
	}
	
	void setModebut(boolean newmodebut)
	{
		plane.modebut = newmodebut;
	}
	
	void setTruespeed(float newspeed)
	{
		plane.truespeed = newspeed;
	}
	
	void setGroundpeed(float newspeed)
	{
		plane.groundspeed = newspeed;
	}
	
	void setWindspeed(float newspeed)
	{
		plane.windspeed = newspeed;
	}
	
	void setWindhead(float newhead)
	{
		plane.windheading = newhead;
	}
	
	void setLat(float newLat)
	{
		plane.lat = newLat;
	}
	
	void setLon(float newLon)
	{
		plane.lon = newLon;
	}
	
	void setLatwp0(float la)
	{
		plane.latwp[0] = la;
	}
	
	void setLonwp0(float lo)
	{
		plane.lonwp[0] = lo;
	}
	
	void setLatwp1(float la)
	{
		plane.latwp[1] = la;
	}
	
	void setLonwp1(float lo)
	{
		plane.lonwp[1] = lo;
	}
	
	void setLatwp2(float la)
	{
		plane.latwp[2] = la;
	}
	
	void setLonwp2(float lo)
	{
		plane.lonwp[2] = lo;
	}
	
	void setLatwp3(float la)
	{
		plane.latwp[3] = la;
	}
	
	void setLonwp3(float lo)
	{
		plane.lonwp[3] = lo;
	}
	
	void setLatwp4(float la)
	{
		plane.latwp[4] = la;
	}
	
	void setLonwp4(float lo)
	{
		plane.lonwp[4] = lo;
	}
	
	void setLatwp5(float la)
	{
		plane.latwp[5] = la;
	}
	
	void setLonwp5(float lo)
	{
		plane.lonwp[5] = lo;
	}
	
	void setLatwp6(float la)
	{
		plane.latwp[6] = la;
	}
	
	void setLonwp6(float lo)
	{
		plane.lonwp[6] = lo;
	}

	void setLatwp7(float la)
	{
		plane.latwp[7] = la;
	}
	
	void setLonwp7(float lo)
	{
		plane.lonwp[7] = lo;
	}
	
	void setLatwp8(float la)
	{
		plane.latwp[8] = la;
	}
	
	void setLonwp8(float lo)
	{
		plane.lonwp[8] = lo;
	}

	void setLatwp9(float la)
	{
		plane.latwp[9] = la;
	}
	
	void setLonwp9(float lo)
	{
		plane.lonwp[9] = lo;
	}
	
	void setLatwp10(float la)
	{
		plane.latwp[10] = la;
	}
	
	void setLonwp10(float lo)
	{
		plane.lonwp[10] = lo;
	}
	
	void setLatwp11(float la)
	{
		plane.latwp[11] = la;
	}
	
	void setLonwp11(float lo)
	{
		plane.lonwp[11] = lo;
	}
	
	void setCurrentwp(String cur)
	{
		plane.currentwp = cur;
	}
	
	void setNumwp(int num)
	{
		plane.numwp = num;
	}
	
	/*
	void SetSpeed(float newSpeed) 
	{
		plane.speed = newSpeed;
	}
	
	void setAltitude(float newAltitude)
	{
		plane.altitude = newAltitude;
	}
	
	void setVerticalSpeed(float newverticalSpeed) 
	{
		plane.verticalSpeed = newverticalSpeed;		
	}
	
	void setRoll(float newRoll)
	{
		plane.horizontRollAngle = -newRoll;	//the roll angle of FGFS is anti-clockwise whereas the rotation of matrix in android is clockwise
	}
	
	void setPitch(float newPitch)
	{
		plane.horizontPitchAngle = newPitch;
	}
	
	void setHeading(float newHeading)
	{
		plane.heading = newHeading;		
	}
	
	void setNAV1Quality(float newQuality)
	{
		plane.locnavQuality = newQuality;
	}
	
	void setNAV1loc(boolean newNavLoc)
	{
		plane.locnav = newNavLoc;		
	}
	
	void setNAV1deflection(float newDeflection)
	{
		plane.headingLoc = newDeflection;
	}
	
	void setGSActive(boolean newGSactive)
	{
		plane.gsActive = newGSactive;		
	}
	
	void setGSInRange(boolean newgsInRange)
	{
		plane.gsInRange = newgsInRange;		
	}
	
	void setGSdeflection(float newgsDeflection)
	{
		plane.gsDeflection = newgsDeflection;
	}
	
	void setRadioaltimeter(int newRadioaltimeter)
	{
		plane.radioaltimeter = newRadioaltimeter;		
	}
	
	void setMach(float newMach)
	{
		plane.mach = newMach;
	}
	
	void setStallSpeed(float newStallSpeed)
	{
		plane.stallspeed = newStallSpeed; 
	}
	
	void setStallWarning(boolean newStallWarning)
	{
		plane.stallwarning = newStallWarning;		
	}
	
	void setFlaps(float newFlaps)
	{
		plane.flaps = newFlaps;
	}
	
	void setMaxSpeed(float newMaxSpeed)
	{
		plane.maxspeed = newMaxSpeed;
	}
	
	void setApIndicator(String newApIndicator)
	{
		plane.apIndicator = newApIndicator;
		//Log.d("Saul",apIndicator);
	}
	
	void setPitchMode(String newPitchMode)
	{
		plane.pitchMode = newPitchMode;
		//Log.d("Saul",pitchMode);
	}
	
	void setRollMode(String newRoleMode)
	{
		plane.rollMode = newRoleMode;
	}
	
	void setSpeedMode(String newSpeedMode)
	{
		plane.speedMode = newSpeedMode;
	}
	
	void setAPaltitude(float newApAltitude)
	{
		plane.apaltitude = newApAltitude;
	}
	
	void setAPactualaltitude(float newAPactualaltitude)
	{
		plane.apactualaltitude = newAPactualaltitude;
	}
	
	void setAPspeed(float newAPspeed)
	{
		plane.apspeed = newAPspeed;
	}
	
	void setAPheading(int newAPheading)
	{
		plane.apheading = newAPheading;		
	}
	
	void setDMEinrange(boolean inrange)
	{
		plane.dmeinrange = inrange;
	}
	
	void setDME(float newdme)
	{
		plane.dme = newdme;
	}
	
	
	*/
	
	
}
