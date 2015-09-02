package com.rodriguez.saul.flightgearpfd;


//import com.example.com.readnav.test.NAVdb;
import android.util.Log;

public class PlaneG1000 extends Plane {
	//NAV database
	NAVdb navdb;	
	FIXdb fixdb;
	int sync;
	
	int pfdormfd;
	
	float g1000scaleFactor;
	float leftload, rightload, leftload, rightrpm;
	
	
	public PlaneG1000(Context context) {
		
		mContext = context;
		
		//Default plane
		planeType = G1000;
		
		
		//Initialize all the parameters
		scaleFactor = (float)1.0;
		g1000scaleFactor = (float)1.0;
		
		heading = 330;
		apheading = 10;
		
		//VORL
		switchvorl = 0;
				
		vorlid = "QMS";
		vorldme = 9000;
		vorldmeinrange = true; // is DME in range ?
		vorlinrange = true;
		vorlfreq = (float)114.8;
		vorldirection = 10;
		
		//VORR
		switchvorr = 1;
		
		vorrid = "QMS";
		vorrdme = 9000;
		vorrdmeinrange = true; // is DME in range ?
		vorrinrange = true; // is nav in range ?
		vorrfreq = (float)114.8;
		vorrdirection = 20;
		
		//ADFL
		adflid = "ZUI";
		adflinrange = true;
		adflfreq = 290;
		adfldirection = (float)80.3;
				
		//ADFL
		adfrid = "ZUI";
		adfrinrange = true;
		adfrfreq = 290;
		adfrdirection = (float)80.3;
		
		//Radial NAV1
		radial = 330;
		realheading = 0;
		radialdef = 0;
		gsdef = 0;
		
		//Encoder Modes
		mode = 0;
		range = 10;
		modebut = false; //false == arc, true == circle
		
		//Speed
		truespeed = 0;
		groundspeed = 0;
		
		//windspeed
		windspeed = 0;
		windheading = 0;

		//Position
		lat = 0;
		lon = 0;
		
		reflat = 0;
		reflon = 0;
			
		//route
		latwp = new float[ROUTESIZE];
		lonwp = new float[ROUTESIZE];
		
		currentwp = "None";
		numwp = 0;
		
		//EIS
		leftload = 0;
		rightload = 0;
		leftload = 0;
		rightrpm = 0;
		
		leftoiltemp = 0;
		rightoiltemp = 0;
		leftoilpres = 0;
		rightoilpres = 0;
		
		pfdormfd = MFDG1000View.MFD;
	}
	
