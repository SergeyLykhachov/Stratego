package com.yahoo.slykhachov.strategone.model;

import java.util.Stack;
import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.material.*;

public class BoardModel {
	private final IPieceModel[][] board;
	private Stack<Tuple> stack;
	private IAdversary adversary1;
	private IAdversary adversary2;
	public BoardModel() {
		this.board = new IPieceModel[10][10];
		this.stack = new Stack<>();
	}
	public void setAdversaries(IAdversary adv1, IAdversary adv2) {
		this.adversary1 = adv1;
		this.adversary2 = adv2;
		loadPieces(adversary1, this.board);
		loadPieces(adversary2, this.board);
		this.adversary1.setOpponent(this.adversary2);
		this.adversary2.setOpponent(this.adversary1);
	}
	private static void loadPieces(IAdversary adv, IPieceModel[][] board) {
		if (adv.getPieces().length != 0) {
			for (IPieceModel piece : adv.getPieces()) {
				if (!piece.isCaptured()) {
					board[piece.getRow()][piece.getCol()] = piece;
				}
			}
		}
	}
	public static BoardModel newInstanceFactory(BoardModel boardModel) {
		BoardModel newInstance = new BoardModel();
		IAdversary[] adversaries = boardModel.getAdversaries();
		IAdversary adv1 = adversaries[0].getClass().equals(Red.class) ? new Red((Red) adversaries[0]) : new Blue((Blue) adversaries[0]);
		IAdversary adv2 = adversaries[1].getClass().equals(Red.class) ? new Red((Red) adversaries[1]) : new Blue((Blue) adversaries[1]);
		newInstance.setAdversaries(
			adv1,
			adv2
		);
		return newInstance;
	}
	public void clearStack() {
		while (!this.stack.isEmpty()) {
			this.stack.pop();
		}
	}
	public void clearBoard() {
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {
				this.board[i][j] = null;
			}
		}
	}
	public boolean isSafeToUndoOneMove() {
		return this.stack.size() >= 1;
	}
	//public boolean isSafeToUndoTwoMoves() {
	//	return this.stack.size() >= 2;
	//}
	public int getNumberOfMovesPerformed() {
		return this.stack.size();
	}
	public Class<? extends IAdversary> getLastMoveAdversary() {
		if (this.stack.size() > 0) {
			if (this.stack.peek()
					.getMove()
					.getPiece()
					.getAdversary()
					.equals(Red.class)) {
				return Red.class;
			} else {
				return Blue.class;
			}
		}
		return null;
	}
	public void performMove(Move move) {
		int finRow = move.getFinalRow();
		int finCol = move.getFinalColumn();
		IPieceModel pieceOnTheMove = move.getPiece();
		IPieceModel pieceOnDefence = this.board[finRow][finCol];
		Tuple tuple;
		if (pieceOnDefence == null) {
			pieceOnTheMove.setRow(finRow);
		    pieceOnTheMove.setCol(finCol);
			this.board[finRow][finCol] = pieceOnTheMove;
			tuple = new Tuple(move, null, null);
		} else {
			if (pieceOnDefence.getClass().equals(Flag.class)) {
				pieceOnDefence.setCaptured(true);
				pieceOnTheMove.setRow(finRow);
		    	pieceOnTheMove.setCol(finCol);
		    	this.board[finRow][finCol] = pieceOnTheMove;
				tuple = new Tuple(move, null, pieceOnDefence);
			} else {
				if (pieceOnDefence.getClass().equals(Bomb.class)) {
					if (pieceOnTheMove.getClass().equals(Miner.class)) {
						pieceOnDefence.setCaptured(true);
						pieceOnTheMove.setRow(finRow);
		    			pieceOnTheMove.setCol(finCol);
						this.board[finRow][finCol] = pieceOnTheMove;
						tuple = new Tuple(move, null, pieceOnDefence);
					} else {
						pieceOnTheMove.setCaptured(true);
						tuple = new Tuple(move, pieceOnTheMove, null);
					}
				} else {
					if (pieceOnTheMove.getClass().equals(Spy.class)) {
						if (pieceOnDefence.getClass().equals(Marshall.class)) {
							pieceOnDefence.setCaptured(true);
							pieceOnTheMove.setRow(finRow);
		    				pieceOnTheMove.setCol(finCol);
							this.board[finRow][finCol] = pieceOnTheMove;
							tuple = new Tuple(move, null, pieceOnDefence);
						} else {
							if (pieceOnDefence.getClass().equals(Spy.class)) {
								pieceOnTheMove.setCaptured(true);
								pieceOnDefence.setCaptured(true);
								tuple = new Tuple(move, pieceOnTheMove, pieceOnDefence);
							} else {
								pieceOnTheMove.setCaptured(true);
								tuple = new Tuple(move, pieceOnTheMove, null);
							}
						}
					} else {
						if (pieceOnTheMove.getRank() == pieceOnDefence.getRank()) {
							pieceOnTheMove.setCaptured(true);
							pieceOnDefence.setCaptured(true);
							this.board[finRow][finCol] = null;
							tuple = new Tuple(move, pieceOnTheMove, pieceOnDefence);
						} else {
							if (pieceOnTheMove.getRank() > pieceOnDefence.getRank()) {
								pieceOnDefence.setCaptured(true);
								pieceOnTheMove.setRow(finRow);
		    					pieceOnTheMove.setCol(finCol);
								this.board[finRow][finCol] = pieceOnTheMove;
								tuple = new Tuple(move, null, pieceOnDefence);
							} else {
								pieceOnTheMove.setCaptured(true);
								tuple = new Tuple(move, pieceOnTheMove, null);
							}
						}
					}
				}
			}
		}
		this.board[move.getInitRow()][move.getInitColumn()] = null;
		stack.push(tuple);
	}
	public void undoMove() {
		Tuple tuple = stack.pop();
		Move move = tuple.getMove();
		IPieceModel piece = move.getPiece();
		IPieceModel movingPiece = tuple.getMovingPiece();
		IPieceModel onDefencePiece = tuple.getOnDefencePiece();
		int initRow = move.getInitRow();
		int initCol = move.getInitColumn();
		int finRow = move.getFinalRow();
		int finCol = move.getFinalColumn();
		if ((movingPiece == null) && (onDefencePiece == null)) {
			piece.setRow(initRow);
			piece.setCol(initCol);
			this.board[initRow][initCol] = piece;
			this.board[finRow][finCol] = null;
		} else {
			if ((movingPiece == null) && (onDefencePiece != null)) {
				piece.setRow(initRow);
				piece.setCol(initCol);
				this.board[initRow][initCol] = piece;
				onDefencePiece.setCaptured(false);
				this.board[finRow][finCol] = onDefencePiece;
			} else {
				if ((movingPiece != null) && (onDefencePiece == null)) {
					piece.setCaptured(false);
					piece.setRow(initRow);
					piece.setCol(initCol);
					this.board[initRow][initCol] = piece;
				} else {
					piece.setCaptured(false);
					piece.setRow(initRow);
					piece.setCol(initCol);
					this.board[initRow][initCol] = piece;
					onDefencePiece.setCaptured(false);
					this.board[finRow][finCol] = onDefencePiece;
				}
			}
		}
	}
	public IPieceModel[][] getBoard() {
		return this.board;
	}
	public IAdversary[] getAdversaries() {
		return new IAdversary[] {adversary1, adversary2};
	}
	public static String staticToString(Object[][] board) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t");
		for (int i = 0, row = 0; i < board.length; i++, row++) {
			if (i == 0) {
				for (int numOfLetters = 0, letter = 0x30; numOfLetters < 10; numOfLetters++, letter++) {
					sb.append("   " + (char) letter);
				}
				sb.append("\n\t  ---------------------------------------\n\t");
			}
			for (int k = 0; k < board[i].length; k++) {
				if (board[i][k] != null) {	
					if (k == 0) {
						sb.append(row + "|");
					}
					if (board[i][k].toString().length() == 3) {
						sb.append(board[i][k] + "|");
					} else {
						sb.append(board[i][k] + " |");
					}
				} else {
					if (k == 0) {
						sb.append(row + "|");
					}
					if (isLake(i, k)) {
						sb.append(" X |");
					} else {
						sb.append("   |");
					}
				}	
			}
			sb.append(row);
			sb.append("\n");
			sb.append("\t");
			if (i < board.length - 1) {
				sb.append(" |---|---|---|---|---|---|---|---|---|---|\n\t");
			} else {
				sb.append("  ---------------------------------------\n\t");
				for (int numOfLetters = 0, letter = 0x30; numOfLetters < 10; numOfLetters++, letter++) {
					sb.append("   " + (char) letter);
				}
			}
		}
		return sb.toString();
	}
	private static boolean isLake(int row, int col) {
		return ((row == 4) || (row == 5)) 
			&& ((col == 2) || (col == 3) || (col == 6) || (col == 7));
	}
	@Override
	public String toString() {
		return staticToString(this.board);
	}
	private static class Tuple {
		private Move move;
		private IPieceModel movingPiece;
		private IPieceModel onDefencePiece;
		Tuple(Move move, IPieceModel movingPiece,
				IPieceModel onDefencePiece) {
			this.move = move;
			this.movingPiece = movingPiece;
			this.onDefencePiece = onDefencePiece;
		}
		private Move getMove() {
			return this.move;
		}
		private IPieceModel getMovingPiece() {
			return this.movingPiece;
		}
		private IPieceModel getOnDefencePiece() {
			return this.onDefencePiece;
		}
	}
}
