package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BrickMap
{
	protected int bricks[][];
	private int bw;
	private int bh;
	private GradientPaint gradient;
	private int level;
	private int randomIndex;
	
	//protected BufferedImage wood;
	protected Image wood;
	protected Image crackedWood;
	
	public BrickMap(int rows, int col, int level)
	{
		bricks = new int[rows][col];
		
		this.level = level;
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < col; j++)
			{
				bricks[i][j] = 1;
			}
		}
		
		bw = 540 / col;
		bh = 150 / rows;
		
		switch (level)
		{	
			case 1:
				for (int j = 2; j <= 5; j++)
				{
					bricks[2][j] = 2;
				}
				break;
			
			case 2:
				bricks[1][2] = 2;
				bricks[1][3] = 2;
				bricks[2][4] = 2;
				bricks[2][5] = 2;
				bricks[2][6] = 2;
				break;
				
			case 3:
				bh = 40;
				for (int j = 0; j < 3; j++)
				{
					randomIndex = (int)(Math.random() * (col));
					bricks[1][randomIndex] = 2;
					
					randomIndex = (int)(Math.random() * (col));
					bricks[2][randomIndex] = 2;
					
					randomIndex = (int)(Math.random() * (col));
					bricks[4][randomIndex] = 2;
				}
				
				bricks[0][1] = 3;
				bricks[0][5] = 3;
				for (int j = 0; j < 3; j++)
				{
					bricks[3][j + 1] = 3;
					bricks[4][j + 3 + (j % 2)] = 3;
				}
				
				break;
			
		}
//		try
//		{
//			wood = ImageIO.read(new File("res/images/rsz_wood.jpg"));
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
		
		
//		wood = Toolkit.getDefaultToolkit().getImage("res/images/wood_brick.jpg");
//		crackedWood = Toolkit.getDefaultToolkit().getImage("res/images/crooked_wood_brick.jpg");
		try
		{
			wood = ImageIO.read(ResourceLoader.load("images/wood_brick.jpg"));
			crackedWood = ImageIO.read(ResourceLoader.load("images/crooked_wood_brick.jpg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
//		wood = ResourceLoader.getImage("rsz_wood.jpg");
//		crackedWood = ResourceLoader.getImage("rsz_wood_cracked2.jpg");
//		wood = loadImage("rsz_wood.jpg");
//		crackedWood = loadImage("rsz_wood_cracked2.jpg");
	}
	
//	private Image loadImage(String fileName)
//	{
//		return Toolkit.getDefaultToolkit().getImage(BrickMap.class.getResource("/res/images/") + fileName);
//	}
	
	public void draw(Graphics2D g2d)
	{
		for (int i = 0; i < bricks.length; i++)
		{
			for (int j = 0; j < bricks[i].length; j++)
			{
				if (bricks[i][j] != 0)
				{
					if (bricks[i][j] == 1)
						gradient = new GradientPaint(new Point2D.Double(j * bw + 80 / 2, i * bh + 50), new Color(42, 180, 209), new Point2D.Double(j * bw + 80 / 2, i * bh + 100), new Color(13, 55, 64));
					else if (bricks[i][j] == 2)
						gradient = new GradientPaint(new Point2D.Double(j * bw + 80 / 2, i * bh + 50), new Color(124, 127, 124), new Point2D.Double(j * bw + 80 / 2, i * bh + 100), new Color(248, 255, 248));
					else if (bricks[i][j] == 3)
					{
						
						g2d.drawImage(wood, j * bw + 80, i * bh + 50, bw, bh, null);
						g2d.finalize();
					}
					else if (bricks[i][j] == 4)
					{
						
						g2d.drawImage(crackedWood, j * bw + 80, i * bh + 50, bw, bh, null);
						g2d.finalize();
					}
					
					if (bricks[i][j] < 3)
					{
						g2d.setPaint(gradient);
						g2d.fillRect(j * bw + 80, i * bh + 50, bw, bh);
					}
					
					g2d.setStroke(new BasicStroke(3));
					g2d.setColor(Color.BLACK);
					g2d.drawRect(j * bw + 80, i * bh + 50, bw, bh);
				}
			}
		}
	}

	public int getBw()
	{
		return bw;
	}

	public int getBh()
	{
		return bh;
	}
	
	
}
