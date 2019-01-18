package com.yahoo.slykhachov.strategone.model.material;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.model.IAdversary;

abstract class AbstractPieceModel implements IPieceModel {
	private int row;
	private int col;
	private boolean isCaptured;
	private int rank;
	private final Class<? extends IAdversary> adversary;
	public AbstractPieceModel(int row, int col,
			Class<? extends IAdversary> adversary, int rank) {
		this.isCaptured = false;
		this.adversary = adversary;
		this.rank = rank;
		this.row = row;
		this.col = col;
	}
	@Override
	public int getRank() {
		return this.rank;
	}
	@Override
	public boolean isCaptured() {
		return isCaptured;
	}
	@Override
	public void setCaptured(boolean b) {
		this.isCaptured = b;
	}
	@Override
	public void setRow(int row) {
		this.row = row;
	}
	@Override
	public void setCol(int col) {
		this.col = col;
	}
	@Override
	public int getRow() {
		return this.row;
	}
	@Override
	public int getCol() {
		return this.col;
	}
	@Override
	public Class<? extends IAdversary> getAdversary() {
		return this.adversary;
	}
	@Override
	public Move[] generateAllPossibleMoves(BoardModel board) {
		return new Move[0];
	}
}
