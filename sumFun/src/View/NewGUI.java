package View;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Model.Game;
import Model.Queue;
import View.GUI.MouseActionTiles;
import View.GUI.TilesClickListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class NewGUI extends JFrame implements Observer{
	private JPanel contentPane;
	private Game theGame;
	private Tile[][] tiles;
	private JLabel [] queueTiles;
	private JLabel lblDesc;
	private JLabel lblLeft;
	private JLabel lblScoreDesc;
	private JLabel lblScore;
	private JLabel lblGameStatus;
	private JLabel lblPoints;
	private JLabel lblPointsDesc;
	private JMenu fileMenu;
	private JMenuItem saveGameOpt;
	private JMenuItem loadGameOpt;
	private JMenu helpMenu;
	private JMenuItem refreshOpt;
	
	public NewGUI(Game game) {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 640, 480);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(fileMenu);
		
		saveGameOpt = new JMenuItem("Save Game");
		saveGameOpt.setHorizontalAlignment(SwingConstants.CENTER);
		saveGameOpt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		saveGameOpt.addActionListener(new SaveClickListener());
		fileMenu.add(saveGameOpt);
		
		loadGameOpt = new JMenuItem("Load Game");
		loadGameOpt.setHorizontalAlignment(SwingConstants.CENTER);
		loadGameOpt.setFont(new Font("Tahoma", Font.PLAIN, 14));
		fileMenu.add(loadGameOpt);
		
		helpMenu = new JMenu("Help");
		helpMenu.setFont(new Font("Tahoma", Font.BOLD, 14));
		menuBar.add(helpMenu);
		
		refreshOpt = new JMenuItem("Refresh Queue");
		refreshOpt.setFont(new Font("Tahoma", Font.BOLD, 14));
		refreshOpt.addActionListener(new refreshQueueClickListener()); //TODO added actionlistener to refresh option under help menu
		helpMenu.add(refreshOpt);
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.theGame = game;
		this.theGame.addObserver(this);
		
		JPanel northPanel = new JPanel();
		northPanel.setBackground(Color.BLACK);
		getContentPane().add(northPanel, BorderLayout.NORTH);
		northPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(Color.BLACK);
		northPanel.add(headerPanel, BorderLayout.CENTER);
		headerPanel.setLayout(new GridLayout(0, 2, -1, 0));
		
		lblDesc = new JLabel("Moves Left : ");
		lblDesc.setOpaque(true);
		lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesc.setForeground(Color.WHITE);
		lblDesc.setFont(new Font("Chiller", Font.BOLD | Font.ITALIC, 16));
		lblDesc.setBackground(Color.BLACK);
		headerPanel.add(lblDesc);
		
		lblLeft = new JLabel("");
		lblLeft.setBackground(Color.BLACK);
		lblLeft.setForeground(Color.RED);
		lblLeft.setOpaque(true);
		lblLeft.setFont(new Font("Chiller", Font.BOLD | Font.ITALIC, 16));
		headerPanel.add(lblLeft);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setBackground(Color.BLACK);
		getContentPane().add(eastPanel, BorderLayout.EAST);
		eastPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBackground(Color.BLACK);
		eastPanel.add(optionPanel);
		optionPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel scoresPanel = new JPanel();
		optionPanel.add(scoresPanel);
		scoresPanel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel pointsPanel = new JPanel();
		scoresPanel.add(pointsPanel);
		pointsPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblPointsDesc = new JLabel("Points:");
		lblPointsDesc.setOpaque(true);
		lblPointsDesc.setBackground(Color.BLACK);
		lblPointsDesc.setForeground(Color.BLUE);
		lblPointsDesc.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		pointsPanel.add(lblPointsDesc);
		
		lblPoints = new JLabel("0");
		lblPoints.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoints.setOpaque(true);
		lblPoints.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		lblPoints.setForeground(Color.YELLOW);
		lblPoints.setBackground(Color.BLACK);
		pointsPanel.add(lblPoints);
		
		JPanel totalScorePanel = new JPanel();
		scoresPanel.add(totalScorePanel);
		totalScorePanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblScoreDesc = new JLabel("Score:");
		lblScoreDesc.setOpaque(true);
		lblScoreDesc.setHorizontalAlignment(SwingConstants.CENTER);
		lblScoreDesc.setForeground(Color.BLUE);
		lblScoreDesc.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 17));
		lblScoreDesc.setBackground(Color.BLACK);
		totalScorePanel.add(lblScoreDesc);
		
		lblScore = new JLabel("0");
		lblScore.setOpaque(true);
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setForeground(Color.YELLOW);
		lblScore.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 17));
		lblScore.setBackground(Color.BLACK);
		totalScorePanel.add(lblScore);
		
		JPanel panel = new JPanel();
		optionPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblGameStatus = new JLabel("Game in Progress");
		lblGameStatus.setOpaque(true);
		lblGameStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameStatus.setForeground(new Color(255, 140, 0));
		lblGameStatus.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		lblGameStatus.setBackground(Color.BLACK);
		panel.add(lblGameStatus);
		
		JPanel queueTilesPanel = new JPanel();
		queueTilesPanel.setForeground(Color.WHITE);
		queueTilesPanel.setBackground(Color.BLACK);
		eastPanel.add(queueTilesPanel);
		queueTilesPanel.setLayout(new GridLayout(5, 1, 0, 0));
		Queue<Integer> modelQueueTiles = theGame.getTilesQueue();
		this.queueTiles = new JLabel[5];
		for(int i=0;i<5;i++) {
			queueTiles[i]=new JLabel(modelQueueTiles.getElement(i).toString());
			queueTiles[i].setHorizontalAlignment(SwingConstants.CENTER);
			queueTiles[i].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
			queueTiles[i].setForeground(Color.CYAN);
			queueTiles[i].setBackground(Color.black);
			queueTilesPanel.add(queueTiles[i]);
		}
		
		JPanel boardPanel = new JPanel();
		//boardPanel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(boardPanel, BorderLayout.CENTER);
		boardPanel.setLayout(new GridLayout(9, 9));
		Integer [][]modelTiles= theGame.getTiles();
		tiles = new Tile[9][9];
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				tiles[i][j]=new Tile(i,j);
				tiles[i][j].setOpaque(true);
				tiles[i][j].setForeground(Color.white);
				tiles[i][j].setFocusable(false);
				tiles[i][j].addActionListener(new TilesClickListener());
				tiles[i][j].addMouseListener(new MouseActionTiles());
			}
		}
		for(int i=1;i<8;i++){
			for(int j=1;j<8;j++){
				tiles[i][j].setText(modelTiles[i][j].toString());
				tiles[i][j].setEnabled(false);
			}
		}
		//add all buttons to the boardPanel
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				boardPanel.add(tiles[i][j]);
			}
		}
		
		setVisible(true);
	}
