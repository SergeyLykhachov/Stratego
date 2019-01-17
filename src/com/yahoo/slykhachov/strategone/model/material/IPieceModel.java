package com.yahoo.slykhachov.strategone.model.material;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.model.IAdversary;

public interface IPieceModel {
	int getRow();
	int getCol();
	void setRow(int row);
	void setCol(int col);
	boolean isCaptured();
	void setCaptured(boolean b);
	int getRank();
	Class<? extends IAdversary> getAdversary();
	Move[] generateAllPossibleMoves(BoardModel board);
}
