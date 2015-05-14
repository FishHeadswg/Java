/*
Richardson, Trevor
COP-3252
Project 1
02-24-14

MainBoard.java - Creates a tic-tac-toe board with various settings. 
Processes moves, checks for wins, and implements a fairly stupid computer player.

2D array (matrix) concept and implementation ideas from Section 27.8 of Deitel and:
http://pervasive2.morselli.unimo.it/~nicola/courses/IngegneriaDelSoftware/java/JavaGame_TicTacToe.html
(Nothing is a direct copy-paste from either source)
 */

import java.util.Random;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {

	static final long serialVersionUID = 42L; // Eclipse apparently wanted this
	private boolean inPlay = true; // game in progress
	private boolean WinnerX = false; // X won
	private boolean WinnerO = false; // O won
	private boolean vsComp = true; // On by default

	private int turn = 0;

	// Create 3x3 matrix for board
	private int gameBoard[][] = new int[3][3];

	// Create a 3x3 matrix of buttons for board
	private JButton spaces[][] = new JButton[3][3];

	// Garnet and gold colors
	private Color garnet = new Color(140, 0, 12);
	private Color gold = new Color(180, 150, 90);

	public Board() {

		// Colors in odd border gaps (possible problem on my end, will check
		// on VM)
		this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, garnet));

		// Set layout to 3x3 grid
		GridLayout layout = new GridLayout(3, 3, 0, 0);
		this.setLayout(layout);

		// Keeps text gold after button is used
		UIManager.getDefaults().put("Button.disabledText", gold);

		// Sets various settings for each button in the matrix
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				spaces[i][j] = new JButton("");
				spaces[i][j].setBackground(garnet);

				// Getting border issues for some raisin
				spaces[i][j].setBorder(BorderFactory.createLineBorder(gold, 3));

				// X/O
				spaces[i][j].setFont(new Font("Arial", Font.BOLD, 124));
				spaces[i][j].setName(i + "" + j);

				// Button action for moves
				spaces[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						move(evt);
					}
				});
				add(spaces[i][j]);
			}
		}
	}

	// Processes each move
	// (might want to rename this if Java implements move like C++11)
	private void move(ActionEvent evt) {
		++turn;
		if (!WinnerX && !WinnerO && inPlay) {

			// Gets matrix location of last move
			int x = Integer.parseInt(String.valueOf(((JButton) evt.getSource())
					.getName().charAt(0)));
			int y = Integer.parseInt(String.valueOf(((JButton) evt.getSource())
					.getName().charAt(1)));

			// Determines whether X or O moved and sets 1 or -1, respectively
			if (vsComp) {
				spaces[x][y].setText("O");
				gameBoard[x][y] = -1;
			} else {
				if (turn % 2 == 0) {
					spaces[x][y].setText("X");
					gameBoard[x][y] = 1;
				} else {
					spaces[x][y].setText("O");
					gameBoard[x][y] = -1;
				}
			}

			// Space disabled
			spaces[x][y].setEnabled(false);
			winCheck();
		}

		// CPU decides its move
		if (vsComp && inPlay) {
			if (!WinnerO && !WinnerX) {
				Random rand = new Random();
				boolean deciding = true;
				int x = 0;
				int y = 0;

				// Basically looks until it finds a random unoccupied space
				// Sets X to that space
				while (deciding) {
					x = rand.nextInt(3);
					y = rand.nextInt(3);

					if (gameBoard[x][y] == 0)
						deciding = false;
				}
				spaces[x][y].setText("X");
				gameBoard[x][y] = 1;
				spaces[x][y].setEnabled(false);
				winCheck();
			}
		}
	} // end move

	// Save settings
	public void saveSettings(String gameType) {
		if (gameType.compareTo("player") == 0) {
			setComp(false);
		} else {
			setComp(true);
		}
		reset();
	}

	// Sets vsComp
	private void setComp(boolean comp) {
		vsComp = comp;
	}

	// Checks if a win has occurs over row/col/diag, highlights path if so
	private void winCheck() {

		// Checks if there are any moves still left
		int movesLeft = 9; // max moves in 3x3 TTT
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				if (gameBoard[i][j] != 0) {
					movesLeft--;
				}
			}
		}

		// Win over column?
		for (int j = 0; j < 3; ++j) {
			if (gameBoard[0][j] + gameBoard[1][j] + gameBoard[2][j] == -3) {
				WinnerO = true;
				spaces[0][j].setBackground(Color.YELLOW);
				spaces[1][j].setBackground(Color.YELLOW);
				spaces[2][j].setBackground(Color.YELLOW);
			} else if (gameBoard[0][j] + gameBoard[1][j] + gameBoard[2][j] == 3) {
				WinnerX = true;
				spaces[0][j].setBackground(Color.YELLOW);
				spaces[1][j].setBackground(Color.YELLOW);
				spaces[2][j].setBackground(Color.YELLOW);
			}
		}

		// Win over row?
		for (int i = 0; i < 3; ++i) {
			if (gameBoard[i][0] + gameBoard[i][1] + gameBoard[i][2] == -3) {
				WinnerO = true;
				spaces[i][0].setBackground(Color.YELLOW);
				spaces[i][1].setBackground(Color.YELLOW);
				spaces[i][2].setBackground(Color.YELLOW);

			} else if (gameBoard[i][0] + gameBoard[i][1] + gameBoard[i][2] == 3) {
				WinnerX = true;
				spaces[i][0].setBackground(Color.YELLOW);
				spaces[i][1].setBackground(Color.YELLOW);
				spaces[i][2].setBackground(Color.YELLOW);
			}
		}

		// Win over diagonal?
		if (gameBoard[0][0] + gameBoard[1][1] + gameBoard[2][2] == 3) {
			WinnerX = true;
			spaces[0][0].setBackground(Color.YELLOW);
			spaces[1][1].setBackground(Color.YELLOW);
			spaces[2][2].setBackground(Color.YELLOW);
		} else if (gameBoard[2][0] + gameBoard[1][1] + gameBoard[0][2] == 3) {
			WinnerX = true;
			spaces[2][0].setBackground(Color.YELLOW);
			spaces[1][1].setBackground(Color.YELLOW);
			spaces[0][2].setBackground(Color.YELLOW);
		} else if (gameBoard[0][0] + gameBoard[1][1] + gameBoard[2][2] == -3) {
			WinnerO = true;
			spaces[0][0].setBackground(Color.YELLOW);
			spaces[1][1].setBackground(Color.YELLOW);
			spaces[2][2].setBackground(Color.YELLOW);
		} else if (gameBoard[2][0] + gameBoard[1][1] + gameBoard[0][2] == -3) {
			WinnerO = true;
			spaces[2][0].setBackground(Color.YELLOW);
			spaces[1][1].setBackground(Color.YELLOW);
			spaces[0][2].setBackground(Color.YELLOW);
		}

		// Displays winner dialog
		int replay;
		if (WinnerX) {
			replay = JOptionPane.showConfirmDialog(null,
					"         PLAYER X WINS\n\n             Play again?",
					"Tic-Tac-Nole", 0);
			if (replay == 0)
				reset();
		} else if (WinnerO) {
			replay = JOptionPane.showConfirmDialog(null,
					"         PLAYER O WINS\n\n             Play again?",
					"Tic-Tac-Nole", 0);
			if (replay == 0)
				reset();
		} else if (movesLeft <= 0 && inPlay) {
			replay = JOptionPane.showConfirmDialog(null,
					"               TIE GAME\n\n             Play again?",
					"Tic-Tac-Nole", 0);
			if (replay == 0)
				reset();
		}
	} // end winCheck

	// Resets game board, resets each matrix space
	public void reset() {
		gameBoard = new int[3][3];
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				spaces[i][j].setForeground(gold);
				spaces[i][j].setBackground(garnet);
				spaces[i][j].setText("");
				spaces[i][j].setEnabled(true);
			}
		}
		WinnerX = false;
		WinnerO = false;
		inPlay = true;
	}
}
