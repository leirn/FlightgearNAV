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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.rodriguez.saul.flightgearpfd.R;

public class PanelView extends Activity {

	//Communications related members
	private int udpPort;	
	private UDPReceiver udpReceiver = null;
	public static final int SOCKET_TIMEOUT = 10000;
	
	//Selected plane
	private int selPlane;
	
	//Dynamic view related
	LinearLayout llMain;
	MFD777View mMFD777;
	myWebView myWeb;
	
	public static final int NAV = 1;
	public static final int MAP = 2;
	int displayFlag;
	
	NAVdb[] navdb;	
	FIXdb[] fixdb;
	
	//Debug constant
	private static final String MLOG = "PANELVIEW";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Orientation allways landscape
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		//Set Full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_panel_view);
		
		llMain = (LinearLayout) findViewById(R.id.ll);
				
		// Get the configuration via intent
		Intent intent = getIntent();				
		udpPort = intent.getIntExtra(MainActivity.MESS_PORT, 5502);		
		selPlane = intent.getIntExtra(MainActivity.SELECTED_PLANE, 0);
		
		navdb = new NAVdb[1];
		navdb[0] = new NAVdb(this);
		navdb[0].readDB();
		
		fixdb = new FIXdb[1];
		fixdb[0] = new FIXdb(this);
		fixdb[0].readDB();
		
		displayFlag = NAV;
		loadView();
				
