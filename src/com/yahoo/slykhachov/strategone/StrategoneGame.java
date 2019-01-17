package com.yahoo.slykhachov.strategone;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.yahoo.slykhachov.strategone.ai.Best;
import com.yahoo.slykhachov.strategone.ai.MoveChooser;
import com.yahoo.slykhachov.strategone.model.StrategoneGameModel;
import com.yahoo.slykhachov.strategone.model.Red;
import com.yahoo.slykhachov.strategone.model.Blue;
import com.yahoo.slykhachov.strategone.model.IAdversary;
import com.yahoo.slykhachov.strategone.view.PieceView;
import com.yahoo.slykhachov.strategone.view.StrategoneGameView;

public class StrategoneGame {
	public static int SEARCH_DEPTH = 7;
	private StrategoneGameModel strategoneGameModel;
	private StrategoneGameView strategoneGameView;
	private Board board;
	private IAdversary computerAdversary;
	private IAdversary redPlayer;
	private IAdversary bluePlayer;
	private IAdversary adversaryToMove;
	private ExecutorService pool;
	public StrategoneGame() {
		this.pool = Executors.newFixedThreadPool(1);
		this.setBluePlayer(new Blue());
		this.setRedPlayer(new Red());
		this.setBoard(new Board(this));
		this.setStrategoneGameModel(new StrategoneGameModel(getBoard().getBoardModel()));
		this.setStrategoneGameView(new StrategoneGameView(this));
	    this.setAdversaryToMove(redPlayer);
	    this.setComputerAdversary(bluePlayer);
	}
	public void doResponce() {
		if (isGameOver(getBoard())) {
			if (getBluePlayer().hasLost(getBoard().getBoardModel())) {
				System.out.println("Human player won");
				setAdversaryToMove(
					getAdversaryToMove().getOpponent()
				);
				return;
			} else {
				System.out.println("Computer player won");
				setAdversaryToMove(
					getAdversaryToMove().getOpponent()
				);
				return;
			}
		} else {
			pool.submit(
				() -> {
					Best best = MoveChooser.parallelChooseMove(
						this.getComputerAdversary(),
						this.getBoard().getBoardModel(),
						1,
						7
					);
					Move move = Move.moveFactory(
						best.getMove(),
						this.getBoard().getBoardModel()
					);
					System.out.println(best.getScore());
					System.out.println(best.getMove());
					if (move != null) {
						if (getBoard().getBoardModel()
								.getBoard()[move.getFinalRow()][move.getFinalColumn()] != null) {
							doSplash(move);
						}
						getBoard().getBoardModel().performMove(move);
						System.out.println(getBoard().getBoardModel());
						getBoard().getBoardView().updateBoardView();
						if (isGameOver(getBoard())) {
							System.out.println("Computer player won");
							setAdversaryToMove(
								getAdversaryToMove().getOpponent()
							);
							return;
						}
						setAdversaryToMove(
							getAdversaryToMove().getOpponent()
						);
					} else {
						Best secondTry = MoveChooser.miniMaxAlphaBeta(
							MoveChooser.MAXIMIZER,
							getComputerAdversary(),
							getComputerAdversary(),
							getBoard().getBoardModel(),
							Integer.MIN_VALUE, 
							Integer.MAX_VALUE,
							1
						);
						Move moveOfHope = secondTry.getMove();
						for (int i = 2; i < SEARCH_DEPTH; i++) {
							secondTry = MoveChooser.miniMaxAlphaBeta(
								MoveChooser.MAXIMIZER,
								getComputerAdversary(),
								getComputerAdversary(),
								getBoard().getBoardModel(),
								Integer.MIN_VALUE, 
								Integer.MAX_VALUE,
								i
							);
							if (secondTry.getMove() != null) {
								moveOfHope = secondTry.getMove();
							} else {
								break;
							}
						}
						if (getBoard().getBoardModel()
								.getBoard()[moveOfHope.getFinalRow()][moveOfHope.getFinalColumn()] != null) {     
							doSplash(moveOfHope);
						}
						getBoard().getBoardModel().performMove(moveOfHope);
						getBoard().getBoardView().updateBoardView();
						setAdversaryToMove(
							getAdversaryToMove().getOpponent()
						);
					}	
				}
			);
		}
	}
	private void doSplash(Move move) {
		PieceView pv = getBoard()
			.getBoardView()
			.getViews()
			.get(String.valueOf(move.getInitRow()) + String.valueOf(move.getInitColumn()));
		pv.setPoint(
			getBoard().getBoardView()
				.getViews()
				.get(String.valueOf(move.getFinalRow()) + String.valueOf(move.getFinalColumn()))
				.getPoint()
		);
		getBoard().getBoardView().setSplashPieceView(pv);
		getBoard().getBoardView().chargeSplashEndTimer();
	}
	public boolean isGameOver(Board bm) {
		return bluePlayer.hasLost(bm.getBoardModel()) || redPlayer.hasLost(bm.getBoardModel());
	}
	public IAdversary getComputerAdversary() {
		return this.computerAdversary;
	}
	public void setComputerAdversary(IAdversary adv) {
		this.computerAdversary = adv;
	}
	public void setAdversaryToMove(IAdversary adv) {
		this.adversaryToMove = adv;
	}
	public IAdversary getAdversaryToMove() {
		return this.adversaryToMove;
	}
	public Board getBoard() {
		return this.board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public void setBluePlayer(IAdversary adv) {
		this.bluePlayer = adv;
	}
	public void setRedPlayer(IAdversary adv) {
		this.redPlayer = adv;
	}
	public IAdversary getRedPlayer() {
		return this.redPlayer;
	}
	public IAdversary getBluePlayer() {
		return this.bluePlayer;
	}
	public StrategoneGameModel getStrategoneGameModel() {
		return this.strategoneGameModel;
	}
	private void setStrategoneGameModel(StrategoneGameModel strategoneGameModel) {
		this.strategoneGameModel = strategoneGameModel;
	}
	public StrategoneGameView getStrategoneGameView() {
		return this.strategoneGameView;
	}
	private void setStrategoneGameView(StrategoneGameView strategoneGameView) {
		this.strategoneGameView = strategoneGameView;
	}
}
