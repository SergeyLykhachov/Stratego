package com.yahoo.slykhachov.strategone.model.material;

import  com.yahoo.slykhachov.strategone.Move;
import  com.yahoo.slykhachov.strategone.model.*;

public class Lieutenant extends AbstractPieceModel {
	public Lieutenant(int row, int col, Class<? extends IAdversary> adversary) {
		super(row, col, adversary, 5);
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel board) {
		return MoveGenerator.standardMoves(board, this);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Blue.class) ? "B" : "R";
		return s + "6";
	}
}
