package com.yahoo.slykhachov.strategone.model.material;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.*;

public class Scout extends AbstractPieceModel {
	public Scout(int row, int col, Class<? extends IAdversary> adversary) {
		super(row, col, adversary, 2);
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel board) {
		return MoveGenerator.scoutMoves(board, this);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Blue.class) ? "B" : "R";
		return s + "9";
	}
}
