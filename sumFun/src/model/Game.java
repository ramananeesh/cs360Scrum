package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import javax.swing.Timer;

public class Game extends Observable{
	//private Tile [][]tiles;
	private Integer [][]tiles; 		//integer type should be enough since we are using JButtons in our view.
	private Queue<Integer> tilesQueue; // @TODO check implementation
	private int score;
	private String gameType;
	private GameTimer timer;
	private int remainingMoves;
	private String remainingTime;
	//private boolean noTime;
	private int points; //points per move
	private boolean gameOver;
	private int empty;
	private boolean won;
	private final int moveLimit=50;
	private final int queueSize=5;
	private int removedElement;
	private boolean witchCraft;
	boolean witchCraftOnce=false;
	private int hints=3;
	private boolean bonusMove;
	private static Game game;	//for the singleton
	/**
	 * Default constructor
	 * Initializes all the data members
	 */
	private Game(String gameType){
		/* random object for assigning random values in the tiles */
		Random rand=new Random();
		//this.tiles=new Tile[9][9];
		this.tiles=new Integer[9][9];
		//initialize every tile
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				this.tiles[i][j]=new Integer(-1);
			}
		}
		//set values for the tiles now; ignore borders
		for(int i=1;i<8;i++){
			for(int j=1;j<8;j++){
				//tiles[i][j].setValue(rand.nextInt());
				tiles[i][j]=rand.nextInt(10);
			}
		}
		//initialize the queue
		//this.tilesQueue=new ArrayList<Integer>();   // Initialized Twice???????????
		//initialize the score (initial)
		this.score=0;
		//initialize the gameType
		this.gameType=gameType;
		//initialize queue
		this.tilesQueue=new Queue<Integer>();
		populateQueue();
		if(gameType.equals("untimed")) {
			this.remainingMoves=moveLimit;
		}

		if(gameType.equals("timed")){
			timer=new GameTimer();
			this.remainingTime=timer.getTimeLeft();
			//this.noTime=true;
		}
		this.gameOver=false;
		this.empty=32;//present number in the borders
		this.witchCraft=false;
	}
	public boolean getWitchCraft() {
		return this.witchCraft;
	}
	public String getRemainingTime() {
		return remainingTime;
	}
	
	public int getTimeLeft() {
		return timer.getTimeLimit();
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
		setChanged();
		notifyObservers();
	}
	
	public GameTimer getTimer() {
		return timer;
	}
	public void setTimer(GameTimer timer) {
		this.timer = timer;
	}
	public int getRemainingMoves() {
		return remainingMoves;
	}

	public void setRemainingMoves(int remainingMoves) {
		this.remainingMoves = remainingMoves;
	}
	
	public void stopTimer() {
		this.timer.stopTimer();;
	}
	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public int getEmpty() {
		return empty;
	}

	public void setEmpty(int empty) {
		this.empty = empty;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public int getQueueSize() {
		return queueSize;
	}
	public void pauseTime(){
		this.timer.stopTimer();
	}
	public void resumeTime(){
		this.timer.startTimer();
	}
	
	public boolean isSuccessfulPlacement(Coordinates coord,int element){
		return isSuccessfulPlacement(coord.getRow(), coord.getCol(), element);
	}
	
	public boolean isSuccessfulPlacement(int row, int col, int element) {
		boolean success=false;
		int sum=0;
		//get coordinates of all useful neighbors
		//useful neighbors are those which are not empty.
		ArrayList<Coordinates>usefulNeighbors=getUsefulNeighbors(row, col);
		//loop through all useful neighbors and calculate the sum of their values
		for(int i=0;i<usefulNeighbors.size();i++){
			int r=usefulNeighbors.get(i).getRow();
			int c=usefulNeighbors.get(i).getCol();
			sum+=tiles[r][c];
		}
		//get top element of queue
		int elementPlaced=element;
		//compare value according to given rule
		if(elementPlaced==-1){
			throw new NullPointerException("tilesQueue getQueueElement in successfulplacement null ptr in FOLLOWING IF. Not Working!\n");
		} else if((sum%10)==elementPlaced){
			success=true;
		}
		//method now returns if tiles surrounding the current tile can be removed or not
		return success;
	}

	public ArrayList<Coordinates>getUsefulNeighbors(Coordinates coord) {
		return getUsefulNeighbors(coord.getRow(), coord.getCol());
	}

	public ArrayList<Coordinates>getUsefulNeighbors(int row, int col){
		/**
			//all possible combinations for a tile placed in a grid having coordinates {row,col}
			//Logic as follows
			/*
		 * Assume that a tile is placed at location row,col in a matrix
		 * The matrix looks like:
		 * 							row-1,col-1	|	row-1,col	|	row-1,col+1
		 * 							row,col-1	|	row,col		|	row,col+1
		 * 							row+1,col	|	row+1,col	|	row+1,col+1
		 *
		 * The neighbors will be all the surrounding tiles.
		 * Now, if the tile is placed at the corners, the values of [i,j] where i belongs to all
		 * values of rows and j belongs to all values of columns have to be within the limits
		 * of 0<=row<=8 since it is a 9*9 board. So running a simple if-else conditional structure
		 * to check these limits for all the above neighbor coordinates can simplify the procedure
		 * WE CAN AVOID THE ABOVE COMMENTED CODE, WHICH IS LENGTHY AND REPETITIVE
		 */
		int [][]allNeighborsCoords={{row-1,col-1},{row-1,col},{row-1,col+1},
				{row,col-1},{row,col+1},
				{row+1,col-1},{row+1,col},{row+1,col+1}};
		ArrayList<Coordinates>allNeighbors=new ArrayList<Coordinates>();//all neighbors for the placed tile
		ArrayList<Coordinates>usefulNeighbors=new ArrayList<Coordinates>();//all neighbors that are not empty
		//find all the neighbors
		for(int i=0;i<allNeighborsCoords.length;i++){
			int r=allNeighborsCoords[i][0];
			int c=allNeighborsCoords[i][1];
			if((r>=0&&r<=8)&&(c>=0&&c<=8)){
				allNeighbors.add(new Coordinates(r,c));
			}
		}
		//find all non-empty neighbors
		for(int i=0;i<allNeighbors.size();i++){
			if(tiles[allNeighbors.get(i).getRow()][allNeighbors.get(i).getCol()]!=-1){
				usefulNeighbors.add(allNeighbors.get(i));
			}
		}
		/*
		 *return only non empty neighbors. This will be useful since in the future, all non-empty neighbors
		 *will be removed if it is a successful placement. Need not check later if they are empty
		 */
		return usefulNeighbors;
	}

	public void updateTilesinBoard(Coordinates coord){
		//first get row and col from the given coordinates
		int row=coord.getRow();
		int col=coord.getCol();
		//check if the given tile is empty. If not, return false
		//indicating that tile cannot be placed
		if(tiles[row][col]!=-1)	{
			//return false;
			return;
		} else {
			removedElement=tilesQueue.dequeue();
			//tile at given position is updated
			//tiles[row][col]=tilesQueue.dequeue();;
			tiles[row][col]=removedElement;
			if(gameType.equals("untimed")){
				//update moves
				remainingMoves--;
				commonProcedure(coord, gameType);
				setChanged();
				notifyObservers();
			} else{
				if(timer.getTimeLimit()==0){
					gameOver=true;
					setChanged();
					notifyObservers();
				}else{
					commonProcedure(coord, gameType);
					setChanged();
					notifyObservers();
				}
			}
			
		}
		
	}
	public boolean commonProcedure(Coordinates coord, String gameType){
		if(gameType.equals("untimed")){
			if(remainingMoves==0){
				//update gameOver status
				gameOver=true;
				return true;
			}
		} else if(gameType.equals("timed")){
			if(timer.getTimeLimit()==0){
				gameOver=true;
				return true;
			}
		}
		
		int row=coord.getRow();
		int col=coord.getCol();
		//Score calculation
		this.score+=scorePlacement(coord);
		//score calculation ends

		//now come to the other tiles/neighbors
		//boolean placement=isSuccessfulPlacement(coord, tiles[row][col]);
		boolean placement=isSuccessfulPlacement(coord, removedElement);
		if(placement==false){//if it is not a successful placement
			empty--;//decrease the number of empty tiles in the board
		} else{//if the placement is successful
			ArrayList<Coordinates> usefulNeighbors=getUsefulNeighbors(coord);//get all the useful neighbors that were removed
			//change the placed tile to empty since it was a successful placement
			tiles[row][col]=-1;
			//now loop through all useful neighbors and change their values to empty
			for(int i=0;i<usefulNeighbors.size();i++){
				tiles[usefulNeighbors.get(i).getRow()][usefulNeighbors.get(i).getCol()]=-1;
			}
			//the number of empty tiles now will be equal to the size of usefulNeighbors.
			//so update the number of empty tiles in the board
			empty+=usefulNeighbors.size();
		}

		//tilesQueue update starts
		if(tilesQueue.getSize() < queueSize) {
			populateQueue();
		}
		//tilesQueue update ends

		//check game status again
		if(empty==0){//if the number of empty tiles in the board is zero, game is over!
			gameOver=true;
			//return true since tile was updated and some operation took place
			return true;
		} else if(empty==81){//since there are 81 tiles in the board. If all are empty, game won!
			//game is won; update that in the model
			won=true;
			//return true for updated tiles
			return true;
		}
		
		//if nothing works
		return false;
	}
	public void setWitchCraft(boolean bool) {
		this.witchCraft=bool;
	}
	//
	public void populateQueue(){

		Random rand = new Random();
		while(this.tilesQueue.getSize() < this.getQueueSize()){
			this.tilesQueue.enqueue(rand.nextInt(10));
		}
	}

	//if that returns true the method scores that placement accordingly
	public int scorePlacement(Coordinates coord) {
		int score=0;
		bonusMove=false;
		if(isSuccessfulPlacement(coord,removedElement)){
			ArrayList<Coordinates> neighbors = this.getUsefulNeighbors(coord);

			//add placed tile value to score
			/**
			 * Should not do this as Dr. Sedlmeyer docked points in Sprint 2
			 */
			//score+=tiles[coord.getRow()][coord.getCol()].intValue();
			int sum=0;
			//add all neighbors values to score
			for(int i= 0; i < neighbors.size(); i++) {
				sum+=tiles[neighbors.get(i).getRow()][neighbors.get(i).getCol()];
			}
			score+=sum;
			//add bonus points if applicable
			if(neighbors.size() >= 3) {
				score+=10*neighbors.size();
				bonusMove=true;
			}
		}
		//set Points to score for this move
		this.points=score;
		
		//return score
		return score;
	}
	
	public boolean isBonusMove() {
		return bonusMove;
	}
	public void setBonusMove(boolean bonusMove) {
		this.bonusMove = bonusMove;
	}
	public Integer[][] getTiles() {
		return tiles;
	}

	public void setTiles(Integer[][] tiles) {
		this.tiles = tiles;
		setChanged();
		notifyObservers();
	}
	public int getHints() {
		return this.hints;
	}
	public Queue<Integer> getTilesQueue() {
		return tilesQueue;
	}

	public void setTilesQueue(Queue<Integer> tilesQueue) {
		this.tilesQueue = tilesQueue;
		setChanged();
		notifyObservers();
	}
	
	public void refreshQueue() {
		Queue<Integer> newTilesQueue = new Queue<Integer>();
		Random rand = new Random();
		for(int i = 0; i < this.queueSize; i++) {
			newTilesQueue.enqueue(rand.nextInt(10));
		}
		setTilesQueue(newTilesQueue);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		setChanged();
		notifyObservers();
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
		setChanged();
		notifyObservers();
	}

	public int getMoveLimit() {
		return moveLimit;
	}
	public int getTileValue(Coordinates coord){
		return tiles[coord.getRow()][coord.getCol()];
	}
	public void setTileValue(int value, Coordinates coord){
		this.tiles[coord.getRow()][coord.getCol()]=value;
		setChanged();
		notifyObservers();
	}
	public Coordinates getBestPlacement() {
		int placedValue=tilesQueue.getElement(0);
		Coordinates ret = new Coordinates(0,0);
		int max = getUsefulNeighbors(0, 0).size();
		for(int i=0; i<tiles.length; i++) {
			for(int j=0; j<tiles[i].length; j++) {
				int possibleMax = getUsefulNeighbors(i, j).size();
				if(tiles[i][j] == -1 && possibleMax > max && isSuccessfulPlacement(i, j, placedValue)) {
							max = possibleMax;
							ret = new Coordinates(i,j);
				}
			}
		}
		this.hints--;
		return ret;
	}
	public void witchCraft(Coordinates coord) {
		int tileValue=tiles[coord.getRow()][coord.getCol()];
		//checking for other tiles in the board with this value
		for(int i=0; i<tiles.length; i++) {
			for(int j=0; j<tiles[i].length; j++) {
				if(tiles[i][j] == tileValue) {
					//set tiles value to -1 if same value
					tiles[i][j]=-1;
					empty+=1;
				}
			}
		}
		if(empty==0) {
			gameOver=true;
		} else if(empty==81) {
			won=true;
		}
		setChanged();
		notifyObservers();
	}
	
	public static Game getGame(String type){
		if(game==null) {
			game=new Game(type);
		}
		return game;
	}
	
	public static void clear(){
		game=null;
	}
	
	public class GameTimer {
		private Timer gametimer;
		private int timeLimit;
		private String timeLeft;

		public GameTimer() {
			this.timeLimit = 180;
			//1000 ms delay, actionlistener for the timer
			this.gametimer = new Timer(1000, new TimerListener());
			startTimer();
		}

		//should be called by the timer every second
		//checks if the time is up, if not decrements
		public void updateTime() {
			if(timeLimit<=180) {
				if(timeLimit > 0) {
					timeLimit--;
					timeLeft = getMinutes() + " : " + getSeconds();
				} else {
					stopTimer();
					gameOver=true;
				}
				remainingTime=getTimeLeft();
				setChanged();
				notifyObservers();
				//return timeUp;
			}
		}

		//returns minutes lefts
		public String getMinutes() {
			if(timeLimit<=180 && timeLimit>=0) {
				return Integer.toString(timeLimit/60);
			} else {
				return "Invalid Time";
			}
		}

		//returns seconds left
		public String getSeconds() {
			if(timeLimit<0||timeLimit>180) {
				return "Invalid Time";
			}
			int seconds = timeLimit%60;
			if(seconds<10) {
				return "0" + Integer.toString(seconds);
			}
			return Integer.toString(seconds);
		}

		//getter for time left
		public String getTimeLeft() {
			if(timeLimit==0) {
				return "0 : 00";
			}
			return timeLeft;
		}
		public Timer getGameTimer() {
			return gametimer;
		}
		void stopTimer() {
			gametimer.stop();
		}

		void startTimer() {
			gametimer.start();
		}
		public int getTimeLimit(){
			//System.out.println(timeLimit);
			return timeLimit;
		}
		public void setTimeLimit(int limit) {
			timeLimit=limit;
		}
		private class TimerListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				//remainingTime=getTimeLeft();
				updateTime();
				//setChanged();
				//notifyObservers();
			}

		}
	}
}
