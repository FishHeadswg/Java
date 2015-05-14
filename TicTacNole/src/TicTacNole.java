/*
Richardson, Trevor
COP-3252
Project 1
02-24-14

TicTacNole.java - Creates a tic-tac-toe board with various custom settings

GUI implementation borrows ideas from Section 27.8 of Deitel and:
http://forum.codecall.net/topic/43209-java-source-code-tic-tac-toe-game
(Nothing is a direct copy-paste from either source)
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.io.*;
import javax.imageio.ImageIO;

public class TicTacNole {

	final static String VERSION = "1.0";

	// Contains a frame for the board and another for the settings
	private static Board board;
	private static JFrame settings;
	private static Color garnet = new Color(140, 0, 12);

	public static void main(String[] args) {

		// Load title/footer images
		BufferedImage titleImage = null;
		BufferedImage footerImage = null;
		// Makes sure files are loaded correctly
		try {
			titleImage = ImageIO.read(new File("title.jpg"));
			footerImage = ImageIO.read(new File("footer.jpg"));
		} catch (IOException ex) {
		}

		// Create main window
		JFrame window = new JFrame();
		window.setTitle("Tic-Tac-Nole (Ver " + VERSION + ")"); // title
		window.setSize(400, 599); // window size
		window.setLocation(600, 240); // window location
		window.setResizable(false); // resizing causes issues
		window.setJMenuBar(menu()); //
		window.setBackground(garnet); // garnet background (redundant?)
		window.setLayout(null);
		window.setVisible(true);

		// Title
		JLabel img;
		img = new JLabel(new ImageIcon(titleImage));
		img.setLocation(0, 0);
		img.setSize(400, 100);
		window.add(img);

		// Footer
		JLabel img2 = new JLabel(new ImageIcon(footerImage));
		img2.setLocation(0, 500);
		img2.setSize(400, 50);
		window.add(img2);

		// Main board (extended JPanel)
		board = new Board();
		board.setLocation(0, 100);
		board.setSize(394, 400); // Getting odd positioning issues
		window.add(board);

		// Settings window
		settings = new JFrame();
		settings.setTitle("Settings");
		settings.setSize(175, 175);
		settings.setLocation(710, 420);
		settings.setResizable(false);
		settings.setVisible(false);
		settings.setLayout(new FlowLayout());
		settings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Game type settings
		JPanel jpanel = new JPanel();
		TitledBorder title;
		title = BorderFactory.createTitledBorder("Game Type");
		jpanel.setBorder(title);
		jpanel.setLayout(new GridLayout(2, 1));
		JRadioButton player = new JRadioButton("Player vs Player", true);
		player.setActionCommand("player");
		JRadioButton comp = new JRadioButton("Player vs Comp", false);
		comp.setActionCommand("comp");
		opponent = new ButtonGroup();
		opponent.add(player);
		opponent.add(comp);
		jpanel.add(player);
		jpanel.add(comp);
		jpanel.setPreferredSize(new Dimension(150, 100));
		settings.add(jpanel);

		// Save settings
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				// save and close
				TicTacNole.saveSettings();
				settings.setVisible(false);
			}
		});

		// Discard and close
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// close
				settings.setVisible(false);

			}
		});
		JPanel jpanel2 = new JPanel();
		jpanel2.add(saveButton);
		jpanel2.add(cancelButton);
		settings.add(jpanel2);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// Save settings
	public static ButtonGroup opponent;

	private static void saveSettings() {
		board.saveSettings(opponent.getSelection().getActionCommand());
	}

	// Set up and return menu bar
	private static JMenuBar menu() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();

		// Game settings drop-down
		menu = new JMenu("Tic-Tac-Nole");
		menuBar.add(menu);

		// Start new game
		menuItem = new JMenuItem("New Game", KeyEvent.VK_N);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				board.reset();
			}
		});
		menu.add(menuItem);

		// Open settings menu
		menuItem = new JMenuItem("Settings");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				settings.setVisible(true);
			}
		});
		menu.add(menuItem);

		// Exit game
		menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menu.add(menuItem);

		// Misc drop-down
		menu = new JMenu("?");
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane
						.showMessageDialog(
								null,
								"Tic-Tac-Nole\nVersion 1.0\nCreated by Trevor Richardson",
								"Tic-Tac-Nole", 1);
			}
		});
		menu.add(menuItem);

		// Opens up TTT wiki page in browser
		menuItem = new JMenuItem("How to Play");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					String link = "http://en.wikipedia.org/wiki/Tic-tac-toe";
					java.awt.Desktop.getDesktop().browse(
							java.net.URI.create(link));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		menu.add(menuItem);

		menuBar.add(menu);

		// Opens up FSU site in browser
		menuItem = new JMenuItem("FSU Website");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					String link = "http://www.fsu.edu";
					java.awt.Desktop.getDesktop().browse(
							java.net.URI.create(link));
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});
		menu.add(menuItem);

		menuBar.add(menu);
		return menuBar;
	} // end menu()
	
} // end TicTacNole
