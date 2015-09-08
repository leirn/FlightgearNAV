package com.rodriguez.saul.flightgearpfd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.LinearGradient;

public class G1000Panels
{
    static final double TOPBAR_HEIGHT = 0.073;
    static final double SOFTKEYS_HEIGHT = 0.034;
    static final double EIS_HEIGHT = 0.893;
    static final double EIS_WIDTH = 0.147;
    static final double EIS_RATIO = 0.21;
    static final double LEFT_TRIANGLE_SHIFT_Y = -0.02766;
    static final double RIGHT_TRIANGLE_SHIFT_Y = 0;
    static final double HSI_SIZE = 0.42; // Taille par rapport à la zone principale, pas au canvas complet
    public double hsisize;
    public double horsize;
    int height, width;
    Paint paint;
    private Bitmap topBar;
    private Bitmap softKeys;
    private Bitmap activeSoftKey;
    private Bitmap engine1, engine2, engine3;
    private Bitmap leftTriangleGauge, rightTriangleGauge, leftTriangleGaugeAmber, rightTriangleGaugeAmber, leftTriangleGaugeRed, rightTriangleGaugeRed;
    private Bitmap horizon, hsi, pfdmask;
    
    public HorEISGauge oilTemp, oilpres, coolant, fueltemp, fuelqty;

    public G1000Panels(int width, int height) {
        paint = new Paint();
        reset(width, height);
    }

    public void reset(int width, int height) {
        this.height = height;
        this.width = width;
        topBar = null;
        softKeys = null;
        activeSoftKey = null;
        engine1 = null;
        engine2 = null;
        engine3 = null;
        horizon = null;
        hsi = null;
        oiltemp = null;
        oilpres = null;
        coolant = null;
        fueltemp = null;
        fuelqty = null;
    }

