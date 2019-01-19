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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		JPanel panel3 = new JPanel();
		panel3.add(gameView);
		panel2.add(panel3, BorderLayout.CENTER);
		JPanel aiMoveListContainer = new JPanel();
		aiMoveListContainer.setPreferredSize(new Dimension(200, (int) gameView.getPreferredSize().getHeight()));
		JList<String> aiMoveList = new JList<>();
		aiMoveList.setBackground(Color.LIGHT_GRAY);
		DefaultListModel<String> aiListModel = new DefaultListModel<>();
		aiMoveList.setModel(aiListModel);
		aiMoveListContainer.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(aiMoveList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		aiMoveListContainer.add(scrollPane, BorderLayout.CENTER);
		aiMoveListContainer.add(new JScrollPane(aiMoveList), BorderLayout.CENTER);
		this.getStrategoneGame().getBoard().getBoardView().setListModel(aiListModel);
		this.getStrategoneGame().setListModel(aiListModel);
		panel2.add(aiMoveListContainer, BorderLayout.EAST);
		panel2.add(
			createToolBar(
				cardLayout,
				this,
				sgv -> sgv.getStrategoneGame().getComputerAdversary() !=
					sgv.getStrategoneGame().getAdversaryToMove(),
				(brdMdl, listModel) -> {
					if (Blue.class.equals(brdMdl.getLastMoveAdversary())) {
						brdMdl.undoMove();
						brdMdl.undoMove();
						listModel.removeElementAt(listModel.getSize() - 1);
						listModel.removeElementAt(listModel.getSize() - 1);
						getBoardView().updateBoardView();
					} else {
						if (Red.class.equals(brdMdl.getLastMoveAdversary())) {
							brdMdl.undoMove();
							listModel.removeElementAt(listModel.getSize() - 1);
							getBoardView().updateBoardView();
						}
					}
				},
				jtb -> {
					jtb.add(bluePlayerActivePieces);
				},
				jtb -> {
					jtb.add(redPlayerActivePieces);
				},
				aiListModel
			),
			BorderLayout.NORTH
		);
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
		JPanel panel6 = new JPanel();
		panel6.add(singlePlayerGameView);
		panel5.add(panel6, BorderLayout.CENTER);
		JPanel trainingMoveListContainer = new JPanel();
		trainingMoveListContainer.setPreferredSize(new Dimension(200, (int) gameView.getPreferredSize().getHeight()));
		JList<String> trainingMoveList = new JList<>();
		trainingMoveList.setBackground(Color.LIGHT_GRAY);
		DefaultListModel<String> trainingListModel = new DefaultListModel<>();
		trainingMoveList.setModel(trainingListModel);
		trainingMoveListContainer.setLayout(new BorderLayout());
		JScrollPane scrollPane2 = new JScrollPane(trainingMoveList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		trainingMoveListContainer.add(scrollPane2, BorderLayout.CENTER);
		this.getStrategoneGame().getBoard().getSingleUserBoardView().setListModel(trainingListModel);
		panel5.add(trainingMoveListContainer, BorderLayout.EAST);
		panel5.add(
			createToolBar(
				cardLayout,
				this,
				sgv -> true,
				(brdMdl, listModel) -> {
					if (brdMdl.isSafeToUndoOneMove()) {
						brdMdl.undoMove();
						listModel.removeElementAt(listModel.getSize() - 1);
						getSingleUserBoardView().updateBoardView();
						getStrategoneGame().setAdversaryToMove(
							getStrategoneGame().getAdversaryToMove()
								.getOpponent()
						);	
					}
				},
				jtb -> {},
				jtb -> {},
				trainingListModel
			),
			BorderLayout.NORTH
		);
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
	private static JToolBar createToolBar(CardLayout cardLayout,
			StrategoneGameView sgv,
			Predicate<StrategoneGameView> predicate,
			BiConsumer<BoardModel, DefaultListModel<String>> consumer1,
			Consumer<JToolBar> consumer2,
			Consumer<JToolBar> consumer3,
			DefaultListModel<String> listModel) {
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
					consumer1.accept(bm, listModel);
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
			ae -> restart(cardLayout, sgv, listModel)
		);
		consumer2.accept(jtb);
		jtb.add(helpButton);
		jtb.add(restartButton);
		jtb.add(undoButton);
		consumer3.accept(jtb);
		return jtb;
	}
	public static void restart(CardLayout cardLayout,
			StrategoneGameView sgv, DefaultListModel<String> listModel) {
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
		if (listModel != null) {
			listModel.removeAllElements();
		}
		cardLayout.show(sgv, "1");
	}
	public StrategoneGame getStrategoneGame() {
		return this.strategoneGame;
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
