/*
Richardson, Trevor
COP-3252
Assignment 5
04-08-14

Patches.java - Patches the Abomination
 */

import java.util.Random;

public class Patches extends Fighter{

	public Patches() {
		name = "Patches the Abomination";
		Random rand = new Random();
		health = 30 + rand.nextInt(21);
		armor = "Leather";
		weapon = "Mace";
				
	}
	@Override
	public String toString() {
		String message = "Enemy: " + GetName() + "\nHealth: "
				+ Integer.toString(GetHealth()) + "\nWeapon: "
				+ GetWeapon() + "\nArmor: " + GetArmor();
		return message;
	}
	@Override
	public int fight() {
		Random atk = new Random();
		return 5 + atk.nextInt(31);
	}
	

}
