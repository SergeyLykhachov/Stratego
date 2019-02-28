package com.yahoo.slykhachov.strategone.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.yahoo.slykhachov.strategone.model.IAdversary;
import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.Move;

public class MoveChooser {
	public static final boolean MAXIMIZER = true;
	public static final boolean MINIMIZER = false;
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	private MoveChooser() {}
	public static Best parallelChooseMove(IAdversary adversary,
			BoardModel board, int splitDepth, int searchDepth) {
		if (splitDepth == 0) {
			return miniMaxAlphaBeta(
				MoveChooser.MAXIMIZER,
				adversary,
				adversary,
				board,
				Integer.MIN_VALUE, 
				Integer.MAX_VALUE,
				searchDepth
			);
		} else {
			Future<Best> bestFuture = divideAndConquer(
				MAXIMIZER,
				adversary.getClass(),
				adversary.getClass(),
				null,
				board,
				splitDepth,
				searchDepth
			);
			Best best = null;
			try {
				best = bestFuture.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			return best;
		}	   
	}
	private static Future<Best> divideAndConquer(boolean side,
			Class<? extends IAdversary> adversaryClassInfo,
			Class<? extends IAdversary> alternatingAdversaryClassInfo,
			Move parentThreadMove, BoardModel board, int splitDepth, int searchDepth) {
		BoardModel boardCopy = BoardModel.newInstanceFactory(board);
		IAdversary[] newAdversaries = boardCopy.getAdversaries();
		IAdversary newAdversary = adversaryClassInfo.equals(newAdversaries[0].getClass()) ? newAdversaries[0] : newAdversaries[1];
		IAdversary newAlternatingAdversary = alternatingAdversaryClassInfo.equals(newAdversaries[0].getClass()) ? newAdversaries[0] : newAdversaries[1];
		if (parentThreadMove != null) {
			boardCopy.performMove(Move.moveFactory(parentThreadMove, boardCopy));
		}
		if (newAlternatingAdversary.hasLost(boardCopy)) {
			Callable<Best> callable;
			if (newAlternatingAdversary == newAdversary) {
				callable = () -> {
					Best finale = new Best();
					finale.setScore(Integer.MIN_VALUE);
					finale.setMove(parentThreadMove);
					return finale;
				};
			} else {
				callable = () -> {
					Best finale = new Best();
					finale.setScore(Integer.MAX_VALUE);
					finale.setMove(parentThreadMove);
					return finale;
				};
			}
			Future<Best> future = MoveChooser.pool.submit(callable);
			return future;
		} else {
			if (splitDepth == 0) {
				Callable<Best> callable = () -> {
					Best best = miniMaxAlphaBeta(
						side,
						newAdversary,
						newAlternatingAdversary,
						boardCopy,
						Integer.MIN_VALUE,
						Integer.MAX_VALUE,
						searchDepth
					);
					best.setMove(parentThreadMove);
					return best;
				};
				Future<Best> future = MoveChooser.pool.submit(callable);
				return future;
			} else {
				List<Move> moves = newAlternatingAdversary.generateAllPossibleMoves(boardCopy);
				List<Callable<Best>> tasks = new ArrayList<>();
				for (Move move : moves) {
					tasks.add(
						() -> {
							Future<Best> future = divideAndConquer(
								!side,
								adversaryClassInfo,
								newAlternatingAdversary.getOpponent().getClass(),
								move,
								boardCopy,
								splitDepth - 1,
								searchDepth - 1	
							);				
							Best best = null;
							try {
								best = future.get();
								best.setMove(move);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
							return best;					
						}    
					);
				}
				List<Future<Best>> futuresOfBest = null;
				try {
					futuresOfBest = pool.invokeAll(tasks);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return getMaxFuture(
					futuresOfBest,
					side == MAXIMIZER ? Integer::compare : (e1, e2) -> Integer.compare(e2, e1)
				);
			}
		}
	}
	private static Future<Best> getMaxFuture(List<Future<Best>> list,
			Comparator<Integer> comparator) {
		Future<Best> maxFuture = list.get(0);
		int maxScore = 0;
		try {
			maxScore = maxFuture.get().getScore();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
	    	e.printStackTrace();
	    }
		for (int i = 1; i < list.size(); i++) {
			Future<Best> currentFuture = list.get(i);
			int currentScore = 0;
			try {
				currentScore = currentFuture.get().getScore();
			} catch (InterruptedException e) {
		        e.printStackTrace();
			} catch (ExecutionException e) {
	    	    e.printStackTrace();
	    	}
			if (comparator.compare(currentScore, maxScore) > 0) {
				maxScore = currentScore;
				maxFuture = currentFuture;
			}
		}
		return maxFuture;
	}
	public static Best miniMaxAlphaBeta(boolean side, IAdversary adversary,
			IAdversary alternatingAdversary, BoardModel board, int alpha, int beta,
			int depth) {
		if (alternatingAdversary.hasLost(board)) {
			Best finale = new Best();
			if (alternatingAdversary == adversary) {
				finale.setScore(Integer.MIN_VALUE);
				return finale;
			} else {
				finale.setScore(Integer.MAX_VALUE);
				return finale;
			}
		} else {
			if (depth == 0) {
				Best finale = new Best();
				finale.setScore(StaticEvaluator.evaluate(board, adversary));
				return finale;
			}
		}	
		Best myBest = new Best();
		Best reply;               
		if (side == MAXIMIZER) {
			myBest.setScore(alpha);
		} else {
			myBest.setScore(beta);
		}	
		List<Move> moves = alternatingAdversary.generateAllPossibleMoves(board);
		presortMoves(moves, board, alternatingAdversary);
		for (Move move : moves) {
			board.performMove(move);
			reply = miniMaxAlphaBeta(
				!side,
				adversary,
				alternatingAdversary.getOpponent(),
				board,
				alpha,
				beta,
				depth - 1
			);
			board.undoMove();
			if ((side == MAXIMIZER)
			    	&& (reply.getScore() > myBest.getScore())) {
				myBest.setMove(move);
				myBest.setScore(reply.getScore());
				alpha = reply.getScore();
			} else {
				if ((side == MINIMIZER)
						&& (reply.getScore() < myBest.getScore())) {
					myBest.setMove(move);
					myBest.setScore(reply.getScore());
					beta = reply.getScore();
				}
			}
			if (alpha >= beta) {
				return myBest;
			}
		}
		return myBest;
	}
	private static void presortMoves(List<Move> list, BoardModel bModel,
			IAdversary adv) {
		int[] scores = new int[list.size()];
		for (int i = 0; i < scores.length; i++) {
			bModel.performMove(list.get(i));
			scores[i] = StaticEvaluator.evaluate(bModel, adv);
			bModel.undoMove();
		}
		int indexOfMax = 0;
		int numberOfBest = Math.min(5, scores.length);
		for (int i = 0; i < numberOfBest; i++) {
			int max = Integer.MIN_VALUE;
			int currentIterationIndexOfMax = 0;
			for (int j = indexOfMax; j < scores.length; j++) {
				int temp = scores[j];
				if (temp > max) {
					max = temp;
					currentIterationIndexOfMax = j;
				}
			}
			Move maxMove = list.get(currentIterationIndexOfMax);
			list.set(
				currentIterationIndexOfMax,
				list.get(indexOfMax)
			);
			list.set(
				indexOfMax,
				maxMove
			);
			indexOfMax++;
		}
	}
}
