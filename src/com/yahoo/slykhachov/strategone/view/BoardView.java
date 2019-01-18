package com.yahoo.slykhachov.strategone.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;

import com.yahoo.slykhachov.strategone.Move;
import com.yahoo.slykhachov.strategone.StrategoneGame;
import com.yahoo.slykhachov.strategone.model.*;
import com.yahoo.slykhachov.strategone.model.material.*;

import static java.util.stream.Collectors.*;

public class BoardView extends JPanel {
	private static final long serialVersionUID = 1L;
	private StrategoneGame strategoneGame;
	private BoardModel boardModel;
	private Map<String, PieceView> views;
	private Timer splashEndTimer;
	private PieceView actedUponPieceView = null;
	private PieceView splashPieceView = null;
	public BoardView(StrategoneGame strategoneGame, BoardModel boardModel) {
		this.splashEndTimer = new Timer(
			1000,
			ae -> this.repaint()
		);
		this.splashEndTimer.setRepeats(false);
		this.setPreferredSize(new Dimension(600, 600));
		this.views = new HashMap<>();
		this.setStrategoneGame(strategoneGame);
		this.setBoardModel(boardModel);
		List<IPieceModel> models = getNonCapturedPieceModels(strategoneGame);
		BoardView.initViews(
			this.views,
			models,
			getPreferredSize()
		);	
		BoardViewMouseListener listener = new BoardViewMouseListener();
		this.addMouseMotionListener(listener);
		this.addMouseListener(listener);
		this.setBorder(
			new CompoundBorder(
				new BevelBorder(BevelBorder.RAISED),
				new EtchedBorder()
			)
		);
	}
	private static List<IPieceModel> getNonCapturedPieceModels(StrategoneGame strategoneGame) {
		return Stream.of(new IAdversary[] {strategoneGame.getRedPlayer(), strategoneGame.getBluePlayer()})
			.flatMap(adversary -> Stream.of(adversary.getPieces()))
			.filter(mdl -> mdl.isCaptured() == false)
			.collect(toList());
	}
	private static void initViews(Map<String, PieceView> pieceViews,
			List<IPieceModel> models, Dimension dimension) {
		BufferedImage bi;
		boolean isRed;
		String key;   
		for (IPieceModel pModel : models) {
			isRed = pModel.getAdversary().equals(Red.class);
			key = String.valueOf(pModel.getRow()) + String.valueOf(pModel.getCol());
			PieceView pieceView;
			if (pModel.getClass().equals(Scout.class)) {
				if (isRed) {
					bi = StrategoneGameView.red_scout_image;
				} else {
					bi = StrategoneGameView.blue_scout_image;
				}
			} else {  
				if (pModel.getClass().equals(Miner.class)) {
					if (isRed) {
						bi = StrategoneGameView.red_miner_image;
					} else {
						bi = StrategoneGameView.blue_miner_image;
					}
				} else {
					if (pModel.getClass().equals(Sergeant.class)) {
						if (isRed) {
							bi = StrategoneGameView.red_sergeant_image;
						} else {
							bi = StrategoneGameView.blue_sergeant_image;
						}
					} else {
						if (pModel.getClass().equals(Lieutenant.class)) {
							if (isRed) {
								bi = StrategoneGameView.red_lieutenant_image;
							} else {
								bi = StrategoneGameView.blue_lieutenant_image;
							}
						} else {
							if (pModel.getClass().equals(Captain.class)) {
								if (isRed) {
									bi = StrategoneGameView.red_captain_image;
								} else {
									bi = StrategoneGameView.blue_captain_image;
								}
							} else {
								if (pModel.getClass().equals(Major.class)) {
									if (isRed) {
										bi = StrategoneGameView.red_major_image;
									} else {
										bi = StrategoneGameView.blue_major_image;
									}
								} else {
									if (pModel.getClass().equals(Colonel.class)) {
										if (isRed) {
											bi = StrategoneGameView.red_colonel_image;
										} else {
											bi = StrategoneGameView.blue_colonel_image;
										}
									} else {
										if (pModel.getClass().equals(General.class)) {
											if (isRed) {
												bi = StrategoneGameView.red_general_image;
											} else {
												bi = StrategoneGameView.blue_general_image;
											}
										} else {
											if (pModel.getClass().equals(Marshall.class)) {
												if (isRed) {
													bi = StrategoneGameView.red_marshall_image;
												} else {
													bi = StrategoneGameView.blue_marshall_image;
												}
											} else {
												if (pModel.getClass().equals(Spy.class)) {
													if (isRed) {
														bi = StrategoneGameView.red_spy_image;
													} else {
														bi = StrategoneGameView.blue_spy_image;
													}
												} else {
													if (pModel.getClass().equals(Bomb.class)) {
														if (isRed) {
															bi = StrategoneGameView.red_bomb_image;
														} else {
															bi = StrategoneGameView.blue_bomb_image;
														}
													} else {
														if (pModel.getClass().equals(Flag.class)) {
															if (isRed) {
																bi = StrategoneGameView.red_flag_image;
															} else {
																bi = StrategoneGameView.blue_flag_image;
															}
														} else {
															throw new UnsupportedOperationException();
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
			}
			pieceView = new PieceView(
				convertToPoint2D(
					pModel.getRow(),
					pModel.getCol(), 
					dimension
				),
				(isRed ? bi : StrategoneGameView.blue_tails_image),
				bi
			);
			pieceViews.put(
				key,
				pieceView
			);
		}
	}
	private static Point2D.Double convertToPoint2D(int row ,int col, Dimension dimension) {
		Point2D.Double point = new Point2D.Double(
				col * (dimension.getWidth() / 10),
				row * (dimension.getHeight() / 10)
		);
		return point;
	}
	public Map<String, PieceView> getViews() {
		return this.views;
	}
	public void setSplashPieceView(PieceView pv) {
		this.splashPieceView = pv;
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(new Color(26, 148, 49));
		g2d.fillRect(
			0,
			0,
			(int) getPreferredSize().getWidth(),
			(int) getPreferredSize().getHeight()
		);
		g2d.setColor(Color.BLUE);
		g2d.fillRect(
			(int) (getPreferredSize().getWidth() / 10) * 2,
			(int) (getPreferredSize().getHeight() / 10) * 4,
			(int) (getPreferredSize().getWidth() / 10) * 2,
			(int) (getPreferredSize().getHeight() / 10) * 2
		);
		g2d.fillRect(
			(int) (getPreferredSize().getWidth() / 10) * 6,
			(int) (getPreferredSize().getHeight() / 10) * 4,
			(int) (getPreferredSize().getWidth() / 10) * 2,
			(int) (getPreferredSize().getHeight() / 10) * 2
		);
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < getWidth(); i += getWidth() / 10) {
			g2d.drawLine(i, 0, i, getHeight()); 
		}
		for (int i = 0; i < getHeight(); i += getHeight() / 10) {
			g2d.drawLine(0, i, getWidth(), i); 
		}
		double heightRatio;
		double widthRatio;
		AffineTransform at = new AffineTransform();
		for (Entry<String, PieceView> e : views.entrySet()) {
			PieceView pv = e.getValue();
			heightRatio = (this.getPreferredSize().getHeight() / 12) / pv.getPieceImage().getHeight();
			widthRatio = (this.getPreferredSize().getWidth() / 12) / pv.getPieceImage().getWidth();
			at.translate(
				pv.getPoint().getX() + (pv.getPieceImage().getWidth() / 18),
				pv.getPoint().getY() + (pv.getPieceImage().getHeight() / 18)
			);
			at.scale(widthRatio, heightRatio);
			g2d.drawRenderedImage(pv.getPieceImage(), at);
			at.setToIdentity();
		}
		if (this.actedUponPieceView != null) {
			at.translate(
				actedUponPieceView.getPoint().getX(),
				actedUponPieceView.getPoint().getY()
			);
			heightRatio = (this.getPreferredSize().getHeight() / 12) / actedUponPieceView.getPieceImage().getHeight();
			widthRatio = (this.getPreferredSize().getWidth() / 12) / actedUponPieceView.getPieceImage().getWidth();
			at.scale(widthRatio, heightRatio);
			g2d.drawRenderedImage(actedUponPieceView.getPieceImage(), at);
		}
		if (this.splashPieceView != null) {
			at.translate(
				splashPieceView.getPoint().getX() + (splashPieceView.getReverseImge().getWidth() / 18),
				splashPieceView.getPoint().getY() + (splashPieceView.getReverseImge().getHeight() / 18)
			);
			heightRatio = (this.getPreferredSize().getHeight() / 12) / splashPieceView.getReverseImge().getHeight();
			widthRatio = (this.getPreferredSize().getWidth() / 12) / splashPieceView.getReverseImge().getWidth();
			at.scale(widthRatio, heightRatio);
			g2d.drawRenderedImage(splashPieceView.getReverseImge(), at);
			this.splashPieceView = null;
		}
		g2d.dispose();
	}
	public void updateBoardView() {
		getViews().clear();
		initViews(
			getViews(),
			getNonCapturedPieceModels(strategoneGame),
			getPreferredSize()
		);
		strategoneGame.getStrategoneGameView()
			.setBluePlayerActivePiecesLabel("Blue Player Pieces Left: " + strategoneGame.getBluePlayer().getNumberOfUncapturedPieces());
		strategoneGame.getStrategoneGameView()
			.setRedPlayerActivePiecesLabel("Red Player Pieces Left: " + strategoneGame.getRedPlayer().getNumberOfUncapturedPieces());
		this.repaint();	
	}
	public void setStrategoneGame(StrategoneGame strategoneGame) {
		this.strategoneGame = strategoneGame;
	}
	public StrategoneGame getStrategoneGame() {
		return this.strategoneGame;
	}
	public void setBoardModel(BoardModel boardModel) {
		this.boardModel = boardModel;
	}
	public BoardModel getBoardModel() {
		return this.boardModel;
	}
	public void chargeSplashEndTimer() {
		this.splashEndTimer.start();
	}
	private class BoardViewMouseListener implements MouseMotionListener, MouseListener {	
		private IPieceModel pieceModel;
		@Override
		public void mousePressed(MouseEvent me)	{
			if (!getStrategoneGame().isGameOver()) {
				if (me.getButton() == MouseEvent.BUTTON1) {
					double x = me.getX();
					double y = me.getY();
					int row = (int) ((y / getPreferredSize().getWidth()) * 10);
					int col = (int) ((x / getPreferredSize().getHeight()) * 10);
					this.pieceModel = getIfCorrectTurnAdversary(getStrategoneGame().getAdversaryToMove(), row, col);
					if (pieceModel != null) {
						actedUponPieceView = views.remove(String.valueOf(row) + String.valueOf(col));
						actedUponPieceView.getPoint()
							.setLocation(
								x - getPreferredSize().getWidth() / 20,
								y - getPreferredSize().getHeight() / 20
						);
						repaint();
					}
				}
			}
		}
		private IPieceModel getIfCorrectTurnAdversary(IAdversary adv,
				int row, int col) {
			for (IPieceModel pm : adv.getPieces()) {
				if (getStrategoneGame().getComputerAdversary() != adv) {
					if (!pm.isCaptured()) {
						if (pm.getRow() == row) {
							if (pm.getCol() == col) {
								return pm;
							}
						}
					}
				}
			}
			return null;
		}
		@Override
		public void mouseReleased(MouseEvent me) {
			if (actedUponPieceView != null) {
				IAdversary adversaryToMove = getStrategoneGame().getAdversaryToMove();
				double x = me.getX();
				double y = me.getY();
				int row = (int) ((y / getPreferredSize().getHeight()) * 10);
				int col = (int) ((x / getPreferredSize().getWidth()) * 10);
				List<Move> listOfMoves = adversaryToMove.generateAllPossibleMoves(getBoardModel());
				Move candidateMove = new Move(
					this.pieceModel,
					this.pieceModel.getRow(),
					this.pieceModel.getCol(),
					row,
					col
				);
				if (listOfMoves.contains(candidateMove)) {
					if (getBoardModel().getBoard()[row][col] != null) {
						splashPieceView = getViews()
							.get(String.valueOf(row) + String.valueOf(col));
						chargeSplashEndTimer();
					}
					getBoardModel().performMove(candidateMove);
					getStrategoneGame().setAdversaryToMove(
						adversaryToMove.getOpponent()
					);
					actedUponPieceView = null;	
					updateBoardView();
					getStrategoneGame().doResponce();
				} else {			
					actedUponPieceView.getPoint()
						.setLocation(
							pieceModel.getCol() * (getPreferredSize().getWidth() / 10),
							pieceModel.getRow() * (getPreferredSize().getHeight() / 10)
					);
					views.put(
						String.valueOf(pieceModel.getRow()) + String.valueOf(pieceModel.getCol()),
						actedUponPieceView
					);
					repaint();
					actedUponPieceView = null;
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent me)	{
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (actedUponPieceView != null) {
					int x = me.getX();
					int y = me.getY();
					actedUponPieceView.getPoint()
						.setLocation(
						    x - getPreferredSize().getWidth() / 20,
						    y - getPreferredSize().getHeight() / 20
					);
					repaint();
				}
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override	
		public void mouseEntered(MouseEvent e)	{}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
}
