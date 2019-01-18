package com.yahoo.slykhachov.strategone.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import com.yahoo.slykhachov.strategone.StrategoneGame;
import com.yahoo.slykhachov.strategone.model.*;
import com.yahoo.slykhachov.strategone.model.material.*;

public class StrategoneGameView extends JPanel {
	private static final long serialVersionUID = 1L;
	public static BufferedImage blue_tails_image;
	public static BufferedImage blue_flag_image;  
	public static BufferedImage red_flag_image;  
	public static BufferedImage blue_bomb_image;  
	public static BufferedImage red_bomb_image;  
	public static BufferedImage blue_marshall_image;
	public static BufferedImage red_marshall_image;
	public static BufferedImage blue_general_image;
	public static BufferedImage red_general_image;
	public static BufferedImage blue_colonel_image; 
	public static BufferedImage red_colonel_image; 
	public static BufferedImage blue_major_image;  
	public static BufferedImage red_major_image;
	public static BufferedImage blue_captain_image; 
	public static BufferedImage red_captain_image; 
	public static BufferedImage blue_lieutenant_image;  
	public static BufferedImage red_lieutenant_image;
	public static BufferedImage blue_sergeant_image; 
	public static BufferedImage red_sergeant_image; 
	public static BufferedImage blue_miner_image;  
	public static BufferedImage red_miner_image;
	public static BufferedImage blue_scout_image;  
	public static BufferedImage red_scout_image;
	public static BufferedImage blue_spy_image;  
	public static BufferedImage red_spy_image;
	static {
		try {
			blue_tails_image = ImageIO.read(new File(".//images//tails_blue.jpg"));
			blue_flag_image = ImageIO.read(new File(".//images//flag_blue.jpg"));  
			red_flag_image = ImageIO.read(new File(".//images//flag_red.jpg"));  
			blue_bomb_image = ImageIO.read(new File(".//images//bomb_blue.jpg"));  
			red_bomb_image = ImageIO.read(new File(".//images//bomb_red.jpg"));  
			blue_marshall_image = ImageIO.read(new File(".//images//1_blue.jpg"));
			red_marshall_image = ImageIO.read(new File(".//images//1_red.jpg"));
			blue_general_image = ImageIO.read(new File(".//images//2_blue.jpg"));
			red_general_image = ImageIO.read(new File(".//images//2_red.jpg"));
			blue_colonel_image = ImageIO.read(new File(".//images//3_blue.jpg")); 
			red_colonel_image = ImageIO.read(new File(".//images//3_red.jpg")); 
			blue_major_image = ImageIO.read(new File(".//images//4_blue.jpg"));  
			red_major_image = ImageIO.read(new File(".//images//4_red.jpg"));
			blue_captain_image = ImageIO.read(new File(".//images//5_blue.jpg")); 
			red_captain_image = ImageIO.read(new File(".//images//5_red.jpg")); 
			blue_lieutenant_image = ImageIO.read(new File(".//images//6_blue.jpg"));  
			red_lieutenant_image = ImageIO.read(new File(".//images//6_red.jpg"));
			blue_sergeant_image = ImageIO.read(new File(".//images//7_blue.jpg")); 
			red_sergeant_image = ImageIO.read(new File(".//images//7_red.jpg")); 
			blue_miner_image = ImageIO.read(new File(".//images//8_blue.jpg"));  
			red_miner_image = ImageIO.read(new File(".//images//8_red.jpg"));
			blue_scout_image = ImageIO.read(new File(".//images//9_blue.jpg"));  
			red_scout_image = ImageIO.read(new File(".//images//9_red.jpg"));
			blue_spy_image = ImageIO.read(new File(".//images//s_blue.jpg"));  
			red_spy_image = ImageIO.read(new File(".//images//s_red.jpg"));
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			System.exit(-1);
		}
	}
	private StrategoneGame strategoneGame;
	private JLabel redPlayerActivePieces = new JLabel();
	private JLabel bluePlayerActivePieces = new JLabel();
	private GameSetupView setupView;
	
