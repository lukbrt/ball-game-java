package game;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Ball
{
	public static final int XSIZE = 20;
	public static final int YSIZE = 20;
	private double x;
	private double y;
	private double dx = 1;
	private double dy = 2;
	
	public Ball(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void move(Rectangle2D bounds)
	{
		x += dx;
		y += dy;
		
		if (x <= bounds.getMinX())
		{
			x = 2;
			dx = -dx;
		}
		if (x > bounds.getMaxX() - XSIZE)
		{
			x = 692 - XSIZE;
			dx = -dx;
		}
		if (y <= bounds.getMinY())
		{
			y = bounds.getMinY();
			dy = -dy;
		}
	}
	
	public void reverseY()
	{
		dy = -dy;
	}
	
	public void reverseX()
	{
		dx = -dx;
	}
	
	public Ellipse2D getShape()
	{
		return new Ellipse2D.Double(x, y, XSIZE, YSIZE);
	}
	
	public void reinitialize(double x, double y)
	{
		this.x = x;
		this.y = y;
		dx = 1;
		dy = 2;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}
	
	
	
}
