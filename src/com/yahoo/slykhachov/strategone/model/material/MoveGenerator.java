package com.yahoo.slykhachov.strategone.model.material;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.*;

class MoveGenerator {
	private MoveGenerator() {}
	static Move[] standardMoves(BoardModel board, IPieceModel pieceModel) {
		if (pieceModel.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		ArrayList<Move> list = new ArrayList<>();
		Move north = generateMove(
			pieceModel,
			board,
			pMdl -> pMdl.getRow() != 0,
			MoveDirection.VERTICAL,
			pMdl -> pMdl.getRow() - 1
		);
		if (north != null) {
			list.add(north);
		}
		Move south = generateMove(
			pieceModel,
			board,
			pMdl -> pMdl.getRow() != 9,
			MoveDirection.VERTICAL,
			pMdl -> pMdl.getRow() + 1
		);
		if (south != null) {
			list.add(south);
		}
		Move east = generateMove(
			pieceModel,
			board,
			pMdl -> pMdl.getCol() != 0,
			MoveDirection.HORIZONTAL,
			pMdl -> pMdl.getCol() - 1
		);
		if (east != null) {
			list.add(east);
		}
		Move west = generateMove(
			pieceModel,
			board,
			pMdl -> pMdl.getCol() != 9,
			MoveDirection.HORIZONTAL,
			pMdl -> pMdl.getCol() + 1
		);
		if (west != null) {
			list.add(west);
		}
		Move[] moves = new Move[list.size()];
		return list.toArray(moves);
	}
	private static Move generateMove(IPieceModel pieceModel, BoardModel board,
			Predicate<IPieceModel> predicate, MoveDirection moveDir,
			Function<IPieceModel, Integer> function) {
		if (predicate.test(pieceModel)) {
			int row;
			int col;
			if (moveDir.equals(MoveDirection.VERTICAL)) {
				row = function.apply(pieceModel);
				col = pieceModel.getCol();
			} else {
				row = pieceModel.getRow();
				col = function.apply(pieceModel);
			}
			if (isInLake(row, col)) {
				return null;
			}
			if (board.getBoard()[row][col] == null) {
				return new Move(
					pieceModel,
					pieceModel.getRow(),
					pieceModel.getCol(),
					row,
					col
				);
			} else {
				if (!board.getBoard()[row][col]
						.getAdversary()
						.equals(pieceModel.getAdversary())) {
					return new Move(
						pieceModel,
						pieceModel.getRow(),
						pieceModel.getCol(),
						row,
						col
					);
				} else {
					return null;
				}
			}
		}
		return null;
	}
	private static boolean isInLake(int row, int col) {
		return ((row == 4) || (row == 5)) 
			&& ((col == 2) || (col == 3) || (col == 6) || (col == 7));
	}
	static Move[] scoutMoves(BoardModel board, IPieceModel pieceModel) {
		if (pieceModel.isCaptured()) {
			throw new UnsupportedOperationException();
		}
		ArrayList<Move> list = new ArrayList<>();
		int northMoves = generateNumberOfMoves(
			pieceModel,
			board,
			(pMdl, val) -> isInLake(pMdl.getRow() + val, pMdl.getCol()),
			pMdl -> pMdl.getRow() != 0,
			e -> --e,
			e -> ++e,
			(brd, p, val) -> brd[p.getRow() + val][p.getCol()],
			(i, j) -> i > j,
			IPieceModel::getRow,
			0
		);
		//System.out.println("North moves: " + northMoves);
		addMoves(
			list,
			northMoves,
			pieceModel,
			(piece, number) -> piece.getRow() - number,
			(piece, number) -> piece.getCol()
		);
		int southMoves = generateNumberOfMoves(
			pieceModel,
			board,
			(pMdl, val) -> isInLake(pMdl.getRow() + val, pMdl.getCol()),
			pMdl -> pMdl.getRow() != 9,
			e -> ++e,
			e -> --e,
			(brd, p, val) -> brd[p.getRow() + val][p.getCol()],
			(i, j) -> i < j,
			IPieceModel::getRow,
			9
		);
		//System.out.println("South moves: " + southMoves);
		addMoves(
			list,
			southMoves,
			pieceModel,
			(piece, number) -> piece.getRow() + number,
			(piece, number) -> piece.getCol()
		);
		int eastMoves = generateNumberOfMoves(
			pieceModel,
			board,
			(pMdl, val) -> isInLake(pMdl.getRow(), pMdl.getCol() + val),
			pMdl -> pMdl.getCol() != 9,
			e -> ++e,
			e -> --e,
			(brd, p, val) -> brd[p.getRow()][p.getCol() + val],
			(i, j) -> i < j,
			IPieceModel::getCol,
			9
		);
		//System.out.println("East moves: " + eastMoves);
		addMoves(
			list,
			eastMoves,
			pieceModel,
			(piece, number) -> piece.getRow(),
			(piece, number) -> piece.getCol() + number
		);
		int westMoves = generateNumberOfMoves(
			pieceModel,
			board,
			(pMdl, val) -> isInLake(pMdl.getRow(), pMdl.getCol() + val),
			pMdl -> pMdl.getCol() != 0,
			e -> --e,
			e -> ++e,
			(brd, p, val) -> brd[p.getRow()][p.getCol() + val],
			(i, j) -> i > j,
			IPieceModel::getCol,
			0
		);
		//System.out.println("West moves: " + westMoves);
		addMoves(
			list,
			westMoves,
			pieceModel,
			(piece, number) -> piece.getRow(),
			(piece, number) -> piece.getCol() - number
		);
		Move[] moves = new Move[list.size()];
		return list.toArray(moves);
	}
	private static int generateNumberOfMoves(IPieceModel pieceModel,
			BoardModel board,
			BiPredicate<IPieceModel, Integer> isInLakePredicate,
			Predicate<IPieceModel> predicate,
			Function<Integer, Integer> advanceFunction,
			Function<Integer, Integer> retreatFunction,
			MyTriFunction<IPieceModel[][], IPieceModel, IPieceModel> squareMapper,
			BiPredicate<Integer, Integer> terminalCondition,
			Function<IPieceModel, Integer> pieceFunction,
			int lim) {
		int value = 0;
		if (predicate.test(pieceModel)) {	
 			do {
 				value = advanceFunction.apply(value);
 				if (isInLakePredicate.test(pieceModel, value)) {
 					return Math.abs(retreatFunction.apply(value));
 				}
 				IPieceModel p = squareMapper.apply(board.getBoard(), pieceModel, value);
 				if (p != null) {                                           
					if (!(p.getAdversary().equals(pieceModel.getAdversary()))) {
						return Math.abs(value);
					} else {
						return Math.abs(retreatFunction.apply(value));
					}
				}
 			} while (terminalCondition.test(pieceFunction.apply(pieceModel) + value, lim));
 		}
 		return Math.abs(value);
	}
	private static void addMoves(ArrayList<Move> list, int moves,
			IPieceModel piece, BiFunction<IPieceModel, Integer, Integer> rowFunction,
			BiFunction<IPieceModel, Integer, Integer> colFunction) {
		while (moves != 0) {
			list.add(
				new Move(
					piece,
					piece.getRow(),
					piece.getCol(),
					rowFunction.apply(piece, moves),
					colFunction.apply(piece, moves)
				)
			);
			moves--;
		}
	}
	private static enum MoveDirection {
		VERTICAL, HORIZONTAL
	}
	@FunctionalInterface
	interface MyTriFunction<A, B, C> {
		C apply(A a, B b, int val);
	}
}

