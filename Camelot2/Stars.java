/*
Richardson, Trevor
COP-3252
Assignment 3
02-11-14

Stars.java - Stores star rows/columns and outputs a string matrix of stars.
*/

import javax.swing.JOptionPane;

public class Stars{
	
	private int rows;
	private int cols;
	
	Stars()
	{
		this.SetRows();
		this.SetCols();
	}
	
	public void SetRows()
	{
		rows = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Number of rows of stars:", "Stars", 3));
	}
	
	public int GetRows()
	{
		return rows;
	}
	
	public void SetCols()
	{
		cols = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Number of columns of stars:", "Stars", 3));
	}
	
	public int GetCols()
	{
		return cols;
	}
	
	
	// assembles matrix of stars for knight
	public String Output()
	{
		int i = 0;
		String rowString = "";
		while (i < cols)
		{
			rowString += "* ";
			++i;
		}
		i = 0;
		int even;
		String starString = "";
		while (i < rows)
		{
			even = i % 2;
			if (even == 0)
				starString += "\n" + rowString;
			else
				starString += "\n " + rowString;
			++i;
		}
		return starString;
	}
	
}