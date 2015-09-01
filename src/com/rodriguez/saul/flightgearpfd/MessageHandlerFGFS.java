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
    
    NOTE: this class is a modified version of GPL code available at https://github.com/Juanvvc/FlightGearMap
 * 
 */


package com.rodriguez.saul.flightgearpfd;

import java.util.Date;

import android.util.Log;

public class MessageHandlerFGFS {
	String[] data;
	String[] outData;
	private Date date = new Date();
	//private MovingAverage[] averages;
	
	public MessageHandlerFGFS() 
	{
		data = null;	
		
	}
	
	public void parse(final String input) 
	{
		// strip string with new line
		String realInput = input.substring(0, input.indexOf("\n"));
		data = realInput.split(":");

		date = new Date();

		// check that we have the desired number of parameters
		// just read the last data. If throws IndexOutOfBounds, the
		// other extreme is sending wrong data
				
		//getFloat(MAXSPEED);
	}
	
	public int getInt(int i) 
	{
		if (data == null) {
			return 0;
		}
		return Integer.valueOf(data[i]);
	}

	public float getFloat(int i) 
	{
		if (data == null) {
			return 0;
		}
		//MovingAverage ma = this.averages[i];
		//if (ma==null) {
		//return Float.valueOf(data[i]);
		//} else {
		//return ma.getData(Float.valueOf(data[i]));
		//}
		return Float.valueOf(data[i]);
	}

	public String getString(int i) 
	{
		if (data == null) {
			return "";
		}
//		Log.d("Saul getstring index", String.format("%d", i));
//		Log.d("Saul getstring String: ",data[i]);
		return data[i];
	}

	public boolean getBool(int i)
	{
		if (data == null) {
			return false;
		}
		return data[i].equals("1");
	}

	public Date getDate() 
	{
		return date;
	}

	public boolean hasData() 
	{
		return data != null;
	}
			
}
