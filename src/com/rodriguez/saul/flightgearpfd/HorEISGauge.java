public class HorEISGauge
{
	private double[] params;
	private Bitmap gauge;
	private int width, height;

	public HorEISGauge(double min, double lowred, double lowyellow, double green, double highyellow, double max, int eisWidth, int eisHeight)
	{
		gauge = null;
		params[0] = min;
		params[1] = lowred;
		params[2] = lowyellow;
		params[3] = green;
		params[4] = highyellow;
		params[5] = max;

		width = 0.9 * eisWidth;
		height = 0.024 * eisHeight;
	}

	public Bitmap drawGauge ()
	{
		if(gauge != null)
			return gauge;
		gauge = Bitmap.createBitmap(width, 0.024 * height);
		gauge.eraseColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(gauge);

		double unit = width / (params[5] - params[0]);

		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		paint.setColor(Color.RED);
		canvas.drawRect(0, 0.25 * height, unit * (params[1] - params[0]), 0.75 * height, paint);
		canvas.drawRect(unit * (params[4] - params[0]), 0.25 * height, width, 0.25 * height, paint);

		paint.setColor(Color.YELLOW);
		canvas.drawRect(unit * (params[1] - params[0]), 0.25 * height, unit * (params[2] - params[0]), 0.75 * height, paint);
		canvas.drawRect(unit * (params[3] - params[0]), 0.25 * height, unit * (params[4] - params[0]), 0.25 * height, paint);

		paint.setColor(Color.GREEN);
		canvas.drawRect(unit * (params[2] - params[0]), 0.25 * height, unit * (params[3] - params[0]), 0.75 * height, paint);
		
		paint.setColor(Color.WHITE);
		canvas.drawLine(0, 0, 0, height, paint);
		canvas.drawLine(width - 1, 0, width - 1, height, paint);

		return gauge;
	}

	public Status getStatus (double value)
	{
		if(value < params[1] || value > params[5])
			return Status.RED;
		if(value < params[2] || value > params[4])
			return Status.YELLOW;
		return Status.GREEN;
	}
	
	public double getCursorPosition (double value)
	{
		return (value - params[0]) / (params[5] - params[0]);
	}
	
	public int getWidth()
	{
		return width;
	}

	public class Status
	{
		public final static int RED = 0;
		public final static int YELLOW = 1;
		public final static int GREEN = 2;
	}
	
	abstract class Cursor ()
	{
		private Bitmap green, yellow, red;
		public final static int LEFT = 0;
		public final static int RIGHT = 1;
		
		public Bitmap getCursor(int s)
		{
			Color c;
			switch(s)
				case Status.RED:
					if(red == null) 
						red = drawCursor(Color.RED);
					return red;
				case Status.YELLOW:
					if(yellow == null) 
						yellow = drawCursor(Color.YELLOW);
					return red;
				case Status.GREEN:
					if(green == null) 
						green = drawCursor(Color.WHITE);
					return red;
			return null;
		}
		
		private abstract Bitmap drawCursor(Color c);
	}
	
	public class LeftCursor extends Cursor
	{
		private Bitmap drawCursor(Color c)
		{
		}
	}
	
	public class LeftCursor extends Cursor
	{
		private Bitmap drawCursor(Color c)
		{
		}
	}

}
