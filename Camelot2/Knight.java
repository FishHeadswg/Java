/*
Richardson, Trevor
COP-3252
Assignment 3
02-11-14

Knight.java - Creates a knight with attributes. 
Also outputs attributes and stars.
*/

import javax.swing.JOptionPane;

public class Knight
{
	// Attributes
	private String name;
	private int health;
	private int battles;
	private int age;
	private int gold;
	private Stars star;
	
	public Knight() // initialize knight with attributes using setters
	{
		this.SetName();
		this.SetHealth();
		this.SetBattles();
		this.SetAge();
		this.SetGold();
		this.SetStars();
	}
	
	// **SET AND GET METHODS**
	
	public void SetName()
	{
		
		name = JOptionPane.showInputDialog(null,
				"Knight's name:", "Knight", 3);
	}
	
	public String GetName()
	{
		return name;
	}
	
	public void SetHealth()
	{
		health = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's health:", "Knight", 3));
	}
	
	public int GetHealth()
	{
		return health;
	}
	
	public void SetBattles()
	{
		battles = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's battles:", "Knight", 3));
	}
	
	public int GetBattles()
	{
		return battles;
	}
	
	public void SetAge()
	{
		age = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's age:", "Knight", 3));
	}
	
	public int GetAge()
	{
		return age;
	}
	
	public void SetGold()
	{
		gold = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's gold:", "Knight", 3));
	}
	
	public int GetGold()
	{
		return gold;
	}
	
	public void SetStars()
	{
		star = new Stars();
	}
	
	public Stars GetStars()
	{
		return star;
	}
	
	// **END OF SET AND GET METHODS**
	
	
	// Outputs knight's info and stars
	public void display()
	{
		String message = 
				"Knight Name: " + GetName() +
				"\nKnight Health: " + Integer.toString(GetHealth()) +
				"\nKnight Battles: " + Integer.toString(GetBattles()) +
				"\nKnight Age: " + Integer.toString(GetAge()) +
				"\nKnight Gold: " + Integer.toString(GetGold()) + "g" +
				"\n" + star.Output();
		JOptionPane.showMessageDialog(null, message, GetName(), 1);
	}
	
	// Exit message
	public void exit()
	{
		JOptionPane.showMessageDialog(null, "Good luck in Camelot!",
				GetName(), 1);
	}
	
} // end Knight class