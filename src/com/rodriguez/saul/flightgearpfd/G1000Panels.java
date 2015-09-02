package com.rodriguez.saul.flightgearpfd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public class G1000Panels
{
        int height, width;
        
        Bitmap topBar;
        Bitmap softKeys;
        Bitmap activeSoftKey;
        Bitmap engine1, engine2, engine3;
        Bitmap leftTriangleGauge, rightTriangleGauge, leftTriangleGaugeAmber, rightTriangleGaugeAmber, leftTriangleGaugeRed, rightTriangleGaugeRed;
        
        static float TOPBAR_HEIGHT = 0.073;
        static float SOFTKEYS_HEIGHT = 0.034;
        static float EIS_HEIGHT = 0.893;
        
        static float EIS_WIDTH = 0.147;
        static float EIS_RATIO = 0.219;
        
        static float LEFT_TRIANGLE_SHIFT_Y = 0.05;
        static float RIGHT_TRIANGLE_SHIFT_Y = -0.05;
        
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
        
        Bitmap getLeftTriangleGauge()
        {
        	if(leftTriangleGauge != null)
                        return leftTriangleGauge;
        	float eisHeight = height * EIS_HEIGHT;
                leftTriangleGauge = Bitmap.createBitmap(
                	Color.GREEN , 
                	0.3493 * height * EIS_HEIGHT, 
                	0,02766 * height * EIS_HEIGHT, 
                	Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(leftTriangleGauge);
                
                Point a = new Point(0, 0);
		Point b = new Point(0.3493 * eisHeight, 0);
		Point c = new Point(0.01747* eisHeight, 0.02766 * eisHeight);
		
		Path path = new Path();
		path.setFillType(FillType.EVEN_ODD);
		path.lineTo(b.x, b.y);
		path.lineTo(c.x, c.y);
		path.lineTo(a.x, a.y);
		path.close();
		
                paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
		
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawPath(path, paint);
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                
                paint.setTextAlign(Paint.Align CENTER);
                canvas.drawText("L", 0.01747* eisHeight, 0.01383 * eisHeight);
                
                return leftTriangleGauge;
        }
        
        Bitmap getRightTriangleGauge()
        {
        	if(rightTriangleGauge != null)
                        return rightTriangleGauge;
        	float eisHeight = height * EIS_HEIGHT;
                rightTriangleGauge = Bitmap.createBitmap(
                	Color.BLUE , 
                	0.3493 * height * EIS_HEIGHT, 
                	0,02766 * height * EIS_HEIGHT, 
                	Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(rightTriangleGauge);
                
                Point a = new Point(0, 0.02766 * eisHeight);
		Point b = new Point(0.3493 * eisHeight, 0.02766 * eisHeight);
		Point c = new Point(0.01747* eisHeight, 0);
		
		Path path = new Path();
		path.setFillType(FillType.EVEN_ODD);
		path.lineTo(b.x, b.y);
		path.lineTo(c.x, c.y);
		path.lineTo(a.x, a.y);
		path.close();
		
                paint.setStrokeWidth(1);
		paint.setAntiAlias(true);
		
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawPath(path, paint);
		
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(path, paint);
                
                paint.setTextAlign(Paint.Align CENTER);
                canvas.drawText("R", 0.01747* eisHeight, 0.01383 * eisHeight);
                return rightTriangleGauge;
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
        
        Bitmap getEISEngine()
        {
                if(engine1 != null)
                        return engine1;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine1);
                
                return engine1;
        }
        
        Bitmap getEISSystem()
        {
                if(engine2 != null)
                        return engine2;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine1);
                
                return engine2;
        }
        
        Bitmap getEISFuel()
        {
                if(engine3 != null)
                        return engine3;
                
                engineHeight = EIS_HEIGHT * height;
                engineWidth = EIS_WIDTH * width;
                engine1 = Bitmap.createBitmap(Color.TRANSPARENT , engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(engine3);
                
                return engine3;
        }
        
        public class EisEngine {
        	// ratio compared to EIS Panel size
        	static float OIL_TEMP_X = 0.1;
        	static float OIL_TEMP_Y = 0.5;
        	static float OIL_TEMP_WIDTH = 0.8;
        	static float OIL_TEMP_MAX = 120;
        	
        	static float OIL_PRES_X = 0.1;
        	static float OIL_PRES_Y = 0.6;
        	static float OIL_PRES_WIDTH = 0.8;
        	static float OIL_TEMP_MAX = 120;
        }
        public class EisSystem {
        }
        public class EisFuel {
        }
        }
}
