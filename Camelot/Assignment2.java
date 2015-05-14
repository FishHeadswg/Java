/*
Richardson, Trevor
COP-3252
Assignment 2
01-31-14
*/

import java.util.Scanner;

public class Assignment2
{
	// Attributes
	private static String name;
	private static int health;
	private static int battles;
	private static int age;
	private static int gold;
	
	public static void main(String[] args)
	{
		System.out.printf("Welcome to Camelot!\nPlease input your name: ");
		Scanner input = new Scanner(System.in);
		name = input.nextLine();	// set name
		System.out.printf("Health: ");
		health = input.nextInt();	// set health
		System.out.printf("Number of battles: ");
		battles = input.nextInt();	// set battles
		System.out.printf("Age: ");
		age = input.nextInt();		// set age
		System.out.printf("Gold: ");
		gold = input.nextInt();		// set gold
		System.out.printf("%n%s's information:%n", name); // display attributes
		System.out.printf("Health:          %d%nBattles:         %d%n" +
				"Age:             %d%nGold:            %d%nGold Per Battle: " +
				"%d%n", health, battles, age, gold, gold/battles); // int div
		System.out.printf("%nGood luck in Camelot, %s!", name); // exit  msg
	} // end main
} // end class Assignment2