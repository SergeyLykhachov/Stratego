package com.yahoo.slykhachov.strategone.model;

import java.util.List;
import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.material.IPieceModel;

public interface IAdversary {
	IPieceModel[] getPieces();
	int getNumberOfUncapturedPieces();
	IAdversary getOpponent();
	void setOpponent(IAdversary a);
	boolean hasLost(BoardModel bm);
	void setPieces(IPieceModel[] pieces);
	List<Move> generateAllPossibleMoves(BoardModel board);
}