    Bitmap getPFDMask() {
        if (pfdmask != null)
            return pfdmask;

        int h = (int) (EIS_HEIGHT * height);

        pfdmask = Bitmap.createBitmap(
                width,
                h,
                Bitmap.Config.ARGB_8888);
        pfdmask.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(pfdmask);

        paint.setStrokeWidth(2);

        //Panels for speed and altitude
        paint.setColor(Color.argb(128, 128, 128, 128));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect((float) ((0.45 - 0.453 / 2) * width), (float) ((0.33 - 0.25) * h), (float) ((0.45 - 0.453 / 2 - 0.085) * width), (float) ((0.33 + 0.25) * h), paint);
        canvas.drawRect((float) ((0.45 + 0.453 / 2) * width), (float) ((0.33 - 0.25) * h), (float) ((0.45 + 0.453 / 2 + 0.102) * width), (float) ((0.33 + 0.25) * h), paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect((float) ((0.45 - 0.453 / 2) * width), (float) ((0.33 - 0.25) * h), (float) ((0.45 - 0.453 / 2 - 0.085) * width), (float) ((0.33 + 0.25) * h), paint);
        canvas.drawRect((float) ((0.45 + 0.453 / 2) * width), (float) ((0.33 - 0.25) * h), (float) ((0.45 + 0.453 / 2 + 0.102) * width), (float) ((0.33 + 0.25) * h), paint);

        //Fixed markers around HSI
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(0)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(0)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(0)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(0)),
                paint);
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(Math.PI)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(Math.PI)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(Math.PI)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(Math.PI)),
                paint);
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(Math.PI / 6)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(Math.PI / 6)),
                paint);
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(-Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(-Math.PI / 6)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(-Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(-Math.PI / 6)),
                paint);
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(Math.PI + Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(Math.PI + Math.PI / 6)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(Math.PI + Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(Math.PI + Math.PI / 6)),
                paint);
        canvas.drawLine(
                (float) (0.45 * width + 0.43 / 2 * h * Math.cos(Math.PI - Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.43 / 2 * h * Math.sin(Math.PI - Math.PI / 6)),
                (float) (0.45 * width + 0.475 / 2 * h * Math.cos(Math.PI - Math.PI / 6)),
                (float) ((0.33 + 0.44) * h + 0.475 / 2 * h * Math.sin(Math.PI - Math.PI / 6)),
                paint);
        return pfdmask;
    }

    Bitmap getHorizon() {
        if (horizon != null)
            return horizon;

        horsize = (int) (1.3 * Math.max(width, height));
        horizon = Bitmap.createBitmap(
                (int) horsize,
                (int) horsize,
                Bitmap.Config.ARGB_8888);
        horizon.eraseColor(Color.rgb(73, 67, 45));
        Canvas canvas = new Canvas(horizon);

        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

        Shader shader = new LinearGradient(0, 0, 0, (int) (horsize / 3), Color.rgb(28, 72, 152), Color.rgb(116, 139, 255), Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        canvas.drawRect(new RectF(0, 0, (int) horsize, (int) (horsize / 3)), paint);
        paint.reset();
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);


        canvas.drawLine(0, (int) (horsize / 3), (int) horsize, (int) (horsize / 3), paint);

        double radius = 0.275 * height * EIS_HEIGHT;
        canvas.drawArc(
                (int) ((horsize / 2) - radius),
                (int) ((horsize / 3) - radius),
                (int) ((horsize / 2) + radius),
                (int) ((horsize / 3) + radius)
                , 210, 120, false, paint);

        //Draw 20° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.05 * width), (float) (horsize / 3 - 0.2 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.05 * width), (float) (horsize / 3 - 0.2 * EIS_HEIGHT),
                paint);
        //Draw 15° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.03 * width), (int) (horsize / 3 - 0.15 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.03 * width), (int) (horsize / 3 - 0.15 * EIS_HEIGHT),
                paint);
        //Draw 10° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.05 * width), (int) (horsize / 3 - 0.1 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.05 * width), (int) (horsize / 3 - 0.1 * EIS_HEIGHT),
                paint);
        //Draw 5° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.03 * width), (int) (horsize / 3 - 0.05 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.03 * width), (int) (horsize / 3 - 0.05 * EIS_HEIGHT),
                paint);
        //Draw -5° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.03 * width), (int) (horsize / 3 + 0.05 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.03 * width), (int) (horsize / 3 + 0.05 * EIS_HEIGHT),
                paint);
        //Draw -10° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.05 * width), (int) (horsize / 3 + 0.1 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.05 * width), (int) (horsize / 3 + 0.1 * EIS_HEIGHT),
                paint);
        //Draw -15° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.03 * width), (int) (horsize / 3 + 0.15 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.03 * width), (int) (horsize / 3 + 0.15 * EIS_HEIGHT),
                paint);
        //Draw -20° line
        canvas.drawLine(
                (float) (horsize / 2 - 0.05 * width), (int) (horsize / 3 + 0.2 * EIS_HEIGHT),
                (float) (horsize / 2 + 0.05 * width), (int) (horsize / 3 + 0.2 * EIS_HEIGHT),
                paint);

        return horizon;
    }

    Bitmap getHSI() {
        if (hsi != null)
            return hsi;
        Matrix m = new Matrix();
        m.reset();
        hsisize = (1 - SOFTKEYS_HEIGHT - TOPBAR_HEIGHT) * HSI_SIZE * height;
        hsi = Bitmap.createBitmap(
                (int) hsisize,
                (int) hsisize,
                Bitmap.Config.ARGB_8888);
        hsi.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(hsi);
        paint.setStrokeWidth(2);

        paint.setColor(Color.argb(128, 128, 128, 128));


        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle((float) (hsisize / 2), (float) (hsisize / 2), (float) (hsisize / 2), paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 36; i++) {
            canvas.drawLine(
                    (float) (hsisize / 2 * (1 + 0.95 * Math.cos(Math.PI * (2 * i + 1) / 36))),
                    (float) (hsisize / 2 * (1 + 0.95 * Math.sin(Math.PI * (2 * i + 1) / 36))),
                    (float) (hsisize / 2 * (1 + Math.cos(Math.PI * (2 * i + 1) / 36))),
                    (float) (hsisize / 2 * (1 + Math.sin(Math.PI * (2 * i + 1) / 36))),
                    paint);

            canvas.drawLine(
                    (float) (hsisize / 2 * (1 + 0.9 * Math.cos(Math.PI * i / 18))),
                    (float) (hsisize / 2 * (1 + 0.9 * Math.sin(Math.PI * i / 18))),
                    (float) (hsisize / 2 * (1 + Math.cos(Math.PI * i / 18))),
                    (float) (hsisize / 2 * (1 + Math.sin(Math.PI * i / 18))),
                    paint);
        }

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("N", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("3", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("6", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("E", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("12", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("15", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("S", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("21", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("24", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("W", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("30", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));
        canvas.drawText("33", (float) (hsisize / 2), (float) (0.1 * hsisize), paint);
        canvas.rotate(30, (float) (hsisize / 2), (float) (hsisize / 2));

        return hsi;
    }

    Bitmap getLeftTriangleGauge() {
        if (leftTriangleGauge != null)
            return leftTriangleGauge;
        int eisHeight = (int) (height * EIS_HEIGHT);
        leftTriangleGauge = Bitmap.createBitmap(
                (int) (0.03493 * height * EIS_HEIGHT),
                (int) (0.02766 * height * EIS_HEIGHT),
                Bitmap.Config.ARGB_8888);
        leftTriangleGauge.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(leftTriangleGauge);

        Point a = new Point(0, 0);
        Point b = new Point((int) (0.03493 * eisHeight), 0);
        Point c = new Point((int) (0.01747 * eisHeight), (int) (0.02766 * eisHeight));

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
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

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("L", (float) (0.01747 * eisHeight), (float) (0.01383 * eisHeight), paint);

        return leftTriangleGauge;
    }

    Bitmap getRightTriangleGauge() {
        if (rightTriangleGauge != null)
            return rightTriangleGauge;
        int eisHeight = (int) (height * EIS_HEIGHT);
        rightTriangleGauge = Bitmap.createBitmap(
                (int) (0.03493 * height * EIS_HEIGHT),
                (int) (0.02766 * height * EIS_HEIGHT),
                Bitmap.Config.ARGB_8888);
        rightTriangleGauge.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(rightTriangleGauge);

        Point a = new Point(0, (int) (0.02766 * eisHeight));
        Point b = new Point((int) (0.03493 * eisHeight), (int) (0.02766 * eisHeight));
        Point c = new Point((int) (0.01747 * eisHeight), 0);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(c.x, c.y);
        path.lineTo(b.x, b.y);
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

        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("R", (int) (0.01747 * eisHeight), (int) (0.01383 * eisHeight), paint);
        return rightTriangleGauge;
    }

    Bitmap getTopBar() {
        if (topBar != null)
            return topBar;

        int tbHeight = (int) (TOPBAR_HEIGHT * height);
        topBar = Bitmap.createBitmap(width, tbHeight, Bitmap.Config.ARGB_8888);
        topBar.eraseColor(Color.BLACK);
        Canvas canvas = new Canvas(topBar);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth((float) (1));
        paint.setStyle(Paint.Style.STROKE);
        //Bottom line
        canvas.drawLine(0, (float) (0.1 * height - 2), width, (float) (0.1 * height - 2), paint);
        //Mid height line
        canvas.drawLine((float) (0.25 * width), tbHeight / 2, (float) (0.75 * width), tbHeight / 2, paint);
        // Vertical lines
        paint.setStrokeWidth((float) 2);
        canvas.drawLine((float) (0.25 * width), 0, (float) (0.25 * width), tbHeight, paint);
        canvas.drawLine((float) (0.75 * width), 0, (float) (0.75 * width), tbHeight, paint);

        return topBar;
    }

    Bitmap getSoftKeys() {
        if (softKeys != null)
            return softKeys;

        int softHeight = (int) (SOFTKEYS_HEIGHT * height);
        softKeys = Bitmap.createBitmap(width, softHeight, Bitmap.Config.ARGB_8888);
        softKeys.eraseColor(Color.BLACK);
        Canvas canvas = new Canvas(softKeys);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth((float) (2));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, 0, width, 0, paint);
        for (double i = 1; i < 12; i++) {
            //paint.setColor(Color.BLACK);
            //canvas.drawLine(i / 12 * width, 0, i / 12 * width, softHeight, paint);
            //paint.setColor(Color.WHITE);
            double offsetx = (i / 12) * (double) width;
            canvas.drawLine((float) offsetx, 0, (float) offsetx, softHeight, paint);
        }
        return softKeys;
    }

    Bitmap getActiveSoftKey() {
        if (activeSoftKey != null)
            return activeSoftKey;

        int softHeight = (int) (SOFTKEYS_HEIGHT * height);
        int softWidth = width / 12;
        activeSoftKey = Bitmap.createBitmap(softWidth, softHeight, Bitmap.Config.ARGB_8888);
        activeSoftKey.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(activeSoftKey);

        return activeSoftKey;
    }

    Bitmap getEISEngine() {
        if (engine1 != null)
            return engine1;

        int engineHeight = (int) (EIS_HEIGHT * height);
        int engineWidth = (int) (EIS_RATIO * engineHeight);
        engine1 = Bitmap.createBitmap(engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
        engine1.eraseColor(Color.BLACK);
        paint.setColor(Color.GREEN);
        paint.setTextAlign(Paint.Align.CENTER);
        Canvas canvas = new Canvas(engine1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //Load
        canvas.drawRect((float) (engineHeight * 0.05), (float) (engineHeight * 0.02), (float) (engineHeight * 0.06), (float) (engineHeight * 0.174), paint);
        canvas.drawRect((float) (engineHeight * 0.15), (float) (engineHeight * 0.02), (float) (engineHeight * 0.16), (float) (engineHeight * 0.174), paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("LOAD %", (float) (engineHeight * 0.105), (float) (engineHeight * 0.1981), paint);
        for (int i = 0; i < 6; i++) {
            canvas.drawText(String.valueOf((5 - i) * 20), (float) (engineHeight * 0.105), (float) (engineHeight * (0.02 + ((0.174 - 0.02) * i / 5))), paint);
            canvas.drawLine(
                    (float) (engineHeight * 0.05),
                    (float) (engineHeight * (0.02 + ((0.174 - 0.02) * i / 5))),
                    (float) (engineHeight * 0.07),
                    (float) (engineHeight * (0.02 + ((0.174 - 0.02) * i / 5))),
                    paint);

            canvas.drawLine(
                    (float) (engineHeight * 0.14),
                    (float) (engineHeight * (0.02 + ((0.174 - 0.02) * i / 5))),
                    (float) (engineHeight * 0.16),
                    (float) (engineHeight * (0.02 + ((0.174 - 0.02) * i / 5))),
                    paint);
        }
        // RPM
        paint.setColor(Color.GREEN);
        canvas.drawRect((float) (engineHeight * 0.05), (float) (engineHeight * 0.23), (float) (engineHeight * 0.06), (float) (engineHeight * 0.40), paint);
        canvas.drawRect((float) (engineHeight * 0.15), (float) (engineHeight * 0.23), (float) (engineHeight * 0.16), (float) (engineHeight * 0.40), paint);
        paint.setColor(Color.WHITE);
        canvas.drawText("RPM", (float) (engineHeight * 0.105), (float) (engineHeight * 0.4201), paint);
        for (int i = 0; i < 6; i++) {
            canvas.drawText(String.valueOf((5 - i) * 600), (float) (engineHeight * 0.105), (float) (engineHeight * (0.23 + ((0.40 - 0.23) * i / 5))), paint);
            canvas.drawLine(
                    (float) (engineHeight * 0.05),
                    (float) (engineHeight * (0.23 + ((0.40 - 0.23) * i / 5))),
                    (float) (engineHeight * 0.07),
                    (float) (engineHeight * (0.23 + ((0.40 - 0.23) * i / 5))),
                    paint);

            canvas.drawLine(
                    (float) (engineHeight * 0.14),
                    (float) (engineHeight * (0.23 + ((0.40 - 0.23) * i / 5))),
                    (float) (engineHeight * 0.16),
                    (float) (engineHeight * (0.23 + ((0.40 - 0.23) * i / 5))),
                    paint);
        }

        // FUEL FLOW
        paint.setColor(Color.WHITE);
        canvas.drawText("FUEL FLOW", (float) (engineHeight * 0.105), (float) (engineHeight * 0.4611), paint);
        canvas.drawText("GPH", (float) (engineHeight * 0.105), (float) (engineHeight * 0.4881), paint);


        // OIL TEMP
        paint.setColor(Color.WHITE);
        canvas.drawText("OIL TEMP", (float) (engineHeight * 0.105), (float) (engineHeight * 0.5331), paint);
        oiltemp = new HorEISGauge(-40, -32, 50, 125, 140, 150);
        canvas.drawBitmap(oiltemp.drawGauge(), (float) (engineHeight * 0.015), (float) (engineHeight * 0.5593));
        
        // OIL PRES
        paint.setColor(Color.WHITE);
        canvas.drawText("OIL PRES", (float) (engineHeight * 0.105), (float) (engineHeight * 0.6231), paint);
        oilpres = new HorEISGauge(0, 1.0, 2.3, 5.2, 6.5, 8);
        canvas.drawBitmap(oilpres.drawGauge(), (float) (engineHeight * 0.015), (float) (engineHeight * 0.6516));

        // COOLANT TEMP
        paint.setColor(Color.WHITE);
        canvas.drawText("COOLANT TEMP", (float) (engineHeight * 0.105), (float) (engineHeight * 0.7181), paint);
        coolant = new HorEISGauge(-40, -32, 60, 96, 105, 120);
        canvas.drawBitmap(coolant.drawGauge(), (float) (engineHeight * 0.015), (float) (engineHeight * 0.7442));

        // FUEL TEMP
        paint.setColor(Color.WHITE);
        canvas.drawText("FUEL TEMP", (float) (engineHeight * 0.105), (float) (engineHeight * 0.8081), paint);
        fueltemp = new HorEISGauge(-40, -30, -22, 70, 75, 85);
        canvas.drawBitmap(fueltemp.drawGauge(), (float) (engineHeight * 0.015), (float) (engineHeight * 0.8369));

        // FUEL QTY GAL
        paint.setColor(Color.WHITE);
        canvas.drawText("FUEL QTY GAL", (float) (engineHeight * 0.105), (float) (engineHeight * 0.9031), paint);
        fuelqty = new HorEISGauge(0, 1, 1, 25, 25, 25);
        canvas.drawBitmap(fuelqty.drawGauge(), (float) (engineHeight * 0.015), (float) (engineHeight * 0.9319));
        return engine1;
    }

    Bitmap getEISSystem() {
        if (engine2 != null)
            return engine2;

        int engineHeight = (int) (EIS_HEIGHT * height);
        int engineWidth = (int) (EIS_WIDTH * width);
        engine1 = Bitmap.createBitmap(engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
        engine1.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(engine1);

        return engine2;
    }

    Bitmap getEISFuel() {
        if (engine3 != null)
            return engine3;

        int engineHeight = (int) (EIS_HEIGHT * height);
        int engineWidth = (int) (EIS_WIDTH * width);
        engine1 = Bitmap.createBitmap(engineWidth, engineHeight, Bitmap.Config.ARGB_8888);
        engine1.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(engine3);

        return engine3;
    }

    public class EisEngine {
        // ratio compared to EIS Panel size
        static final double OIL_TEMP_X = 0.1;
        static final double OIL_TEMP_Y = 0.5713;
        static final double OIL_TEMP_WIDTH = 0.8;
        static final double OIL_TEMP_MAX = 120;

        static final double OIL_PRES_X = 0.1;
        static final double OIL_PRES_Y = 0.6636;
        static final double OIL_PRES_WIDTH = 0.8;
        static final double OIL_PRES_MAX = 120;
    }

    public class EisSystem {
    }

    public class EisFuel {
    }
}
