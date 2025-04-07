package spaceWar;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class main_Screen extends JFrame {
	private background background; // Đối tượng background duy nhất

	public main_Screen() {
		init();
	}

	private void init() {
		this.setTitle("SPACE_WAR");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setLayout(new BorderLayout());
		panelGame panelgame = new panelGame();
		this.add(panelgame);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				panelgame.start();

			}

		});
	}

	public static void main(String[] args) {
		main_Screen MAIN = new main_Screen();
		MAIN.setVisible(true);
	}
}