public void update(Observable arg0, Object arg1) {
		
		// TODO Auto-generated method stub
		boolean gameOver= theGame.isGameOver();
		boolean gameWon= theGame.isWon();
		String gameType=theGame.getGameType();
		if(gameType.equals("timed"));
		//update the remaining time
		lblLeft.setText(theGame.getRemainingTime());
		
		//first update the tiles from the model
		Integer [][]modelTiles= theGame.getTiles();
		for(int i=0;i<tiles.length;i++){
			for(int j=0;j<tiles[i].length;j++){
				String text=modelTiles[i][j].toString();
				if(text.equals("-1")){
					tiles[i][j].setText("");
					tiles[i][j].setEnabled(true);
				}
				else{
					tiles[i][j].setText(modelTiles[i][j].toString());
					tiles[i][j].setEnabled(false);
				}
			}
		}
		//update queue of tiles from the model
		Queue<Integer> modelQueueTiles = theGame.getTilesQueue();
		if(gameWon||gameOver){
			for(int i=0;i<queueTiles.length-1;i++){
				queueTiles[i].setText(modelQueueTiles.getElement(i).toString());
			}
			queueTiles[4].setText("");
		}
		else{
			//Queue<Integer> modelQueueTiles = theGame.getTilesQueue();
			for(int i=0;i<queueTiles.length;i++){
			queueTiles[i].setText(modelQueueTiles.getElement(i).toString());
			}
		}
		//update the score board
		int modelScore= theGame.getScore();
		lblScore.setText(Integer.toString(modelScore));
		if(gameType.equals("untimed")){
			//update the moves left
			int modelMovesLeft= theGame.getRemainingMoves();
			lblLeft.setText(Integer.toString(modelMovesLeft));
		}
		
		if(gameOver){
			for(int i=0;i<tiles.length;i++){
				for(int j=0;j<tiles[i].length;j++){
					tiles[i][j].setEnabled(false);
				}
			}
			lblGameStatus.setText("Game Over! Loser!");
			lblGameStatus.setForeground(Color.RED);
			return;
		}
		else if(gameWon){
			for(int i=0;i<tiles.length;i++){
				for(int j=0;j<tiles[i].length;j++){
					tiles[i][j].setEnabled(false);
				}
			}
			lblGameStatus.setText("Game Won! Legend!");
			lblGameStatus.setForeground(Color.GREEN);
			return;
		}
	}
//TODO implemented actionlistener for refreshQueue
	public class refreshQueueClickListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			theGame.refreshQueue();
			
		}
		
	}

	public class SaveClickListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String fileName=JOptionPane.showInputDialog("Enter the File name without any extension");
			theGame.save(fileName);
		}
		
	}
	
	public class TilesClickListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Tile t=(Tile)e.getSource();
			theGame.updateTilesinBoard(t.getCoord());
			//System.out.println("Tile clicked");
		}
		
	}
	public class MouseActionTiles implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
			Tile t=(Tile)arg0.getSource();
			
			//if tile is an empty tile, turn background of tile green on hover
			if(t.isEnabled()) {
				t.setBackground(Color.GREEN);
			}
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			Tile t=(Tile)arg0.getSource();
			
			//upon leaving a hover turns green background of tile back to the default
			if(t.getBackground().equals(Color.GREEN)) {
				t.setBackground(null);
			}
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}


	/*public static void main(String[]args){
		new NewGUI(Game.getGame("untimed"));
	}*/
}
