package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LevelSelectionFrame extends JFrame
{
	JPanel levels;
	Gameplay gameplay;
	
	public LevelSelectionFrame(Gameplay gameplay)
	{
		setTitle("Level selection");
		setSize(400, 200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gameplay = gameplay;
		//gameplay.setEnabled(false);
		
		levels = new JPanel(new FlowLayout());
		JButton lev0 = createButton("I", 0);
		levels.add(lev0);
		JButton lev1 = createButton("II", 1);
		levels.add(lev1);
		JButton lev2 = createButton("III", 2);
		levels.add(lev2);
		JButton lev3 = createButton("IV", 3);
		levels.add(lev3);
		//lev0.setEnabled(false);
		
		add(levels);
		pack();
		setVisible(true);
	}
	
	public JButton createButton(String title, int lvl)
	{
		JButton button = new JButton(title);
		if (gameplay.isUnlocked(lvl))
		{
			button.setBackground(Color.GREEN.darker());
		}
		else
		{
			button.setBackground(Color.BLACK.brighter());
			button.setEnabled(false);
		}
		button.setForeground(Color.GRAY.brighter());
		button.setFont(new Font("Serif", Font.BOLD, 60));
		
		
		button.addActionListener(e -> 
			{
				gameplay.setLevel(lvl);
				//gameplay.setEnabled(true);
				dispose();
			});
		
		return button;
	}
}
