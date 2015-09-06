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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

	public static final int SOCKET_TIMEOUT = 10000;
	public static final int NAV = 1;
	public static final int MAP = 2;
	//Debug constant
	private static final String MLOG = "PANELVIEW";
	//Dynamic view related
	LinearLayout llMain;
	MFD777View mMFD777;
	MFDG1000View mMFDG1000;
	myWebView myWeb;
	int displayFlag;
	NAVdb[] navdb;
	FIXdb[] fixdb;
	float displaydpi;
	//Communications related members
	private int udpPort;
	private UDPReceiver udpReceiver = null;
	//Selected plane
	private int selPlane;
	
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
		loadNAVView();
		

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		displaydpi = metrics.densityDpi;
		
		Log.d("Saul",String.format("DPI = %f",displaydpi));
				
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
			
			//mMFD777.plane.reflat = 0;
			//mMFD777.plane.reflon = 0;
			
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

	public void loadNAVView() {


		int btnGravity = Gravity.LEFT;
		//int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
		int wrapContent = LinearLayout.LayoutParams.FILL_PARENT;

		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				wrapContent, wrapContent);

		lParams.gravity = btnGravity;

		//Remove any previously created view
		llMain.removeAllViews();

		//Attach the custom view to a MFD777 object
		if (selPlane == Plane.G1000) {
			mMFDG1000 = new MFDG1000View(this, null);
			llMain.addView(mMFDG1000, lParams);

			mMFDG1000.setPlane(selPlane);
			mMFDG1000.plane.setdb(navdb, fixdb);
		} else {
			mMFD777 = new MFD777View(this, null);
			llMain.addView(mMFD777, lParams);

			mMFD777.setPlane(selPlane);
			mMFD777.plane.setdb(navdb, fixdb);
		}


	}

	public void loadWebView(MessageHandlerFGFS[] values) {


		int btnGravity = Gravity.LEFT;
		//int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
		int wrapContent = LinearLayout.LayoutParams.FILL_PARENT;

		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				wrapContent, wrapContent);

		lParams.gravity = btnGravity;

		//Remove any previously created view
		llMain.removeAllViews();

		//Attach the custom view to a webview object
		myWeb = new myWebView(this);
		llMain.addView(myWeb, lParams);
		myWeb.setWebViewClient(new WebViewClient());
		myWeb.getSettings().setJavaScriptEnabled(true);

		myWeb.dpi = displaydpi;
		myWeb.setMode(values[0].getInt(B777Protocol.MODE));
		myWeb.setRange(values[0].getInt(B777Protocol.RANGE));
		myWeb.setLat(values[0].getFloat(B777Protocol.LATITUDE));
		myWeb.setLon(values[0].getFloat(B777Protocol.LONGITUDE));
		myWeb.updateRefPos(); //updates center of the map coord.
		myWeb.updateRange();


	}
		
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

				int mode = (values[0].getInt(G1000Protocol.MODE));

				//Default is 777, other planes need to change
				if (mMFD777.planeType == MFD777View.A330) {
					if (mode == 4 ) { //PLAN is 4 in 333 whereas PLN is 3 in 777
						mode = 3;
					} else if(mode == 3) { // This will avoid that the Skyvector is triggered if ARC
						mode = 2;
					}
				}

				if (selPlane == MainActivity.G1000) {
					mMFDG1000.updateView(values);
				} else if (displayFlag == MAP) { //Currenly Skyvector map is on display

					if (mode < 3) { // Change to NAV
						displayFlag = NAV;
						loadNAVView();
						return;
					}

					myWeb.updateView(values);

				} else if (displayFlag == NAV){

					if (mode == 3) { // Change to MAP
						displayFlag = MAP;
						loadWebView(values);
						return;
					}
					mMFD777.updateView(values);
				}
			}
		}
}