		Log.d(MLOG,"Port: " + String.format("%d", udpPort));
	}
	
	 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
			if (udpReceiver != null) {
			     udpReceiver.cancel(true);
			     udpReceiver = null;
			}
		}


		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			Log.d(MLOG, "Pausing threads");
			
			if (udpReceiver != null) {
				 
			     udpReceiver.cancel(true); 
			     udpReceiver = null;
			}
			
			super.onPause();
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
			//Read DB
			//mMFD777.plane.readDB();
			
			Log.d(MLOG,"Starting threads");
			
			mMFD777.plane.reflat = 0;
			mMFD777.plane.reflon = 0;
			
			if (udpReceiver == null) {
			     udpReceiver = (UDPReceiver) new UDPReceiver().execute(udpPort);
			}
					
			Toast toast= Toast.makeText(this,"Connecting..." ,Toast.LENGTH_LONG);
			toast.show();
					
		}
		
		/*
		 *  The UDPReceiver AsyncTask receiver uses 1st parameter (sent to doInBackground) for the UDP port number (integer).
		 *  The 2nd parameter is an object of the helper class MessageHandlerFGFS (sent to onProgressUpdate). The 3rd parameter is 
		 *  a String containing the result of the doInBackground task ( it is supposed to be sent to a method onPostExecute, but for simplicity it is 
		 *  not used in this example).
		 * */
		
		private class UDPReceiver extends AsyncTask<Integer, MessageHandlerFGFS, String> {

			/*
			 *  doInBackground() opens a socket and receives the data from fgfs. It will wait SOCKET_TIMOUT ms before throwing an
			 *  exception. 
			 *  The incoming data is parsed by the parse() method where the buffer is split in multiple Strings. Each String contains 
			 *  an updated parameter. After parsing the buffer, the onProgressUpdate is called by using this.pubishProgress(pd). 
			 * */
			@Override
			protected String doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				DatagramSocket socket;
				byte[] buf = new byte[512];
				
				boolean canceled = false;
				String msg = null;
				MessageHandlerFGFS pd = new MessageHandlerFGFS();
				
				try {
						socket = new DatagramSocket(params[0]);
						socket.setSoTimeout(SOCKET_TIMEOUT);
				} catch (SocketException e) {
						Log.d(MLOG, e.toString());
						return e.toString();
				}
				
				Log.d(MLOG,"UDP Thread started and liseting on port: " + params[0]);
										
				while (!canceled) {
					DatagramPacket p = new DatagramPacket(buf, buf.length);
					
					try {
						socket.receive(p);
						pd.parse(new String(p.getData()));
						
						//Log.d(MLOG,"pd parsed");
						
						//Add here a call to progress with the pd as param
						this.publishProgress(pd);
						//Check if the asynctask was cancelled somewhere else 
						canceled = this.isCancelled();
						
					} catch (SocketTimeoutException e) {	
						Log.d(MLOG,"Socket Timeout Exception");
						canceled = true;
					} catch (Exception e) {
						Log.d(MLOG,"Socket exception");
						canceled = true;
					}
					
				}
				
				socket.close();
				
				Log.d(MLOG, "UDP thread finished");
				
				return msg;
			}

			/*
			 * onProgressUpdate() updates the Activity fields
			 * */
			@Override
			protected void onProgressUpdate(MessageHandlerFGFS... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				
				int mode = (values[0].getInt(MessageHandlerFGFS.MODE));
				
				if (displayFlag == MAP) {
					if (mode < 3) { // Change to MAP
						displayFlag = NAV;
						loadView();
						return;
					}
					
					
				} else if (displayFlag == NAV){
					
					if (mode == 3) { // Change to MAP
						displayFlag = MAP;
						loadView();
						return;
					}
				
					mMFD777.setHeading(values[0].getFloat(MessageHandlerFGFS.HEADING));
					mMFD777.setAPheading(values[0].getInt(MessageHandlerFGFS.APHEADING));
				
					//VORL
					mMFD777.setNAV1ID(values[0].getString(MessageHandlerFGFS.VORLID));
					mMFD777.setNAV1DME(values[0].getFloat(MessageHandlerFGFS.VORLDME));
					mMFD777.setNAV1DMEinrange(values[0].getBool(MessageHandlerFGFS.VORLDMEINRANGE));
					mMFD777.setNAV1inrange(values[0].getBool(MessageHandlerFGFS.VORLINRANGE));
					mMFD777.setNAV1freq(values[0].getFloat(MessageHandlerFGFS.VORLFREQ));
				
					mMFD777.setSwitchleft(values[0].getInt(MessageHandlerFGFS.SWITCHLEFT));
					mMFD777.setNAV1dir(values[0].getFloat(MessageHandlerFGFS.VORLDIR));
				
					//VORRL
					mMFD777.setNAV2ID(values[0].getString(MessageHandlerFGFS.VORRID));
					mMFD777.setNAV2DME(values[0].getFloat(MessageHandlerFGFS.VORRDME));
					mMFD777.setNAV2DMEinrange(values[0].getBool(MessageHandlerFGFS.VORRDMEINRANGE));
					mMFD777.setNAV2inrange(values[0].getBool(MessageHandlerFGFS.VORRINRANGE));
					mMFD777.setNAV2freq(values[0].getFloat(MessageHandlerFGFS.VORRFREQ));
				
					mMFD777.setSwitchright(values[0].getInt(MessageHandlerFGFS.SWITCHRIGHT));
					mMFD777.setNAV2dir(values[0].getFloat(MessageHandlerFGFS.VORRDIR));
				
					//ADFL
					mMFD777.setADF1ID(values[0].getString(MessageHandlerFGFS.ADFLID));
					mMFD777.setADF1inrange(values[0].getBool(MessageHandlerFGFS.ADFLINRANGE));
					mMFD777.setADF1freq(values[0].getInt(MessageHandlerFGFS.ADFLFREQ));
					mMFD777.setADF1dir(values[0].getFloat(MessageHandlerFGFS.ADFLDIR));
				
					//ADFR
					mMFD777.setADFrID(values[0].getString(MessageHandlerFGFS.ADFRID));
					mMFD777.setADFrinrange(values[0].getBool(MessageHandlerFGFS.ADFRINRANGE));
					mMFD777.setADFrfreq(values[0].getInt(MessageHandlerFGFS.ADFRFREQ));
					mMFD777.setADFrdir(values[0].getFloat(MessageHandlerFGFS.ADFRDIR));
				
					//RADIAL NAV1
					mMFD777.setRaddir(values[0].getFloat(MessageHandlerFGFS.RADIALDIR));
					mMFD777.setRadhead(values[0].getFloat(MessageHandlerFGFS.RADIALHEAD));
					mMFD777.setRaddef(values[0].getFloat(MessageHandlerFGFS.RADIALDEF));
					mMFD777.setGSdef(values[0].getFloat(MessageHandlerFGFS.GSDEF));
				
					//Modes
					mMFD777.setMode(values[0].getInt(MessageHandlerFGFS.MODE));
					mMFD777.setRange(values[0].getInt(MessageHandlerFGFS.RANGE));
					mMFD777.setModebut(values[0].getBool(MessageHandlerFGFS.MODEBUT));
				
					//Speed
					mMFD777.setTruespeed(values[0].getFloat(MessageHandlerFGFS.TRUESPEED));
					mMFD777.setGroundpeed(values[0].getFloat(MessageHandlerFGFS.GROUNDSPEED));
					mMFD777.setWindhead(values[0].getFloat(MessageHandlerFGFS.WINDHEADING));
					mMFD777.setWindspeed(values[0].getFloat(MessageHandlerFGFS.WINDSPEED));
				
					//Position
					mMFD777.setLat(values[0].getFloat(MessageHandlerFGFS.LATITUDE));
					mMFD777.setLon(values[0].getFloat(MessageHandlerFGFS.LONGITUDE));
				
					//Check if the database needs update
					if (mMFD777.plane.checkUpdateDBNeeded()) {
						mMFD777.plane.updateDB();
					} else {
						mMFD777.draw();
					}
				
				}
				
				
			}		
	    	
	    }

		public void loadView() {
			
			
			int btnGravity = Gravity.LEFT;
			//int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
			int wrapContent = LinearLayout.LayoutParams.FILL_PARENT;
			
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
			          wrapContent, wrapContent);
			
			lParams.gravity = btnGravity;
			
			//Remove any previously created view
			llMain.removeAllViews();
			
			if (displayFlag == NAV)	{
				//Attach the custom view to a MFD777 object
				
				mMFD777 = new MFD777View(this,null);
				llMain.addView(mMFD777,lParams);
				
				mMFD777.setPlane(selPlane);
				mMFD777.plane.setdb(navdb,fixdb);
			} else if (displayFlag == MAP) {
				myWeb = new myWebView(this);
				llMain.addView(myWeb,lParams);
				myWeb.setWebViewClient(new WebViewClient());
				myWeb.getSettings().setJavaScriptEnabled(true);
				myWeb.loadUrl("http://skyvector.com/?ll=-0.2393181630572534,-78.46655272806412&chart=301&zoom=1");

			}
			
		}
}
