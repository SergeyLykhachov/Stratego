package com.yahoo.slykhachov.strategone.model.material;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.*;

public class Flag extends AbstractPieceModel {
	public Flag(int row, int col, Class<? extends IAdversary> adversary) {
		super(row, col, adversary, 0);
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel board) {
		return new Move[0];
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Blue.class) ? "B" : "R";
		return s + "F";
	}
}
