package com.yahoo.slykhachov.strategone.ai;

import com.yahoo.slykhachov.strategone.Move;

public class Best {
	private Move move;
	private int score;
	public void setMove(Move move) {
		this.move = move;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Move getMove() {
		return move;
	}
	public int getScore() {
		return score;
	}
}
