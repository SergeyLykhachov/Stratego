package com.yahoo.slykhachov.strategone.model;

public class StrategoneGameModel {
	private BoardModel boardModel;
	public StrategoneGameModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
}