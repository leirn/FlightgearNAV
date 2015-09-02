package com.rodriguez.saul.flightgearpfd;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class G1000Panels
{
        int height, width;
        
        Bitmap topBar;
        Bitmap softKeys;
        Bitmap activeSoftKey;
        Bitmap engine1;
        Bitmap engine2;
        Bitmap engine3;
        
        static float TOPBAR_HEIGHT = 0.073;
        static float SOFTKEYS_HEIGHT = 0.034;
        static float EIS_HEIGHT = 0.893;
        
        static float EIS_WIDTH = 0.147;
        
        public G1000Panels (int width, int height)
        {
                reset(width, height);
        }
        
        public reset(int width, int height)
        {
                this.height = height;
                this.width = width;
                topBar = null;
                softKeys = null;
                activeSoftKey = null;
                engine1 = null;
                engine2 = null;
                engine3 = null;
        
        }
        
        Bitmap getTopBar()
        {
                if(topBar != null)
                        return topBar;
                
                tbHeight = TOPBAR_HEIGHT * height;
                topBar = Bitmap.createBitmap(Color.TRANSPARENT , width, tbHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(topBar);
                
                paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float)(1);
		paint.setStyle(Style.STROKE);
		//Bottom line
		canvas.drawLine(0, 0.1 * height, width, 0.1 * height, paint);
		//Mid height line
		canvas.drawLine(0.25 * width,tbHeight / 2, 0.75 * width, tbHeight / 2, paint);
		// Vertical lines
		paint.setStrokeWidth((float)(2);
		canvas.drawLine(0.25 * width, 0, 0.25 * width, tbHeight, paint);
		canvas.drawLine(0.75 * width, 0, 0.75 * width, tbHeight, paint);

                return topBar;
        }
        
        Bitmap getSoftKeys()
        {
                if(softKeys != null)
                        return softKeys;
                
                softHeight = SOFTKEYS_HEIGHT * height;
                softKeys = Bitmap.createBitmap(Color.TRANSPARENT , width, softHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(softKeys);
                
                paint.setColor(Color.WHITE);
		paint.setStrokeWidth((float)(2);
		paint.setStyle(Style.STROKE);
		canvas.drawLine(0, 0, width, 0, paint);
		for(int i = 0;i < 11; i++) {
                        paint.setColor(Color.BLACK);
			canvas.drawLine(i / 12 * width, 0, i / 12 * width, softHeight, paint);
                        paint.setColor(Color.WHITE);
			canvas.drawLine((i / 12 * width) + 1, 0, (i / 12 * width) + 1, softHeight, paint);
		}

                return softKeys;
        }
        
        Bitmap getActiveSoftKey()
        {
                if(activeSoftKey != null)
                        return activeSoftKey;
                
                softHeight = SOFTKEYS_HEIGHT * height;
                softWidth = width / 12;
                activeSoftKey = Bitmap.createBitmap(Color.TRANSPARENT , softWidth, softHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(activeSoftKey);
                
                return activeSoftKey;
        }
        
        Bitmap getEngine1()
        {
                if(engine1 != null)
                        return engine1;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine1);
                
                return engine1;
        }
        
        Bitmap getEngine2()
        {
                if(engine2 != null)
                        return engine2;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine1);
                
                return engine2;
        }
        
        Bitmap getEngine3()
        {
                if(engine3 != null)
                        return engine3;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine3);
                
                return engine3;
        }
}
