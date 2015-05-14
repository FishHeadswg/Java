/*
Richardson, Trevor
COP-3252
Assignment 5
04-08-14

JavaBeanForest.java - Driver
 */

import java.util.Random;
import javax.swing.JOptionPane;

public class JavaBeanForest {

	public static void main(String[] args) {

		// Welcome message
		JOptionPane.showMessageDialog(null, "You have entered JavaBean Forest! Beware of random enemies!",
				"JavaBean Forest", 1);
		int enemies = 0; // sentinel for resume play
		boolean playing = true;
		while (playing == true) { // checks if resuming play
			Random rand = new Random();
			// Prompts user to create their knight
			Fighter knight1 = new Fighter(200 + rand.nextInt(21));
			while (enemies < 3) { // checks if through the forest
				Fighter enemy = Fighter.getRandomEnemy(); // Random enemy to
															// fight
				JOptionPane.showMessageDialog(null, "You have encountered "
						+ enemy.GetName() + "!", "JavaBean Forest", 1);
				// Start battle
				int turn = rand.nextInt(2); // Randomly select first turn
				int round = 0; // Round counter
				int dmg = 0; // Raw damage
				int realdmg; // Damage after armor mitigation
				while ((knight1.GetHealth() > 0) && (enemy.GetHealth() > 0)) {

					// Increment turn counter and round counter
					++turn;
					++round;

					// Knight 1 begins if turn is even
					if (turn % 2 == 0) {
						dmg = knight1.fight(); // calculates weapon damage

						// calculates damage after armor mitigation
						try {
							realdmg = enemy.damage(dmg, knight1.GetWeapon());
						} catch (NegativeDamageException nde) {
							realdmg = 0;
						}
						// Round summary
						JOptionPane.showMessageDialog(
								null,
								"Round " + round + ":\n" + knight1.GetName()
										+ " attacks " + enemy.GetName()
										+ " with a " + knight1.GetWeapon()
										+ " for " + realdmg + " damage!\n"
										+ enemy.GetName() + " has "
										+ enemy.GetHealth()
										+ " health remaining!", "JavaBean Forest",
								1);
					}

					// Knight 2 begins if round is odd
					else {
						dmg = enemy.fight();
						try {
							realdmg = knight1.damage(dmg, enemy.GetWeapon());
						} catch (NegativeDamageException nde) {
							realdmg = 0;
						}
						JOptionPane.showMessageDialog(
								null,
								"Round " + round + ":\n" + enemy.GetName()
										+ " attacks " + knight1.GetName()
										+ " with a " + enemy.GetWeapon()
										+ " for " + realdmg + " damage!\n"
										+ knight1.GetName() + " has "
										+ knight1.GetHealth()
										+ " health remaining!", "JavaBean Forest",
								1);
					}
				} // end while

				// Determines victor and displays outcome
				if (knight1.GetHealth() > 0) {
					JOptionPane.showMessageDialog(null, knight1.GetName()
							+ " has vanquished " + enemy.GetName() + " after "
							+ round + " rounds!", "JavaBean Forest", 1);
					JOptionPane.showMessageDialog(null, "Battle Synopsis:\n\n"
							+ knight1.toString() + "\n\n" + enemy.toString(),
							"JavaBean Forest", 1);
					++enemies;
				} else {
					JOptionPane
							.showMessageDialog(null, enemy.GetName()
									+ " has vanquished " + knight1.GetName()
									+ " after " + round + " rounds!",
									"JavaBean Forest", 1);
					JOptionPane.showMessageDialog(null, "Battle Synopsis:\n\n"
							+ knight1.toString() + "\n\n" + enemy.toString(),
							"JavaBean Forest", 1);
					break;
				}
			} // end enemies while
			if (enemies == 3)
				JOptionPane
						.showMessageDialog(
								null,
								"Congratulations! You made it through JavaBean Forest!",
								"JavaBean Forest", 1);
			else
				JOptionPane.showMessageDialog(null, "You died.",
						"JavaBean Forest", 1);

			String exit = JOptionPane.showInputDialog(null,
					"Would you like to play again? (Enter y|n)",
					"JavaBean Forest", 3);

			// Exits game
			if (exit.equals("n") || exit.equals("N")) {
				JOptionPane.showMessageDialog(null, "Good luck in Camelot!",
						"JavaBean Forest", 1);
				playing = false;
			}
		} // end playing while
	} // end main
} // end class