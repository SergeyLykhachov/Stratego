package com.yahoo.slykhachov.strategone.model.material;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.*;

public class Spy extends AbstractPieceModel {
	public Spy(int row, int col, Class<? extends IAdversary> adversary) {
		super(row, col, adversary, 1);
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel board) {
		return MoveGenerator.standardMoves(board,this);
	}
	@Override
	public String toString() {
		String s = getAdversary().equals(Blue.class) ? "B" : "R";
		return s + "S";
	}
}
