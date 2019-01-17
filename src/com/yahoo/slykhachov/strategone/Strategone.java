package com.yahoo.slykhachov.strategone;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Strategone {
	private StrategoneGame strategoneGame;
	public Strategone() {
		setStrategoneGame(new StrategoneGame());
		JFrame jf = new JFrame("Strategone");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(getStrategoneGame().getStrategoneGameView(), BorderLayout.CENTER);
		jf.pack();
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	public static void main(String[] sa) {
		EventQueue.invokeLater(Strategone::new);
	}
	public StrategoneGame getStrategoneGame() {
		return this.strategoneGame;
	}
	public void setStrategoneGame(StrategoneGame strategoneGame) {
		this.strategoneGame = strategoneGame;
	}
}
