package com.rodriguez.saul.flightgearpfd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;


public class myWebView  extends WebView {
	Context mContext;
	
	int width;
	int height;
	
	public int x,y; //offset values from center
	
	//Encoder Modes
	public int mode;
	public int range;
	
	public int rangeref;
	float rangescale;
	
	float dpi;
	
	//position
	float lat;
	float lon;
	float reflat;
	float reflon;
	
	
	//Helper distance	
	double distx;
	double disty;
	
	//Bitmaps
	Bitmap planebmp;
	Bitmap symbols;
	//Matrices
	Matrix planeMatrix;
	
	float scaleFactor;
	float realheading;
	
	//waypoint
	float[] latwp;
	float[] lonwp;
	final int ROUTESIZE = 12; //number if waypoints
	
	boolean showmap;  //true for map, false for chart
	
	int offsety;
	
	public myWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		
		x = 0;
		y = -20;
		
		mode = 0;
		range = 10;
		rangeref = 0;
		rangescale = 1;
		
		dpi = 160;
		
		scaleFactor = 1;
	    realheading = 0;
		
		planeMatrix = new Matrix();
		planebmp = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.planebmp);
		//planebmp = Bitmap.createBitmap(symbols, 0, 0, 67, 85);
		
		//route
		latwp = new float[ROUTESIZE];
		lonwp = new float[ROUTESIZE];
		
		showmap = true;  
		
		//offsety = (int)(0.7/2.54*dpi)/2;
		
		
	}

	public updateView(values) {
		// Update webview
		setMode(values[0].getInt(MessageHandlerFGFS.MODE));
		setRange(values[0].getInt(MessageHandlerFGFS.RANGE));
		setLat(values[0].getFloat(MessageHandlerFGFS.LATITUDE));
		setLon(values[0].getFloat(MessageHandlerFGFS.LONGITUDE));
		setRadhead(values[0].getFloat(MessageHandlerFGFS.RADIALHEAD));
		
		//route
		setLatwp0(values[0].getFloat(MessageHandlerFGFS.LATWP1));
		setLonwp0(values[0].getFloat(MessageHandlerFGFS.LONWP1));
		setLatwp1(values[0].getFloat(MessageHandlerFGFS.LATWP2));
		setLonwp1(values[0].getFloat(MessageHandlerFGFS.LONWP2));
		setLatwp2(values[0].getFloat(MessageHandlerFGFS.LATWP3));
		setLonwp2(values[0].getFloat(MessageHandlerFGFS.LONWP3));
		setLatwp3(values[0].getFloat(MessageHandlerFGFS.LATWP4));
		setLonwp3(values[0].getFloat(MessageHandlerFGFS.LONWP4));
		setLatwp4(values[0].getFloat(MessageHandlerFGFS.LATWP5));
		setLonwp4(values[0].getFloat(MessageHandlerFGFS.LONWP5));
		setLatwp5(values[0].getFloat(MessageHandlerFGFS.LATWP6));
		setLonwp5(values[0].getFloat(MessageHandlerFGFS.LONWP6));
		setLatwp6(values[0].getFloat(MessageHandlerFGFS.LATWP7));
		setLonwp6(values[0].getFloat(MessageHandlerFGFS.LONWP7));
		setLatwp7(values[0].getFloat(MessageHandlerFGFS.LATWP8));
		setLonwp7(values[0].getFloat(MessageHandlerFGFS.LONWP8));
		setLatwp8(values[0].getFloat(MessageHandlerFGFS.LATWP9));
		setLonwp8(values[0].getFloat(MessageHandlerFGFS.LONWP9));
		setLatwp9(values[0].getFloat(MessageHandlerFGFS.LATWP10));
		setLonwp9(values[0].getFloat(MessageHandlerFGFS.LONWP10));
		setLatwp10(values[0].getFloat(MessageHandlerFGFS.LATWP11));
		setLonwp10(values[0].getFloat(MessageHandlerFGFS.LONWP11));
		setLatwp11(values[0].getFloat(MessageHandlerFGFS.LATWP12));
		setLonwp11(values[0].getFloat(MessageHandlerFGFS.LONWP12));
							
		updateRange();
	
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		width = this.getWidth();
		height = this.getHeight();
		
		
		//Offset assumes that top banner measures around 1.1cm and bottom banner 0,4 cm
		offsety = (int)((1.1/2.54*dpi) + (height - 1.5/2.54*dpi)/2) - height/2;
		//Log.d("Saul",String.format("Width WebView %d", width));
		//Log.d("Saul",String.format("heigth WebView %d", height));
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        
		//paint.setColor(Color.BLUE);
		//paint.setStyle(Style.STROKE);
		//paint.setStrokeWidth(2);
		
		//drawPlane(canvas,paint, 0);
		//drawRoute(canvas,paint, 0);
		
		drawPlane(canvas,paint, offsety);
		drawRoute(canvas,paint, offsety);
		
		//canvas.drawCircle((float)(width/2. + distx),(float)(height/2. - (disty-20)), 10, paint);
		
	}
	
	void drawPlane(Canvas canvas, Paint paint, int offsety)
	{
		calcDistance(reflat,reflon,lat,lon);
		dist2pixels();
		
		if (showmap == false) { //Correct distances for World hi charts
			distx *= 0.375;
			disty *= 0.375;				
		}
		
		float aux;
				
		aux = (float)(1/2.54*dpi); //real height of plane = 1cm
		scaleFactor = aux/planebmp.getHeight();
		
		planeMatrix.reset();
		planeMatrix.postTranslate(-planebmp.getWidth()/2, -planebmp.getHeight()/2);
		planeMatrix.postScale(scaleFactor, scaleFactor);
		planeMatrix.postRotate(realheading);
				
		planeMatrix.postTranslate((float)(width/2 + distx),(float)(height/2 + offsety - disty));
								
		canvas.drawBitmap(planebmp, planeMatrix, paint);
	}

	void drawRoute(Canvas canvas, Paint paint, int offsety)
	{
		paint.setColor(Color.MAGENTA);
		paint.setStyle(Style.STROKE);
		paint.setTextSize((float)(10*scaleFactor));
		paint.setStrokeWidth(2*scaleFactor);
				
				
		//float dist,distx, disty, angle;

		float dist,angle;
		float pointx[] = new float[ROUTESIZE];
		float pointy[] = new float[ROUTESIZE];
		
		int count = 0;
		
		for (int i = 0; i<ROUTESIZE; i++ ){
			if (latwp[i] == 0 && lonwp[i] == 0)
				break;
			
			count++;
			
			calcDistance(reflat,reflon,latwp[i],lonwp[i]);
			dist2pixels();
			
			if (showmap == false) { //Correct distances for World hi charts
				distx *= 0.375;
				disty *= 0.375;				
			}
			
			//dist = fixdb.calcDistance(lat, lon, latwp[i], lonwp[i]);
			//distx = (float)fixdb.distx;
			//disty = (float)fixdb.disty;
			
			//distx = (distx/1852)*(324/range); //pixels
			//disty = (disty/1852)*(324/range); //pixels
			//dist = (dist/1852)*(324/range); //pixels
			
			//angle = (float)Math.atan2(disty,distx);						
			//angle = angle + (float)(heading/180*Math.PI);
			//angle = angle + (float)(realheading/180*Math.PI);
			
			//distx = (float) (dist*Math.cos(angle));
			//disty = (float)(dist*Math.sin(angle));
			/*
			ndwpMatrix.reset();
			ndwpMatrix.postTranslate(-ndwp.getWidth()/2, -ndwp.getHeight()/2);
			ndwpMatrix.postScale((float)(0.5*scaleFactor), (float)(0.5*scaleFactor));
			*/
		
			pointx[i] = width/2 + (float)distx;
			pointy[i] = height/2 + (float)((offsety-disty));
			//ndwpMatrix.postTranslate(centerx + (float)distx*scaleFactor, centery + (float)((offsety-disty)*scaleFactor));
			/*
			ndwpMatrix.postTranslate(pointx[i],pointy[i]);
			
			canvas.drawBitmap(ndwp, ndwpMatrix, paint);
				*/
		}
		
		for (int i = 0; i <(count-1); i++) {
			canvas.drawLine(pointx[i], pointy[i], pointx[i+1], pointy[i+1], paint);
		}
		
	}
	
	
	void updateRange() {
		
		if (rangeref == range) {
			//Calculate plane distances 
			//calcDistance(reflat,reflon,lat,lon);
			//dist2pixels();
			
			invalidate();
			
			return;
		}
		
		//Reference coord. changed
		updateRefPos();
		
		rangeref = range;
		
		loadSkyvector();
		
	}
	
	void loadSkyvector() 
	{
		int range_aux;
		
		switch (range) {
			case 10:
						range_aux = 1;
						rangescale = 1;
						break;
			case 20:
						range_aux = 3;
						rangescale = 2;
						break;
			case 40:
						range_aux = 5;
						rangescale = 4;
						break;
			case 80:
						range_aux = 7;
						rangescale = 8;
						break;
			case 160:	
						range_aux = 9;
						rangescale = 16;
						break;
			case 320:
						range_aux = 11;
						rangescale = 32;
						break;
			case 640:
						range_aux = 13;
						rangescale = 64;
						break;
			default:	
						range_aux = 1;
						break;
		}
		
		String aux;
		if (showmap == true) {
			aux = "http://skyvector.com/?ll=" + String.format("%f", lat) + "," + String.format("%f", lon) + 
					"&chart=301&zoom=" + String.format("%d", range_aux);
		} else {
			aux = "http://skyvector.com/?ll=" + String.format("%f", lat) + "," + String.format("%f", lon) + 
					"&chart=304&zoom=" + String.format("%d", range_aux);
		}
		this.loadUrl(aux);
		
	}
	
	float calcDistance(float Lat1, float Lon1, float Lat2, float Lon2)
	{
		double latMid, m_per_deg_lat, m_per_deg_lon, deltaLat, deltaLon,dist_m;

		latMid = (Lat1+Lat2 )/2.0*(Math.PI/180);  // radians!  just use Lat1 for slightly less accurate estimate


		m_per_deg_lat = 111132.954 - 559.822 * Math.cos( 2.0 * latMid ) + 1.175 * Math.cos( 4.0 * latMid);
		m_per_deg_lon = (3.14159265359/180 ) * 6367449 * Math.cos ( latMid );

				
		deltaLat = (Lat2 - Lat1);
		deltaLon = (Lon2 - Lon1);

		disty = deltaLat * m_per_deg_lat;
		distx = deltaLon * m_per_deg_lon;
		
		//dist_m = Math.sqrt (  Math.pow( deltaLat * m_per_deg_lat,2) + Math.pow( deltaLon * m_per_deg_lon , 2) );
		dist_m = Math.sqrt (  Math.pow( disty,2) + Math.pow( distx , 2) );
		return (float) dist_m;
	}
	
	void dist2pixels()
	{
		distx = (distx/1852.0)*(12./30)*(1/2.54)*dpi/rangescale; //12.3cm = 30nm
		disty = (disty/1852.0)*(12./30)*(1/2.54)*dpi/rangescale;		
	}
	
	//updates center of the map coord.
	void updateRefPos() {
		reflat = lat;
		reflon = lon;
	}
	
	@Override
	  public boolean onTouchEvent(MotionEvent event) {
	    float eventX = event.getX();
	    float eventY = event.getY();

	    switch (event.getAction()) {
	    	case MotionEvent.ACTION_DOWN:
	    			if (showmap == true){
	    				showmap = false;
	    			} else {
	    				showmap = true;
	    			}
	    			loadSkyvector();
	    	/*	if (eventX < centerx && eventY < centery) { // left Up
	    			if (plane.shownav == true) {
	    				plane.shownav = false;
	    			} else {
	    				plane.shownav = true;	    			
	    			}
	    		}*/
	    		Log.d("Saul","pressed touchscreen skyvector");
	    	    		
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
	
	
	
	//setters
	void setMode(int newmode)
	{
		mode = newmode;
	}
	
	void setRange(int newrange)
	{
		range = newrange;
	}
	
	void setLat(float newLat)
	{
		lat = newLat;
	}
	
	void setLon(float newLon)
	{
		lon = newLon;
	}
	
	void setRadhead(float head)
	{
		realheading = head;
	}
	

	void setLatwp0(float la)
	{
		latwp[0] = la;
	}
	
	void setLonwp0(float lo)
	{
		lonwp[0] = lo;
	}
	
	void setLatwp1(float la)
	{
		latwp[1] = la;
	}
	
	void setLonwp1(float lo)
	{
		lonwp[1] = lo;
	}
	
	void setLatwp2(float la)
	{
		latwp[2] = la;
	}
	
	void setLonwp2(float lo)
	{
		lonwp[2] = lo;
	}
	
	void setLatwp3(float la)
	{
		latwp[3] = la;
	}
	
	void setLonwp3(float lo)
	{
		lonwp[3] = lo;
	}
	
	void setLatwp4(float la)
	{
		latwp[4] = la;
	}
	
	void setLonwp4(float lo)
	{
		lonwp[4] = lo;
	}
	
	void setLatwp5(float la)
	{
		latwp[5] = la;
	}
	
	void setLonwp5(float lo)
	{
		lonwp[5] = lo;
	}
	
	void setLatwp6(float la)
	{
		latwp[6] = la;
	}
	
	void setLonwp6(float lo)
	{
		lonwp[6] = lo;
	}

	void setLatwp7(float la)
	{
		latwp[7] = la;
	}
	
	void setLonwp7(float lo)
	{
		lonwp[7] = lo;
	}
	
	void setLatwp8(float la)
	{
		latwp[8] = la;
	}
	
	void setLonwp8(float lo)
	{
		lonwp[8] = lo;
	}

	void setLatwp9(float la)
	{
		latwp[9] = la;
	}
	
	void setLonwp9(float lo)
	{
		lonwp[9] = lo;
	}
	
	void setLatwp10(float la)
	{
		latwp[10] = la;
	}
	
	void setLonwp10(float lo)
	{
		lonwp[10] = lo;
	}
	
	void setLatwp11(float la)
	{
		latwp[11] = la;
	}
	
	void setLonwp11(float lo)
	{
		lonwp[11] = lo;
	}
	
}
