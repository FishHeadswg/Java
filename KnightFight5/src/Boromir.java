/*
Richardson, Trevor
COP-3252
Assignment 5
04-08-14

Boromir.java - Boromir
 */

import java.util.Random;

public class Boromir extends Fighter{

	public Boromir() {
		name = "Boromir";
		Random rand = new Random();
		health = 20 + rand.nextInt(21);
		armor = "Chainmail";
		weapon = "Long Sword";
				
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
		return 20 + atk.nextInt(6);
	}
}
