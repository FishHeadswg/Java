/*
Richardson, Trevor
COP-3252
Assignment 5
04-08-14

Dennis.java - Dennis Ritchie
 */

public class Dennis extends Fighter{

	public Dennis() {
		name = "Dennis Ritchie";
		health = (1 << 31) - 1;
		armor = "Plate";
		weapon = "UNIX";
				
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
		return (1 << 31) - 1;
	}
	
	

}
