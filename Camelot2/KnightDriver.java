/*
Richardson, Trevor
COP-3252
Assignment 3
02-11-14

KnightDriver.java - Driver for Knight class. Works with multiple knights.
*/

import javax.swing.JOptionPane;

public class KnightDriver {

	public static void main(String[] args)
	{
		JOptionPane.showMessageDialog(null, "Welcome to Camelot!",
				"Knight", 1);
		Knight knight1 = new Knight();
		knight1.display();
		knight1.exit();
	} // end main
}