	public StrategoneGameView(StrategoneGame strategoneGame) {
		this.strategoneGame = strategoneGame;
		CardLayout cardLayout = new CardLayout();
		this.setLayout(cardLayout);
		BoarderedBoardView gameView = new BoarderedBoardView(
			strategoneGame.getBoard().getBoardView()
		);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel2.add(
			createToolBar(
				cardLayout,
				this,
				sgv -> sgv.getStrategoneGame().getComputerAdversary() !=
					sgv.getStrategoneGame().getAdversaryToMove(),
				brdMdl -> {
					if (Blue.class.equals(brdMdl.getLastMoveAdversary())) {
						brdMdl.undoMove();
						brdMdl.undoMove();
						getBoardView().updateBoardView();
					} else {
						if (Red.class.equals(brdMdl.getLastMoveAdversary())) {
							brdMdl.undoMove();
							getBoardView().updateBoardView();
						}
					}
				},
				jtb -> {
					jtb.add(bluePlayerActivePieces);
				},
				jtb -> {
					jtb.add(redPlayerActivePieces);
				}
			),
			BorderLayout.NORTH
		);
		JPanel panel3 = new JPanel();
		panel3.add(gameView);
		panel2.add(panel3, BorderLayout.CENTER);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(200, (int) gameView.getPreferredSize().getHeight()));
		panel2.add(p, BorderLayout.EAST);
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		this.setupView = new GameSetupView(
			this,
			cardLayout,
			gameView.getPreferredSize()
		);
		JPanel panel4 = new JPanel();
		panel4.setBackground(Color.ORANGE);
		panel4.add(setupView);
		BoarderedBoardView singlePlayerGameView = new BoarderedBoardView(
			getStrategoneGame().getBoard()
				.getSingleUserBoardView()
		);
		JPanel panel5 = new JPanel();
		panel5.setBackground(Color.ORANGE);
		panel5.setLayout(new BorderLayout());
		panel5.add(
			createToolBar(
				cardLayout,
				this,
				sgv -> true,
				brdMdl -> {
					if (brdMdl.isSafeToUndoOneMove()) {
						brdMdl.undoMove();
						getSingleUserBoardView().updateBoardView();
						getStrategoneGame().setAdversaryToMove(
							getStrategoneGame().getAdversaryToMove()
								.getOpponent()
						);	
					}
				},
				jtb -> {},
				jtb -> {}
			),
			BorderLayout.NORTH
		);
		JPanel panel6 = new JPanel();
		panel6.add(singlePlayerGameView);
		panel5.add(panel6, BorderLayout.CENTER);
		/////////////////////////////////////////
		JPanel panel10 = new JPanel();
		panel10.setPreferredSize(new Dimension(200, (int) gameView.getPreferredSize().getHeight()));
		panel5.add(panel10, BorderLayout.EAST);
		/////////////////////////////////////////
		this.add(panel4, "1");
		this.add(panel2, "2");
		this.add(panel5, "3");
		cardLayout.show(this, "1");
	}
	public BoardView getBoardView() {
		return this.strategoneGame.getBoard().getBoardView();
	}
	public SingleUserBoardView getSingleUserBoardView() {
		return this.strategoneGame.getBoard().getSingleUserBoardView();
	}
	public void setRedPlayerActivePiecesLabel(String text) {
		redPlayerActivePieces.setText(text);
	}
	public void setBluePlayerActivePiecesLabel(String text) {
		bluePlayerActivePieces.setText(text);
	}
	private static JToolBar createToolBar(CardLayout cardLayout, StrategoneGameView sgv,
			Predicate<StrategoneGameView> predicate, Consumer<BoardModel> consumer1,
			Consumer<JToolBar> consumer2, Consumer<JToolBar> consumer3) {
		JToolBar jtb = new JToolBar();
		jtb.setFloatable(false);
		jtb.setLayout(new FlowLayout());
		jtb.setBackground(Color.GRAY);
		JButton undoButton = new JButton("Undo move");
		JButton helpButton = new JButton("Help");
		JButton restartButton = new JButton("Restart");
		undoButton.addActionListener(
			ae -> {
				if (predicate.test(sgv)) {
					BoardModel bm = sgv.getStrategoneGame()
						.getStrategoneGameModel()
						.getBoardModel();
					consumer1.accept(bm);
				}
		});
		helpButton.addActionListener(
			ae -> {
				JFrame ruleFrame = new JFrame();
				ruleFrame.setTitle("Game Rules");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				ImageIcon rulesImage = new ImageIcon("./images/stratego_rules_image4.jpg");
				JLabel picture = new JLabel(rulesImage);
				ruleFrame.getContentPane().add(picture);
				ruleFrame.setSize(rulesImage.getIconWidth() + 35, rulesImage.getIconHeight() + 35);
				int x = (screenSize.width - ruleFrame.getSize().width) / 2;
				int y = (screenSize.height - ruleFrame.getSize().height) / 2;
				ruleFrame.setLocation(x, y);
				ruleFrame.setVisible(true);
		});
		restartButton.addActionListener(
			ae -> restart(cardLayout, sgv)
		);
		consumer2.accept(jtb);
		jtb.add(helpButton);
		jtb.add(restartButton);
		jtb.add(undoButton);
		consumer3.accept(jtb);
		return jtb;
	}
	public static void restart(CardLayout cardLayout, StrategoneGameView sgv) {
		sgv.setupView.setUpPieceModels(new IPieceModel[10][4]);
		sgv.setupView.initViewPanels();
		sgv.getStrategoneGame()
			.getBoard()
			.getBoardModel()
			.clearBoard();
		sgv.getStrategoneGame()
			.getBoard()
			.getBoardModel()
			.clearStack();
		cardLayout.show(sgv, "1");
	}
	public StrategoneGame getStrategoneGame() {
		return this.strategoneGame;
	}
	private static class GameSetupView extends JPanel
			implements MouseMotionListener, MouseListener {
		private static final long serialVersionUID = 1L;
		private PiecePanelView flagViewPanel;
		private PiecePanelView bombViewPanel;
		private PiecePanelView marshallViewPanel; 
		private PiecePanelView generalViewPanel;
		private PiecePanelView colonelViewPanel;
		private PiecePanelView majorViewPanel;
		private PiecePanelView captainViewPanel;
		private PiecePanelView lieutenantViewPanel;
		private PiecePanelView sergeantViewPanel; 
		private PiecePanelView minerViewPanel;
		private PiecePanelView scoutViewPanel;
		private PiecePanelView spyViewPanel;
		private PiecePanelView[] pieceViewPanels;
		private PieceView actedUponPieceView;
		private Class<? extends IPieceModel> actedUponPieceModelInfo;
		private PiecePanelView actedUponPiecePanelView;
		private IPieceModel[][] pieceModels;
		private IPieceModel actedUponPieceModel;
		GameSetupView(StrategoneGameView gameView, CardLayout layout,
				Dimension dim) {
			this.actedUponPieceModel = null;
			this.actedUponPiecePanelView = null;
			this.actedUponPieceModelInfo = null;	
			this.actedUponPieceView = null;
			this.pieceModels = new IPieceModel[10][4];
			this.setPreferredSize(dim);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			JButton trainingButton = new JButton("Training mode");
			JButton startButton = new JButton("Start");
			this.add(trainingButton);
			this.add(startButton);
			this.initViewPanels();
			trainingButton.addActionListener(
				ae -> {
					gameView.getStrategoneGame().getRedPlayer()
							.setPieces(
								hardCodedRedPieceModelsFactory()	
					);
					gameView.getStrategoneGame().getBluePlayer()
							.setPieces(
								hardCodedBluePieceModelsFactory()
						);
					gameView.getStrategoneGame()
							.getBoard()
							.getBoardModel()
							.setAdversaries(
								gameView.getStrategoneGame().getBluePlayer(),
								gameView.getStrategoneGame().getRedPlayer()
					);	
					gameView.getStrategoneGame()
							.getBoard()
							.getSingleUserBoardView()
							.updateBoardView();
					gameView.getStrategoneGame()
							.setAdversaryToMove(
								gameView.getStrategoneGame()
										.getRedPlayer()
					);
					layout.show(gameView, "3");
			});
			startButton.addActionListener(
				ae -> {
					IPieceModel[] humanPlayerPieces = Stream.of(pieceModels)
						.flatMap(array -> Stream.of(array))
						.filter(e -> e != null)
						.toArray(IPieceModel[]::new);
					long pieceCount = Stream.of(humanPlayerPieces)
						.filter(e -> e != null)
						.count();
					if (pieceCount == 40) {	
						gameView.getStrategoneGame().getRedPlayer()
							.setPieces(humanPlayerPieces);
						gameView.getStrategoneGame().getBluePlayer()
							.setPieces(
								hardCodedBluePieceModelsFactory()
						);
						gameView.getStrategoneGame()
							.getBoard()
							.getBoardModel()
							.setAdversaries(
								gameView.strategoneGame.getBluePlayer(),
								gameView.strategoneGame.getRedPlayer()
						);
						gameView.getStrategoneGame()
							.getBoard()
							.getBoardView()
							.updateBoardView();
						gameView.getStrategoneGame()
							.setAdversaryToMove(
								gameView.getStrategoneGame()
									.getRedPlayer()
						);
						layout.show(gameView, "2");
					}
			});
		}
		private static IPieceModel[] hardCodedRedPieceModelsFactory() {
			return new IPieceModel[] {
				new Flag(9, 1, Red.class),
				new Bomb(6, 1, Red.class),
				new Bomb(6, 9, Red.class),
				new Bomb(7, 0, Red.class),
				new Bomb(8, 1, Red.class),
				new Bomb(9, 0, Red.class),
				new Bomb(9, 2, Red.class),
				new Marshall(6, 6, Red.class),
				new General(6, 3, Red.class),
				new Colonel(7, 8, Red.class),
				new Colonel(8, 6, Red.class),
				new Major(7, 4, Red.class),
				new Major(8, 2, Red.class),
				new Major(8, 5, Red.class),
				new Captain(7, 5, Red.class),
				new Captain(7, 7, Red.class),
				new Captain(9, 3, Red.class),
				new Captain(6, 2, Red.class),
				new Lieutenant(8, 4, Red.class),
				new Lieutenant(8, 9, Red.class),
				new Lieutenant(9, 5, Red.class),
				new Lieutenant(9, 6, Red.class),
				new Sergeant(6, 0, Red.class),
				new Sergeant(7, 1, Red.class),
				new Sergeant(8, 0, Red.class),
				new Sergeant(8, 8, Red.class),
				new Miner(6, 7, Red.class),
				new Miner(7, 6, Red.class),
				new Miner(8, 3, Red.class),
				new Miner(8, 7, Red.class),
				new Miner(9, 9, Red.class),
				new Scout(6, 4, Red.class),
				new Scout(6, 5, Red.class),
				new Scout(6, 8, Red.class),
				new Scout(7, 2, Red.class),
				new Scout(7, 9, Red.class),
				new Scout(9, 4, Red.class),
				new Scout(9, 7, Red.class),
				new Scout(9, 8, Red.class),
				new Spy(7, 3, Red.class)
			};
		}
		private static IPieceModel[] hardCodedBluePieceModelsFactory() {
			return new IPieceModel[] {
				new Flag(0, 8, Blue.class),
				//new Flag(3, 4, Blue.class),
				new Bomb(3, 8, Blue.class),
				new Bomb(3, 0, Blue.class),
				new Bomb(2, 9, Blue.class),
				new Bomb(1, 8, Blue.class),
				new Bomb(0, 9, Blue.class),
				new Bomb(0, 7, Blue.class),
				new Marshall(3, 3, Blue.class),
				new General(3, 6, Blue.class),
				new Colonel(2, 1, Blue.class),
				new Colonel(1, 3, Blue.class),
				new Major(2, 5, Blue.class),
				new Major(1, 7, Blue.class),
				new Major(1, 4, Blue.class),
				new Captain(3, 7, Blue.class),
				new Captain(2, 4, Blue.class),
				new Captain(2, 2, Blue.class),
				new Captain(0, 6, Blue.class),
				new Lieutenant(1, 5, Blue.class),
				new Lieutenant(1, 0, Blue.class),
				new Lieutenant(0, 4, Blue.class),
				new Lieutenant(0, 3, Blue.class),
				new Sergeant(3, 9, Blue.class),
				new Sergeant(2, 8, Blue.class),
				new Sergeant(1, 9, Blue.class),
				new Sergeant(1, 1, Blue.class),
				new Miner(3, 2, Blue.class),
				new Miner(2, 3, Blue.class),
				new Miner(1, 6, Blue.class),
				new Miner(1, 2, Blue.class),
				new Miner(0, 0, Blue.class),
				new Scout(3, 5, Blue.class),
				new Scout(3, 4, Blue.class),
				//new Scout(0, 8, Blue.class),
				new Scout(3, 1, Blue.class),
				new Scout(2, 7, Blue.class),
				new Scout(2, 0, Blue.class),
				new Scout(0, 5, Blue.class),
				new Scout(0, 2, Blue.class),
				new Scout(0, 1, Blue.class),
				new Spy(2, 6, Blue.class)
			};
		}
		public void setUpPieceModels(IPieceModel[][] pieces) {
			this.pieceModels = pieces;
		}
		public void initViewPanels() {
			this.pieceViewPanels = new PiecePanelView[12];
			this.flagViewPanel = new PiecePanelView(red_flag_image, 1, Flag.class, 27, 170);
			this.pieceViewPanels[0] = flagViewPanel;
			this.bombViewPanel = new PiecePanelView(red_bomb_image, 6, Bomb.class, 152, 170);
			this.pieceViewPanels[1] = bombViewPanel;
			this.marshallViewPanel = new PiecePanelView(red_marshall_image, 1, Marshall.class, 277, 170);
			this.pieceViewPanels[2] = marshallViewPanel;
			this.generalViewPanel = new PiecePanelView(red_general_image, 1, General.class, 402, 170);
			this.pieceViewPanels[3] = generalViewPanel;
			this.colonelViewPanel = new PiecePanelView(red_colonel_image, 2, Colonel.class, 527, 170);
			this.pieceViewPanels[4] = colonelViewPanel;
			this.majorViewPanel = new PiecePanelView(red_major_image, 3, Major.class, 652, 170);
			this.pieceViewPanels[5] = majorViewPanel;
			this.captainViewPanel = new PiecePanelView(red_captain_image, 4, Captain.class, 27, 320);
			this.pieceViewPanels[6] = captainViewPanel;
			this.lieutenantViewPanel = new PiecePanelView(red_lieutenant_image, 4, Lieutenant.class, 152, 320);
			this.pieceViewPanels[7] = lieutenantViewPanel;
			this.sergeantViewPanel = new PiecePanelView(red_sergeant_image, 4, Sergeant.class, 277, 320);
			this.pieceViewPanels[8] = sergeantViewPanel;
			this.minerViewPanel = new PiecePanelView(red_miner_image, 5, Miner.class, 402, 320);
			this.pieceViewPanels[9] = minerViewPanel;
			this.scoutViewPanel = new PiecePanelView(red_scout_image, 8, Scout.class, 527, 320);
			this.pieceViewPanels[10] = scoutViewPanel;
			this.spyViewPanel = new PiecePanelView(red_spy_image, 1, Spy.class, 652, 320);
			this.pieceViewPanels[11] = spyViewPanel;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.ORANGE);
			g2d.fillRect(
				0,
				0,
				(int) getPreferredSize().getWidth(),
				450
			);
			g2d.setColor(new Color(26, 148, 49));
			g2d.fillRect(
				0,
				450,
				(int) getPreferredSize().getWidth(),
				(int) getPreferredSize().getHeight()
			);
			g2d.setColor(Color.BLACK);
			for (int i = 0; i < (int) getPreferredSize().getWidth(); i += ((int) getPreferredSize().getWidth()) / 10) {
				g2d.drawLine(i, 450, i, (int) getPreferredSize().getHeight());
			}
			for (int i = 450; i < (int) getPreferredSize().getHeight(); i += 300 / 4) {
				g2d.drawLine(0, i, (int) getPreferredSize().getWidth(), i); 
			}
			AffineTransform at = new AffineTransform();
			for (PiecePanelView ppv : pieceViewPanels) {
				at.translate(
					ppv.getX(),
					ppv.getY()
				);
				at.scale(0.74d, 0.74d);
				if (ppv.getCount() == 0) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(
						ppv.getX(),
						ppv.getY(),
						74,
						74
					);
				} else {
					g2d.drawRenderedImage(ppv.getImage(), at);
				}
				at.setToIdentity();
			}
			for (int i = 0; i < pieceModels.length; i++) {
				for (int j = 0; j < pieceModels[i].length; j++) {
					if (pieceModels[i][j] != null) {
						at.translate(i * 75, j * 75 + 450);
						at.scale(0.74, 0.74);
						if (pieceModels[i][j].getClass().equals(Scout.class)) {
							g2d.drawRenderedImage(red_scout_image, at);
						} else {
							if (pieceModels[i][j].getClass().equals(Bomb.class)) {
								g2d.drawRenderedImage(red_bomb_image, at);
							} else {
								if (pieceModels[i][j].getClass().equals(Miner.class)) {
									g2d.drawRenderedImage(red_miner_image, at);
								} else {
									if (pieceModels[i][j].getClass().equals(Sergeant.class)) {
										g2d.drawRenderedImage(red_sergeant_image, at);
									} else {
										if (pieceModels[i][j].getClass().equals(Lieutenant.class)) {
											g2d.drawRenderedImage(red_lieutenant_image, at);
										} else {
											if (pieceModels[i][j].getClass().equals(Captain.class)) {
												g2d.drawRenderedImage(red_captain_image, at);
											} else {
												if (pieceModels[i][j].getClass().equals(Major.class)) {
													g2d.drawRenderedImage(red_major_image, at);
												} else {
													if (pieceModels[i][j].getClass().equals(Colonel.class)) {
														g2d.drawRenderedImage(red_colonel_image, at);
													} else {
														if (pieceModels[i][j].getClass().equals(General.class)) {
															g2d.drawRenderedImage(red_general_image, at);
														} else {
															if (pieceModels[i][j].getClass().equals(Marshall.class)) {
																g2d.drawRenderedImage(red_marshall_image, at);
															} else {
																if (pieceModels[i][j].getClass().equals(Spy.class)) {
																	g2d.drawRenderedImage(red_spy_image, at);
																} else {
																	g2d.drawRenderedImage(red_flag_image, at);
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
						at.setToIdentity();
					}
				}
			}
			if (actedUponPieceView != null) {
				at.translate(
					actedUponPieceView.getPoint().getX() - 37,
					actedUponPieceView.getPoint().getY() - 37
				);
				at.scale(0.74d, 0.74d);
				g2d.drawRenderedImage(actedUponPieceView.getPieceImage(), at);
			}
			g2d.dispose(); 
		}	
		@Override
		public void mousePressed(MouseEvent me)	{
			if (me.getButton() == MouseEvent.BUTTON1) {	
				int x = me.getX();
				int y = me.getY();
				for (PiecePanelView panel : pieceViewPanels) {
					if (panel.contains(x, y)) {
						if (panel.getCount() != 0) {
							panel.setCount(panel.getCount() - 1);
							this.actedUponPiecePanelView = panel;
							this.actedUponPieceView = new PieceView(new Point2D.Double(x, y), panel.getImage(), null);
							this.actedUponPieceModelInfo = panel.getClazz();
						}
						repaint();
						return;
					}
				}
				if (y >= 450) {
					int row = ((y - 450) / 75);
					int col = (int) ((x / getPreferredSize().getWidth()) * 10);
					if (this.pieceModels[col][row] != null) {
						this.actedUponPieceModel = this.pieceModels[col][row];
						this.pieceModels[col][row] = null;
						BufferedImage bi;
						if (this.actedUponPieceModel.getClass().equals(Scout.class)) {
							bi = red_scout_image;
						} else {
							if (this.actedUponPieceModel.getClass().equals(Bomb.class)) {
								bi = red_bomb_image;
							} else {
								if (this.actedUponPieceModel.getClass().equals(Miner.class)) {
									bi = red_miner_image;
								} else {
									if (this.actedUponPieceModel.getClass().equals(Sergeant.class)) {
										bi = red_sergeant_image;
									} else {
										if (this.actedUponPieceModel.getClass().equals(Lieutenant.class)) {
											bi = red_lieutenant_image;
										} else {
											if (this.actedUponPieceModel.getClass().equals(Captain.class)) {
												bi = red_captain_image;
											} else {
												if (this.actedUponPieceModel.getClass().equals(Major.class)) {
													bi = red_major_image;
												} else {
													if (this.actedUponPieceModel.getClass().equals(Colonel.class)) {
														bi = red_colonel_image;
													} else {
														if (this.actedUponPieceModel.getClass().equals(General.class)) {
															bi = red_general_image;
														} else {
															if (this.actedUponPieceModel.getClass().equals(Marshall.class)) {
																bi = red_marshall_image;
															} else {
																if (this.actedUponPieceModel.getClass().equals(Spy.class)) {
																	bi = red_spy_image;
																} else {
																	bi = red_flag_image;
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
						this.actedUponPieceView = new PieceView(
							new Point2D.Double(x, y),
							bi,
							null
						);
						repaint();
					}
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent me) {
			if (me.getButton() == MouseEvent.BUTTON1) {
				if (actedUponPieceView != null) {
					int x = me.getX();
					int y = me.getY();
					this.actedUponPieceView.getPoint()
						.setLocation(x, y);
					this.repaint();
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent me) {
			if (actedUponPieceView != null) {
				int x = me.getX();
				int y = me.getY();
				if ((y >= 450) && (y <= 750) && (x >= 0) && (x <= 750)) {
					int row = ((y - 450) / 75);
					int col = (int) ((x / getPreferredSize().getWidth()) * 10);
					if (this.actedUponPiecePanelView != null) {
						if (this.pieceModels[col][row] == null) {
							if (actedUponPieceModelInfo.equals(Scout.class)) {
								this.pieceModels[col][row] = new Scout(row + 6, col, Red.class);
							} else {
								if (actedUponPieceModelInfo.equals(Bomb.class)) {
									this.pieceModels[col][row] = new Bomb(row + 6, col, Red.class);
								} else {
									if (actedUponPieceModelInfo.equals(Miner.class)) {
										this.pieceModels[col][row] = new Miner(row + 6, col, Red.class);
									} else {
										if (actedUponPieceModelInfo.equals(Sergeant.class)) {
											this.pieceModels[col][row] = new Sergeant(row + 6, col, Red.class);
										} else {
											if (actedUponPieceModelInfo.equals(Lieutenant.class)) {
												this.pieceModels[col][row] = new Lieutenant(row + 6, col, Red.class);
											} else {
												if (actedUponPieceModelInfo.equals(Captain.class)) {
													this.pieceModels[col][row] = new Captain(row + 6, col, Red.class);
												} else {
													if (actedUponPieceModelInfo.equals(Major.class)) {
														this.pieceModels[col][row] = new Major(row + 6, col, Red.class);
													} else {
														if (actedUponPieceModelInfo.equals(Colonel.class)) {
															this.pieceModels[col][row] = new Colonel(row + 6, col, Red.class);
														} else {
															if (actedUponPieceModelInfo.equals(General.class)) {
																this.pieceModels[col][row] = new General(row + 6, col, Red.class);
															} else {
																if (actedUponPieceModelInfo.equals(Marshall.class)) {
																	this.pieceModels[col][row] = new Marshall(row + 6, col, Red.class);
																} else {
																	if (actedUponPieceModelInfo.equals(Spy.class)) {
																		this.pieceModels[col][row] = new Spy(row + 6, col, Red.class);
																	} else {
																		this.pieceModels[col][row] = new Flag(row + 6, col, Red.class);
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
							this.actedUponPiecePanelView = null;
							this.actedUponPieceView = null;
							this.actedUponPieceModelInfo = null;
							this.repaint();
						} else {
							this.actedUponPiecePanelView.setCount(
								actedUponPiecePanelView.getCount() + 1
							);
							this.actedUponPiecePanelView = null;
							this.actedUponPieceView = null;
							this.actedUponPieceModelInfo = null;
							this.repaint();
						}
					} else {
						if (this.pieceModels[col][row] == null) {
							this.pieceModels[col][row] = this.actedUponPieceModel;
							this.actedUponPieceModel.setCol(col);
							this.actedUponPieceModel.setRow(row + 6);
							this.actedUponPieceModel = null;
							this.actedUponPieceView = null;
							this.repaint();
						} else {
							int co1 = this.actedUponPieceModel.getCol();
							int roe = this.actedUponPieceModel.getRow() - 6;
							this.pieceModels[co1][roe] = this.actedUponPieceModel;
							this.actedUponPieceModel = null;
							this.actedUponPieceView = null;
							this.repaint();
						}
					}
				} else {
					if (this.actedUponPiecePanelView != null) {
						this.actedUponPiecePanelView.setCount(
							actedUponPiecePanelView.getCount() + 1
						);
						this.actedUponPiecePanelView = null;
						this.actedUponPieceView = null;
						this.actedUponPieceModelInfo = null;
						this.repaint();
					} else {
						int col = this.actedUponPieceModel.getCol();
						int row = this.actedUponPieceModel.getRow() - 6;
						this.pieceModels[col][row] = this.actedUponPieceModel;
						this.actedUponPieceModel = null;
						this.actedUponPieceView = null;
						this.repaint();
					}
				}
			}
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e)	{}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}
		private static class PiecePanelView {
			private BufferedImage image;
			private int count;
			private Class<? extends IPieceModel> clazz;
			private int x;
			private int y;
			PiecePanelView(BufferedImage image, int count,
					Class<? extends IPieceModel> clazz, int x, int y) {
				this.image = image;
				this.count = count;
				this.clazz = clazz;
				this.x = x;
				this.y = y;
			}
			BufferedImage getImage() {
				return this.image;
			}
			int getCount() {
				return this.count;
			}
			void setCount(int count) {
				this.count = count;
			}
			boolean contains(int x, int y) {
				int sideLength = 74;
				return (this.x <= x && this.y <= y) &&
                	(this.x + sideLength >= x) &&
                	(this.y + sideLength >= y);
			}
			int getX() {
				return this.x;
			}
			int getY() {
				return this.y;
			}
			Class<? extends IPieceModel> getClazz() {
				return this.clazz;
			}
		}
	}
	private static class BoarderedBoardView extends JPanel {
		private static final long serialVersionUID = 1L;
		private Color color;
		BoarderedBoardView(JPanel boardView) {
			this.setLayout(new GridBagLayout());
			this.color = new Color(94, 51, 10);
			this.setPreferredSize(
				new Dimension(
					(int) (boardView.getPreferredSize().getWidth() + 2 * (boardView.getPreferredSize().getWidth() / 8)),
					(int) (boardView.getPreferredSize().getHeight() + 2 * (boardView.getPreferredSize().getHeight() / 8))
					//(int) (boardView.getPreferredSize().getWidth() + 2 * (boardView.getPreferredSize().getWidth() / 10)),
					//(int) (boardView.getPreferredSize().getHeight() + 2 * (boardView.getPreferredSize().getHeight() / 10))
				)
			);
			int squareStepSize = (int) (boardView.getPreferredSize().getWidth() / 10);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
    		c.gridx = 1;
    		c.gridy = 0;
			this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth(),
					(int) (boardView.getPreferredSize().getHeight() / 8),
					//(int) (boardView.getPreferredSize().getHeight() / 10),
					Color.ORANGE,
					SidePanel.CharType.LETTERS,
					xyi -> {
						xyi.setX(xyi.getX() + squareStepSize);
						xyi.setIndex(xyi.getIndex() + 1);
						return xyi;
					}
				),	
				c
			);
    		c.gridx = 0;
    		c.gridy = 1;
    		this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth() / 8,
					//(int) boardView.getPreferredSize().getWidth() / 10,
					(int) (boardView.getPreferredSize().getHeight()),
					Color.ORANGE,
					SidePanel.CharType.NUMS,
					xyi -> {
						xyi.setY(xyi.getY() + squareStepSize);
						xyi.setIndex(xyi.getIndex() - 1);
						return xyi;
					}
				),	
				c
			);
    		c.gridx = 1;
    		c.gridy = 1;
			this.add(boardView, c);
    		c.gridx = 2;
    		c.gridy = 1;
    		this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth() / 8,
					//(int) boardView.getPreferredSize().getWidth() / 10,
					(int) (boardView.getPreferredSize().getHeight()),
					Color.ORANGE,
					SidePanel.CharType.NUMS,
					xyi -> {
						xyi.setY(xyi.getY() + squareStepSize);
						xyi.setIndex(xyi.getIndex() - 1);
						return xyi;
					}
				),	
				c
			);
    		c.gridx = 1;
    		c.gridy = 2;
			this.add(
				new SidePanel(
					(int) boardView.getPreferredSize().getWidth(),
					(int) (boardView.getPreferredSize().getHeight() / 8),
					//(int) (boardView.getPreferredSize().getHeight() / 10),
					Color.ORANGE,
					SidePanel.CharType.LETTERS,
					xyi -> {
						xyi.setX(xyi.getX() + squareStepSize);
						xyi.setIndex(xyi.getIndex() + 1);
						return xyi;
					}
				),
				c
			);
		}
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(color);	
			g2d.fillRect(
				0,
				0,
				(int) getPreferredSize().getWidth(),
				(int) getPreferredSize().getHeight()
			);
			g2d.dispose();
		}
		private static class SidePanel extends JPanel {
			private static final long serialVersionUID = 1L;
			private Color color;
			private CharType charType;
			private UnaryOperator<XYIndexData> function;
			SidePanel(int width, int height, Color color, CharType charType,
					UnaryOperator<XYIndexData> function) {
				this.setPreferredSize(new Dimension(width, height));
				this.color = color;
				this.charType = charType;
				this.function = function;
			}
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(this.color);
				g2d.fillRect(
					0,
					0,
					(int) getPreferredSize().getWidth(),
					(int) getPreferredSize().getHeight()
				);
				g2d.setFont(new Font(Font.SERIF, Font.BOLD, 28).deriveFont(new AffineTransform()));
				g2d.setPaint(Color.BLACK);
				int x = 25;
				int y = 39;
				int index = this.charType == CharType.NUMS ? 9 : 0;
				Stream.iterate(new XYIndexData(x, y, index), function)
				      .limit(10)
				      .forEach(
				          xyi -> {
				              g2d.drawString(
						          this.charType.getChars()[xyi.getIndex()],
				                  xyi.getX(),
				                  xyi.getY()
				              );
				});
				g2d.dispose();
			}
			private static class XYIndexData {
				private int x;
				private int y;
				private int index;
				XYIndexData(int x, int y, int index) {
					this.x = x;
					this.y = y;
					this.index = index;
				}
				void setX(int x) {
					this.x = x;
				}
				int getX() {
					return this.x;
				}
				void setY(int y) {
					this.y = y;
				}
				int getY() {
					return this.y;
				}
				void setIndex(int index) {
					this.index = index;
				}
				int getIndex() {
					return this.index;
				}
			}
			private static enum CharType {
				NUMS(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}),
				LETTERS(new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"});
				private final String[] chars;
				CharType(String[] chars) {
					this.chars = chars;
				}
				String[] getChars() {
					return this.chars;
				}
			}
		}
	}
}
