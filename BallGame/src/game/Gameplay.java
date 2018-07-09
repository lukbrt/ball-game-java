package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener
{
	private Ball ball;
	private Rectangle2D playfield;
	BrickMap map;
	
	private boolean play;
	private int score;
	private int totalBricks;
	private int level = 0;
	private int[] unlockedLevels;
	private boolean win, winIsUpdated;
	
	private int sliderPosition = 300;
	private int sliderWidth = 100;
	private int sliderHeigth = 10;
	
	private Timer timer;
	
	public static final int SLIDER_SHIFT = 20;
	public static final int DELAY = 5;
	
	public Gameplay()
	{
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(DELAY, this);
		timer.start();

		ball = new Ball(300, 300);
		playfield = new Rectangle2D.Double(2, 2, 690, 569);
		
		unlockedLevels = new int[4];
		//fillProgress("res/unlocked.txt");

		fillProgress(ResourceLoader.load("unlocked.txt"));
		
//		for (int i = 0; i < unlockedLevels.length; i++)
//			System.out.println(unlockedLevels[i]);
		
		setLevel(level);
	}
	
	public void paint(Graphics g)
	{
		/*
		 * Przygotowywanie tła
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(2, 2, 690, 590);
		
		/*
		 * Wypisz score
		 */
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Serif", Font.BOLD, 25));
		g2d.drawString("SCORE: " + score, 508, 30);

		/*
		 * Przygotowywanie ramki
		 */
		g2d.setColor(Color.YELLOW);
		g2d.fillRect(0, 0, 3, this.getBounds().height); //lewa krawędź
		g2d.fillRect(this.getWidth() - 3, 0, 3, this.getBounds().height); //prawa kr
		g2d.fillRect(0, 0, this.getBounds().width, 3); //górna kr
		
		/*
		 * Rysowanie cegiełek
		 */
		map.draw(g2d);
		
		/*
		 * Rysowanie slidera
		 */
		GradientPaint gradient = new GradientPaint(new Point2D.Double(sliderPosition + sliderWidth / 2, this.getHeight() - sliderHeigth - 1), Color.MAGENTA, new Point2D.Double(sliderPosition + sliderWidth / 2, this.getHeight() - 1), Color.CYAN);
		g2d.setPaint(gradient);
		g2d.fillRect(sliderPosition, this.getHeight() - sliderHeigth - 2, sliderWidth, sliderHeigth);
		
		/*
		 * Rysowanie piłki
		 */
		g2d.setColor(Color.YELLOW);
		g2d.fill(ball.getShape());
		
		/*
		 * Restart komunikat
		 */
		if (ball.getY() > 570)
		{
			play = false;
			printCenteredText(g2d, new Font("Serif", Font.BOLD, 36), "Press ENTER to restart", Color.RED);
		}
		
		/*
		 * Komunikat 'win'
		 */
		if (totalBricks == 0)
		{
			play = false;
			g2d.setColor(Color.YELLOW);
			g2d.setFont(new Font("Serif", Font.BOLD, 36));
			g2d.drawString("Congratulations - you won the game!", 60, 300);
			g2d.setFont(new Font("Serif", Font.BOLD, 24));
			g2d.drawString("Press ENTER to continue...", 200, 330);
			
			if (level < unlockedLevels.length - 1)
				unlockedLevels[level + 1] = 1;
			
			if (!winIsUpdated)
			{
				updateProgressFile("res/unlocked.txt");
				winIsUpdated = true;
			}
			win = true;
		}
		
		g2d.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		timer.start();
		if (play)
		{
			
			Rectangle2D slider = new Rectangle2D.Double(sliderPosition, 561, sliderWidth, sliderHeigth);
			Ellipse2D b = ball.getShape();
			boolean found = false;
			
			for (int i = 0; i < map.bricks.length; i++)
			{
				for (int j = 0; j < map.bricks[i].length; j++)
				{
					if (map.bricks[i][j] > 0)
					{
						Rectangle2D ballRect = b.getBounds2D();
						
						int brickWidth = map.getBw();
						int brickHeight = map.getBh();
						int brickX = brickWidth * j + 80;
						int brickY = brickHeight * i + 50;
						
						Rectangle2D brickRect = new Rectangle2D.Double(brickX, brickY, brickWidth, brickHeight);
						//Rectangle2D br = brickRect;
						
						if (brickRect.intersects(ballRect))
						{
							
							if (map.bricks[i][j] == 1) 
							{
								map.bricks[i][j] = 0;
								totalBricks--;
								score += 5;
							}
							else if (map.bricks[i][j] == 3)
							{
								map.bricks[i][j]++;
								score += 3;
							}
							else if (map.bricks[i][j] == 4)
							{
								map.bricks[i][j] = 0;
								totalBricks--;
								score += 7;
							}
							found = true;
							if (ballRect.getMinX() >= brickX + brickWidth - 3 || ballRect.getMaxX() <= brickX + 3)
							{
								ball.reverseX();
							}
							else
							{
								ball.reverseY();
							}
							
							break;
						}
						
					}
				}
				if (found)
					break;
			}
			
			if (b.getBounds2D().intersects(slider))
			{
//				if (b.getBounds2D().getMaxY() > 561)
//				{
//					if (b.getBounds2D().getMinX() >= sliderPosition + sliderWidth || b.getBounds2D().getMaxX() <= sliderPosition)
//						ball.reverseX();
//				}
				if (b.getBounds2D().getMaxY() < slider.getMinY() + 4)
					ball.reverseY();
			}
			
//			if (ball.getShape().intersects(new Rectangle2D.Double(sliderPosition, 561, sliderWidth, sliderHeigth)))
//			if (b.getBounds2D().intersects(slider))
//			{
//				ball.reverseY();
//			}
			
			ball.move(playfield);
		}
		
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			if (sliderPosition <= 3)
				sliderPosition = 3;
			else
				moveLeft();
		}
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			if (sliderPosition >= this.getBounds().width - sliderWidth - 4)
				sliderPosition = this.getBounds().width - sliderWidth - 3;
			else
				moveRight();
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (!play)
			{
				play = true;
				ball.reinitialize(150, 300);
				setLevel(level);
				
				if (win)
				{
					setNextLevel();
					win = false;
				}
				
				repaint();
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_S)
		{
			JFrame levelSelectionFrame = new LevelSelectionFrame(this);
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{}

	@Override
	public void keyTyped(KeyEvent e)
	{}
	
	public void moveLeft()
	{
		play = true;
		sliderPosition -= SLIDER_SHIFT;
	}
	
	public void moveRight()
	{
		play = true;
		sliderPosition += SLIDER_SHIFT;
	}
	//TODO
	private void fillProgress(InputStream input)
	{
		//File file = new File(path);
		int i = 0;
		Scanner scanner = new Scanner(input);
		while(scanner.hasNext())
		{
		    if (scanner.hasNextInt()) 
		    {
		    	unlockedLevels[i++] = scanner.nextInt();
		    } 
		    else 
		    {
		        scanner.next();
		    }
		}
		scanner.close();
	}
	
//	public void fillProgress(String path)
//	{
//		File file = new File(path);
//		try
//		{
//			int i = 0;
//			Scanner scanner = new Scanner(file);
//			while(scanner.hasNext())
//			{
//			    if (scanner.hasNextInt()) 
//			    {
//			    	unlockedLevels[i++] = scanner.nextInt();
//			    } 
//			    else 
//			    {
//			        scanner.next();
//			    }
//			}
//			scanner.close();
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	private void updateProgressFile(String path)
	{
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < unlockedLevels.length; i++)
		{
			sb.append(unlockedLevels[i]);
			sb.append("\n");
		}
		
		try (PrintStream out = new PrintStream(new FileOutputStream(path))) 
		{
		    out.print(sb.toString());
		    out.flush();
		    out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setLevel(int lvl)
	{	
		play = false;
		winIsUpdated = false;
		if (this.getBounds().width != 0)
		{
			sliderPosition = this.getBounds().width / 2 - sliderWidth/2;
			ball.reinitialize(this.getBounds().width / 2 - ball.XSIZE/2, 300);
		}
		
		if (unlockedLevels[lvl] != 0)
		{
			level = lvl;
		}
		
		if (level < 3)
		{
			totalBricks = 21;
			map = new BrickMap(3, 7, level);
		}
		else if (level == 3)
		{
			totalBricks = 40;
			map = new BrickMap(5, 8, level);
		}
		
		for (int i = 0; i < map.bricks.length; i++)
		{
			for (int j = 0; j < map.bricks[i].length; j++)
			{
				if (map.bricks[i][j] == 2)
					totalBricks--;
			}
		}
	}
	
	public void setNextLevel()
	{
		if (level < unlockedLevels.length - 1)
		{
			level++;
			setLevel(level);
		}
	}
	
	public boolean isUnlocked(int n)
	{
		return unlockedLevels[n] == 1;
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize)
	{
		super.setPreferredSize(preferredSize);
	}
	
	private void printCenteredText(Graphics2D g2d, Font font, String text, Color color)
	{
		FontRenderContext context = g2d.getFontRenderContext();
        TextLayout txt = new TextLayout(text, font, context);

        Rectangle2D bounds = txt.getBounds();
        int x = (int) ((getWidth() - (int) bounds.getWidth()) / 2);
        int y = (int) ((getHeight() - (bounds.getHeight() - txt.getDescent())) / 2);
        y += txt.getAscent() - txt.getDescent();

        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);
	}
}