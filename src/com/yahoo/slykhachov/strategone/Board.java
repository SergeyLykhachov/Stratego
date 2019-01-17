package com.yahoo.slykhachov.strategone;

import com.yahoo.slykhachov.strategone.model.BoardModel;
import com.yahoo.slykhachov.strategone.view.BoardView;
import com.yahoo.slykhachov.strategone.view.SingleUserBoardView;

public class Board {
	private BoardView boardView;
	private BoardModel boardModel;
	private SingleUserBoardView singleUserBoardView;
	public Board(StrategoneGame strategoneGame) {
		setBoardModel(new BoardModel());
		setBoardView(new BoardView(strategoneGame, getBoardModel()));
		setSingleUserBoardView(new SingleUserBoardView(strategoneGame, getBoardModel()));
	}
	public void setBoardView(BoardView boardView) {
		this.boardView = boardView;
	}
	public BoardView getBoardView() {
		return this.boardView;
	}
	public void setSingleUserBoardView(SingleUserBoardView singleUserBoardView) {
		this.singleUserBoardView = singleUserBoardView;
	}
	public SingleUserBoardView getSingleUserBoardView() {
		return this.singleUserBoardView;
	}
	public void setBoardModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
}
