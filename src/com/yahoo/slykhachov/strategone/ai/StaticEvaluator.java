package com.yahoo.slykhachov.strategone.ai;

import java.util.function.Predicate;

import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.model.IAdversary;
import com.yahoo.slykhachov.strategone.model.material.*;

class StaticEvaluator {
	/*
	https://en.wikipedia.org/wiki/Evaluation_function#In_chess
	One popular strategy for constructing evaluation functions is as a weighted 
	sum of various factors that are thought to influence the value of a position. 
	For instance, an evaluation function for chess might take the form
	c1 * material + c2 * mobility + c3 * king safety + c4 * center control + ...
	Such as
	f(P) = 9(Q-Q') + 5(R-R') + 3(B-B'+N-N') + (P-P') - 0.5(D-D'+S-S'+I-I') + 0.1(M-M') + ...
	in which:
	Q, R, B, N, P are the number of white queens, rooks, bishops, knights and pawns 
	on the board.
	D, S, I are doubled, backward and isolated white pawns.
	M represents white mobility (measured, say, as the number of legal moves available to White).[1]
	*/
	static int evaluate(BoardModel board, IAdversary adversary) {
		return materialFactor(board, adversary)
			   + mobilityFactor(board, adversary);
	}
	private static int mobilityFactor(BoardModel board, IAdversary adversary) {
		return 10 * (adversary.generateAllPossibleMoves(board).size()
			- adversary.getOpponent().generateAllPossibleMoves(board).size());
	}
	private static int materialFactor(BoardModel board, IAdversary adversary) {
		IPieceModel[] maximizerPieces = adversary.getPieces();
		IPieceModel[] minimizerPieces = adversary.getOpponent().getPieces();
		int maximizerMarshallCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Marshall.class),
			1,
			maximizerPieces
		);
		int minimizerMarshallCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Marshall.class),
			1,
			minimizerPieces
		);
		int maximizerGeneralCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(General.class),
			1,
			maximizerPieces
		);
		int minimizerGeneralCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(General.class),
			1,
			minimizerPieces
		);
		int maximizerColonelCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Colonel.class),
			2,
			maximizerPieces
		);
		int minimizerColonelCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Colonel.class),
			2,
			minimizerPieces
		);
		int maximizerMajorCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Major.class),
			3,
			maximizerPieces
		);
		int minimizerMajorCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Major.class),
			3,
			minimizerPieces
		);
		int maximizerCaptainCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Captain.class),
			4,
			maximizerPieces
		);
		int minimizerCaptainCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Captain.class),
			4,
			minimizerPieces
		);
		int maximizerLieutenantCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Lieutenant.class),
			4,
			maximizerPieces
		);
		int minimizerLieutenantCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Lieutenant.class),
			4,
			minimizerPieces
		);
		int maximizerSergeantCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Sergeant.class),
			4,
			maximizerPieces
		);
		int minimizerSergeantCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Sergeant.class),
			4,
			minimizerPieces
		);
		int maximizerMinerCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Miner.class),
			5,
			maximizerPieces
		);
		int minimizerMinerCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Miner.class),
			5,
			minimizerPieces
		);
		int maximizerScoutCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Scout.class),
			8,
			maximizerPieces
		);
		int minimizerScoutCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Scout.class),
			8,
			minimizerPieces
		);
		int maximizerSpyCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Spy.class),
			1,
			maximizerPieces
		);
		int minimizerSpyCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Spy.class),
			1,
			minimizerPieces
		);
		int maximizerBombCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Bomb.class),
			6,
			maximizerPieces
		);
		int minimizerBombCount = findNonCapturedPieceCount(
			p -> p.getClass().equals(Bomb.class),
			6,
			minimizerPieces
		);
		return 100 * (maximizerMarshallCount - minimizerMarshallCount)
			   + 90 * (maximizerGeneralCount - minimizerGeneralCount)
			   + 80 * (maximizerColonelCount - minimizerColonelCount)
			   + 70 * (maximizerMajorCount - minimizerMajorCount)
			   + 60 * (maximizerCaptainCount - minimizerCaptainCount)
			   + 50 * (maximizerLieutenantCount - minimizerLieutenantCount)
			   + 40 * (maximizerSergeantCount - minimizerSergeantCount)
			   + 30 * (maximizerMinerCount - minimizerMinerCount)
			   + 20 * (maximizerScoutCount - minimizerScoutCount)
			   + 10 * (maximizerSpyCount - minimizerSpyCount)
			   + 60 * (maximizerBombCount - minimizerBombCount);
	}
	private static int findNonCapturedPieceCount(Predicate<IPieceModel> condition,
			 int maxCount, IPieceModel[] pieces) {
		int remainingInPlayCount = 0;
		int encounteredCount = 0;
		for (IPieceModel piece : pieces) {
			if (condition.test(piece)) {
				encounteredCount++;
				if (!piece.isCaptured()) {
					remainingInPlayCount++;
				}	
			}
			if (encounteredCount == maxCount) {
				break;
			}
		}
		return remainingInPlayCount;
	}
}
