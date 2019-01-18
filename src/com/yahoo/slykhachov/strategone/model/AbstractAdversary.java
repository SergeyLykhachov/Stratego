package com.yahoo.slykhachov.strategone.model;

import java.util.ArrayList;
import java.util.List;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.model.material.IPieceModel;
import com.yahoo.slykhachov.strategone.model.material.*;

abstract class AbstractAdversary implements IAdversary {
	private IPieceModel[] pieces;
	private IAdversary opponent;
	private Flag flag;
	protected AbstractAdversary() {
		pieces = new IPieceModel[0];
	}
	public AbstractAdversary(IAdversary adversaryToCopy) {
		IPieceModel[] originalPieces = adversaryToCopy.getPieces();
		IPieceModel[] copyPieces = new IPieceModel[originalPieces.length];
		for (int i = 0; i < copyPieces.length; i++) {
			IPieceModel originalPiece = originalPieces[i];
			IPieceModel copyPiece;
			if (originalPiece.getClass().equals(Scout.class)) {
				copyPiece = new Scout(
					originalPiece.getRow(),
					originalPiece.getCol(),
					originalPiece.getAdversary()
				);
			} else {
				if (originalPiece.getClass().equals(Bomb.class)) {
					copyPiece = new Bomb(
						originalPiece.getRow(),
						originalPiece.getCol(),
						originalPiece.getAdversary()
					);
				} else {
					if (originalPiece.getClass().equals(Miner.class)) {
						copyPiece = new Miner(
							originalPiece.getRow(),
							originalPiece.getCol(),
							originalPiece.getAdversary()
						);
					} else {
						if (originalPiece.getClass().equals(Sergeant.class)) {
							copyPiece = new Sergeant(
								originalPiece.getRow(),
								originalPiece.getCol(),
								originalPiece.getAdversary()
							);
						} else {
							if (originalPiece.getClass().equals(Lieutenant.class)) {
								copyPiece = new Lieutenant(
									originalPiece.getRow(),
									originalPiece.getCol(),
									originalPiece.getAdversary()
								);
							} else {
								if (originalPiece.getClass().equals(Captain.class)) {
									copyPiece = new Captain(
										originalPiece.getRow(),
										originalPiece.getCol(),
										originalPiece.getAdversary()
									);
								} else {
									if (originalPiece.getClass().equals(Major.class)) {
										copyPiece = new Major(
											originalPiece.getRow(),
											originalPiece.getCol(),
											originalPiece.getAdversary()
										);
									} else {
										if (originalPiece.getClass().equals(Colonel.class)) {
											copyPiece = new Colonel(
												originalPiece.getRow(),
												originalPiece.getCol(),
												originalPiece.getAdversary()
											);
										} else {
											if (originalPiece.getClass().equals(General.class)) {
												copyPiece = new General(
													originalPiece.getRow(),
													originalPiece.getCol(),
													originalPiece.getAdversary()
												);
											} else {
												if (originalPiece.getClass().equals(Marshall.class)) {
													copyPiece = new Marshall(
														originalPiece.getRow(),
														originalPiece.getCol(),
														originalPiece.getAdversary()
													);
												} else {
													if (originalPiece.getClass().equals(Spy.class)) {
														copyPiece = new Spy(
															originalPiece.getRow(),
															originalPiece.getCol(),
															originalPiece.getAdversary()
														);
													} else {
														copyPiece = new Flag(
															originalPiece.getRow(),
															originalPiece.getCol(),
															originalPiece.getAdversary()
														);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			if (originalPiece.isCaptured()) {
				copyPiece.setCaptured(true);
			}
			copyPieces[i] = copyPiece;
		}
		this.setPieces(copyPieces);
	}
	@Override
	public void setPieces(IPieceModel[] pieces) {
		this.pieces = pieces;
		for (IPieceModel ipm : pieces) {
			if (ipm.getClass().equals(Flag.class)) {
				this.flag = (Flag) ipm;
				break;
			}
		}
	}
	@Override
	public boolean hasLost(BoardModel bm) {
		return (this.flag.isCaptured() == true)
			|| (this.generateAllPossibleMoves(bm).size() == 0);
	}
	@Override
	public IPieceModel[] getPieces() {
		return this.pieces;
	}
	@Override
	public int getNumberOfUncapturedPieces() {
		int playerScore = 40;
		for(IPieceModel piece : this.getPieces()) {
			if(piece.isCaptured()) {
				playerScore--;
			}
		}
		return playerScore;
	}
	@Override
	public IAdversary getOpponent() {
		return this.opponent;
	}
	@Override 
	public void setOpponent(IAdversary a) {
		if (a == this) {
			throw new IllegalArgumentException();
		}
		this.opponent = a;
	}
	@Override 
	public List<Move> generateAllPossibleMoves(BoardModel board) {
		return generateMoves(board, this);
	}
	private static List<Move> generateMoves(BoardModel board, IAdversary adversary) {
		List<Move> list = new ArrayList<>();
		for (IPieceModel piece : adversary.getPieces()) {
			if (!piece.isCaptured()) {
				for (Move move : piece.generateAllPossibleMoves(board)) {
					list.add(move);
				}
			}
		}
		return list;
	}
}
