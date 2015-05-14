/*
Richardson, Trevor
COP-3252
Assignment 5
04-08-14

Fighter.java - Creates a knight/enemy with attributes and weapon/armor.
Custom Exception -> NegativeDamageException in damage() method and used by driver.
Standard Exception -> IllegalArgumentException in Fighter(int), among others.
 */


import java.util.Random;
import javax.swing.JOptionPane;

public class Fighter {
	// Attributes
	protected String name;
	protected int health;
	protected int gold;
	protected String weapon; // LONG SWORD, WAR AXE, POLEARM, MACE
	protected String armor; // LEATHER, CHAINMAIL, PLATE
	static private String[] names = { "Sir Jav-a-lot", "Private Staticvoid",
			"Boromir", "Sir Lancelot", "Gilgamesh", "King Arthur" };

	public Fighter() // auto generate enemy
	{
		SetGold(100);
	}

	public Fighter(int hp) // initialize knight with attributes from user
	{
		try {
			SetHealth(hp);
		} catch (IllegalArgumentException iae) {
			JOptionPane.showMessageDialog(null, iae.getMessage(),
					"JavaBean Forest", 1);
			SetHealth(200);
		}
		SetName();
		SetWeapon();
		SetArmor();
		SetGold(100);
	}

	public static Fighter getRandomEnemy() {
		Random rand = new Random();
		int randEnemy = rand.nextInt(3);
		switch (randEnemy) {
		case 0:
			return new Boromir();
		case 1:
			return new Patches();
		case 2:
			return new Dennis();
		}
		return new Fighter(); // should never happen
	}

	// **SET AND GET METHODS**

	// Sets a random name from an array of names
	public void SetRandName() {

		Random rand = new Random();
		this.name = names[rand.nextInt(6)];
	}

	// Sets a random weapon
	public void SetRandWeapon() {
		Random rand = new Random();
		int wep = 1 + rand.nextInt(4);
		switch (wep) {
		case 1:
			weapon = "Long Sword";
			break;
		case 2:
			weapon = "War Axe";
			break;
		case 3:
			weapon = "Polearm";
			break;
		case 4:
			weapon = "Mace";
			break;
		}
	}

	// Sets a random armor type
	public void SetRandArmor() {
		Random rand = new Random();
		int arm = 1 + rand.nextInt(3);
		switch (arm) {
		case 1:
			armor = "Leather";
			break;
		case 2:
			armor = "Chainmail";
			break;
		case 3:
			armor = "Plate";
			break;
		}
	}

	public void SetWeapon(String weapon) {
		this.weapon = weapon;
	}

	public void SetArmor(String armor) {
		this.armor = armor;
	}

	// Sets a user-defined weapon
	public void SetWeapon() {

		boolean check;
		do {
			check = false;
			int wep = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Choose a weapon (Enter 1-4):" + "\n1. Long Sword"
							+ "\n2. War Axe" + "\n3. Polearm" + "\n4. Mace",
					"JavaBean Forest", 3));
			switch (wep) {
			case 1:
				weapon = "Long Sword";
				break;
			case 2:
				weapon = "War Axe";
				break;
			case 3:
				weapon = "Polearm";
				break;
			case 4:
				weapon = "Mace";
				break;
			default:
				JOptionPane.showMessageDialog(null, "Input a number 1-4!",
						"JavaBean Forest", 2);
				check = true;
			}
		} while (check == true);

	}

	// Save weaponSelect

	public String GetWeapon() {
		return weapon;
	}

	public void SetArmor() {
		boolean check;
		do {
			check = false;
			int arm = Integer.parseInt(JOptionPane.showInputDialog(null,
					"Choose an armor type:" + "\n1. Leather" + "\n2. Chainmail"
							+ "\n3. Plate", "JavaBean Forest", 3));
			switch (arm) {
			case 1:
				armor = "Leather";
				break;
			case 2:
				armor = "Chainmail";
				break;
			case 3:
				armor = "Plate";
				break;
			default:
				JOptionPane.showMessageDialog(null, "Input a number 1-3!",
						"JavaBean Forest", 2);
				check = true;
			}
		} while (check == true);
	}

	public String GetArmor() {
		return armor;
	}

	public void SetName() {

		name = JOptionPane.showInputDialog(null, "What is your knight's name?",
				"JavaBean Forest", 3);
	}

	public String GetName() {
		return name;
	}

	public void SetHealth(int health) {
		if (health < 0)
			throw new IllegalArgumentException(
					"Health can't be negative! Setting health to 200.");
		this.health = health;
	}

	public void SetHealth() {
		health = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's health:", "JavaBean Forest", 3));
	}

	public int GetHealth() {
		if (health <= 0)
			return 0;
		return health;
	}

	public void SetGold() {
		gold = Integer.parseInt(JOptionPane.showInputDialog(null,
				"Knight's gold:", "JavaBean Forest", 3));
	}

	public void SetGold(int gold) {
		this.gold = gold;
	}

	public int GetGold() {
		return gold;
	}

	// **END OF SET AND GET METHODS**

	// Outputs knight's info
	public String toString() {
		String message = "Knight Name: " + GetName() + "\nHealth: "
				+ Integer.toString(GetHealth()) + "\nWeapon: " + GetWeapon()
				+ "\nArmor: " + GetArmor();
		return message;
	}

	// Returns attack damage based on weapon
	public int fight() {
		Random atk = new Random();
		switch (weapon) {
		case "Long Sword":
			return 20 + atk.nextInt(6);
		case "War Axe":
			return atk.nextInt(36);
		case "Polearm":
			return 10 + atk.nextInt(21);
		case "Mace":
			return 5 + atk.nextInt(31);
		}
		return 1000; // should never happen
	}

	// Returns real attack damage based on weapon and armor and subtracts it
	// from health.
	public int damage(int atk, String enemyWep) throws NegativeDamageException {
		if (atk < 0)
			throw new NegativeDamageException();
		int realdmg;
		switch (armor) {
		case "Leather":
			if (enemyWep.equals("Mace")) {
				realdmg = (int) Math.floor(atk * 0.60);
				health -= realdmg;
				return realdmg;
			} else {
				health -= atk;
				return atk;
			}
		case "Chainmail":
			if (enemyWep.equals("Long Sword") || enemyWep.equals("War Axe")) {
				realdmg = (int) Math.floor(atk * 0.90);
				health -= realdmg;
				return realdmg;
			}
			if (enemyWep.equals("Mace")) {
				realdmg = (int) Math.floor(atk * 0.75);
				health -= realdmg;
				return realdmg;
			} else {
				health -= atk;
				return atk;
			}
		case "Plate":
			if (enemyWep.equals("Long Sword") || enemyWep.equals("War Axe")) {
				realdmg = (int) Math.floor(atk * 0.70);
				health -= realdmg;
				return realdmg;
			}
			if (enemyWep.equals("Polearm")) {
				realdmg = (int) Math.floor(atk * 0.60);
				health -= realdmg;
				return realdmg;
			} else {
				health -= atk;
				return atk;
			}
		} // end switch
		return 1000; // should never happen
	}

	// Exit message
	public void exit() {
		JOptionPane.showMessageDialog(null, "Good luck in Camelot!",
				"JavaBean Forest", 1);
	}

} // end Knight class