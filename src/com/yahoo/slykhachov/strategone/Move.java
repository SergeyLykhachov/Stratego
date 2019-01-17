package com.yahoo.slykhachov.strategone;

import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.model.material.IPieceModel;

public class Move {
	private int initRow;
	private int initCol;
	private int finalRow;
	private int finalCol;
	private IPieceModel piece;
	public Move(IPieceModel piece, int initRow, int initCol,
			int finalRow, int finalCol) {
		this.piece = piece;
		this.initRow = initRow;
		this.initCol = initCol;
		this.finalRow = finalRow;
		this.finalCol = finalCol;
	}
	public static Move moveFactory(Move move, BoardModel boardModel) {
		return new Move(
			boardModel.getBoard()[move.getInitRow()][move.getInitColumn()],
			move.getInitRow(),
			move.getInitColumn(),
			move.getFinalRow(),
			move.getFinalColumn()
		);
	}
	public IPieceModel getPiece() {
		return this.piece;
	}
	public int getInitRow() {
		return this.initRow;
	}
	public int getInitColumn() {
		return this.initCol;
	}
	public int getFinalRow() {
		return this.finalRow;
	}
	public int getFinalColumn() {
		return this.finalCol;
	}
	@Override 
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Move other = (Move) obj;
        if (this.initRow != other.initRow) {
            return false;
        } else {
        	if (this.initCol != other.initCol) {
        		return false;
        	} else {
        		if (this.finalRow != other.finalRow) {
        			return false;
        		} else {
        			if (this.finalCol != other.finalCol) {
        				return false; 
        			} else {
        				if (this.piece != other.piece) {
        					return false;
        				}
        			}
        		}
        	}
        }
        return true;
	}
	@Override
	public String toString() {
		return piece.toString() + " (" + initRow + " " + initCol + ")->("
			+ finalRow + " " + finalCol + ")";
	}
}
