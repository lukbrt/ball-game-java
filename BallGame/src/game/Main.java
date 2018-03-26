package game;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Locale;

import javax.swing.JFrame;

public class Main
{

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Ball game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 600);
		frame.add(new Gameplay());
		frame.setResizable(false);
		frame.setVisible(true);
		System.out.println(frame.getContentPane().getHeight() + " " + frame.getContentPane().getWidth());
//		GraphicsEnvironment ge = GraphicsEnvironment
//		        .getLocalGraphicsEnvironment();
//
//		Font[] allFonts = ge.getAllFonts();
//
//		for (Font font : allFonts) {
//		    System.out.println(font.getFontName(Locale.US));
//		}
	}

}
