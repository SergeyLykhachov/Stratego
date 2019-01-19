package com.yahoo.slykhachov.strategone;

import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.model.Red;
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
	public String toDisplayableString() {
		char initialRank = convertRowToRank(initRow);//row
		char initialFile  = convertColumnToFile(initCol);//column
		char finalRank = convertRowToRank(finalRow);
		char finalFile = convertColumnToFile(finalCol);
		return (this.piece.getAdversary().equals(Red.class) ? "RED" : "BLUE")
			+ " (" + initialFile + " " 
			+ (initialRank == '0' ? "10" : initialRank)
			+ ")->(" + finalFile + " "
			+ (finalRank == '0' ? "10" : finalRank) + ")";
	}
	private static char convertRowToRank(int row) {
		switch (row) {
			case 0:
				return '0';
			case 1:
				return '9';
			case 2:
				return '8';
			case 3:
				return '7';
			case 4:
				return '6';
			case 5:
				return '5';
			case 6:
				return '4';
			case 7:
				return '3';
			case 8:
				return '2';
			case 9:
				return '1';
			default:
				throw new IllegalArgumentException();
		}
	}
	private static char convertColumnToFile(int col) {
		switch (col) {
			case 0:
				return 'A';
			case 1:
				return 'B';
			case 2:
				return 'C';
			case 3:
				return 'D';
			case 4:
				return 'E';
			case 5:
				return 'F';
			case 6:
				return 'G';
			case 7:
				return 'H';
			case 8:
				return 'I';
			case 9:
				return 'J';
			default:
				throw new IllegalArgumentException();
		}
	}
	@Override
	public String toString() {
		return this.piece.toString() + " (" + this.initRow + " " + this.initCol
			+ ")->(" + this.finalRow + " " + this.finalCol + ")";
	}
}