	public updateView(values) {
		//update NAV
		heading = values[0].getFloat(G1000Protocol.HEADING);
		apheading = values[0].getInt(G1000Protocol.APHEADING);
	
		//VORL
		vorlid = values[0].getString(G1000Protocol.VORLID);
		vorldme = values[0].getFloat(G1000Protocol.VORLDME);
		vorldmeinrange = values[0].getBool(G1000Protocol.VORLDMEINRANGE);
		vorlinrange = values[0].getBool(G1000Protocol.VORLINRANGE);
		vorlfreq = values[0].getFloat(G1000Protocol.VORLFREQ);
	
		switchvorl = values[0].getInt(G1000Protocol.SWITCHLEFT);
		vorldirectionvalues[0].getFloat(G1000Protocol.VORLDIR);
	
		//VORRL
		vorrid = values[0].getString(G1000Protocol.VORRID);
		vorldme = values[0].getFloat(G1000Protocol.VORRDME);
		vorrdmeinrange = values[0].getBool(G1000Protocol.VORRDMEINRANGE);
		vorrinrange = values[0].getBool(G1000Protocol.VORRINRANGE);
		vorrfreq = values[0].getFloat(G1000Protocol.VORRFREQ);
	
		switchvorr = values[0].getInt(G1000Protocol.SWITCHRIGHT);
		vorrdirectionvalues[0].getFloat(G1000Protocol.VORRDIR);
	
		//ADFL
		adflid = values[0].getString(G1000Protocol.ADFLID);
		adflinrange = values[0].getBool(G1000Protocol.ADFLINRANGE);
		adflfreq = values[0].getInt(G1000Protocol.ADFLFREQ);
		adfldirection = values[0].getFloat(G1000Protocol.ADFLDIR);
	
		//ADFR
		adfrid = values[0].getString(G1000Protocol.ADFRID);
		adfrinrange = values[0].getBool(G1000Protocol.ADFRINRANGE);
		adfrfreq = values[0].getInt(G1000Protocol.ADFRFREQ);
		adfrdirection = values[0].getFloat(G1000Protocol.ADFRDIR);
	
		//RADIAL NAV1
		radial = values[0].getFloat(G1000Protocol.RADIALDIR);
		realheading = values[0].getFloat(G1000Protocol.RADIALHEAD);
		radialdef = values[0].getFloat(G1000Protocol.RADIALDEF);
		gsdef = values[0].getFloat(G1000Protocol.GSDEF);
	
		//Modes
		mode = values[0].getInt(G1000Protocol.MODE);
		range = values[0].getInt(G1000Protocol.RANGE);
		modebut = values[0].getBool(G1000Protocol.MODEBUT);
	
		//Speed
		truespeed = values[0].getFloat(G1000Protocol.TRUESPEED);
		groundspeed = values[0].getFloat(G1000Protocol.GROUNDSPEED);
		windspeed = values[0].getFloat(G1000Protocol.WINDHEADING);
		windheading = values[0].getFloat(G1000Protocol.WINDSPEED);
	
		//Position
		lat = values[0].getFloat(G1000Protocol.LATITUDE);
		lon = values[0].getFloat(G1000Protocol.LONGITUDE);
		
		//route
		latwp[0] = values[0].getFloat(G1000Protocol.LATWP1);
		lonwp[0] = values[0].getFloat(G1000Protocol.LONWP1);
		latwp[1] = values[0].getFloat(G1000Protocol.LATWP2);
		lonwp[1] = values[0].getFloat(G1000Protocol.LONWP2);
		latwp[2] = values[0].getFloat(G1000Protocol.LATWP3);
		lonwp[2] = values[0].getFloat(G1000Protocol.LONWP3);
		latwp[3] = values[0].getFloat(G1000Protocol.LATWP4);
		lonwp[3] = values[0].getFloat(G1000Protocol.LONWP4);
		latwp[4] = values[0].getFloat(G1000Protocol.LATWP5);
		lonwp[4] = values[0].getFloat(G1000Protocol.LONWP5);
		latwp[5] = values[0].getFloat(G1000Protocol.LATWP6);
		lonwp[5] = values[0].getFloat(G1000Protocol.LONWP6);
		latwp[6] = values[0].getFloat(G1000Protocol.LATWP7);
		lonwp[6] = values[0].getFloat(G1000Protocol.LONWP7);
		latwp[7] = values[0].getFloat(G1000Protocol.LATWP8);
		lonwp[7] = values[0].getFloat(G1000Protocol.LONWP8);
		latwp[8] = values[0].getFloat(G1000Protocol.LATWP9);
		lonwp[8] = values[0].getFloat(G1000Protocol.LONWP9);
		latwp[9] = values[0].getFloat(G1000Protocol.LATWP10);
		lonwp[9] = values[0].getFloat(G1000Protocol.LONWP10);
		latwp[10] = values[0].getFloat(G1000Protocol.LATWP11);
		lonwp[10] = values[0].getFloat(G1000Protocol.LONWP11);
		latwp[11] = values[0].getFloat(G1000Protocol.LATWP12);
		lonwp[11] = values[0].getFloat(G1000Protocol.LONWP12);
		
		currentwp = values[0].getString(G1000Protocol.CURRENTWP);
		numwp = values[0].getInt(G1000Protocol.NUMWP);
		
		// G1000 EIS
		leftload = values[0].getFloat(G1000Protocol.LEFT_LOAD);
		rightload = values[0].getFloat(G1000Protocol.RIGHT_LOAD);
		leftload = values[0].getFloat(G1000Protocol.LEFT_RPM);
		rightrpm = values[0].getFloat(G1000Protocol.RIGHT_RPM);
		
		leftoiltemp = values[0].getFloat(G1000Protocol.LEFT_OIL_TEMP);
		rightoiltemp = values[0].getFloat(G1000Protocol.RIGHT_OIL_TEMP);
		leftoilpres = values[0].getFloat(G1000Protocol.LEFT_OIL_PRES);
		rightoilpres = values[0].getFloat(G1000Protocol.RIGHT_OIL_PRES);
		
		//Check if the database needs update
		if (checkUpdateDBNeeded()) {
			updateDB();
		}
	}
	
	
	public void setdb(NAVdb[] nav, FIXdb[] fix)
	{
		navdb = nav[0];
		fixdb = fix[0];
	}
	
	public void readDB() {
		// Read Database
		//sync = 1;
		navdb = new NAVdb(mContext);
		navdb.readDB(); // Read nav.csv file from assets folder
				
		fixdb = new FIXdb(mContext);
		fixdb.readDB();
		//sync = 0;
	}
	
	public boolean checkUpdateDBNeeded()
	{
		return (navdb.calcDistance(lat, lon, reflat, reflon) > 50000);
	}
	
	public void updateDB()
	{
		//Calculate objects nearby
		reflat = lat;
		reflon = lon;
		
		navdb.calcQuickcoeff(lat);
		navdb.selectNear(lat, lon);
		
		fixdb.calcQuickcoeff(lat);			
		fixdb.selectNear(lat, lon);
	}
}
