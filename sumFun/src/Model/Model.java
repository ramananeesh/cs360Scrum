package Model;

import java.util.Random;

public class Model {
	//private Tile [][]tiles;
	private Integer [][]tiles; 		//integer type should be enough since we are using JButtons in our view.
	private Queue tilesQueue;
	private int score;
	private String gameType;
	private final int moveLimit=50;
	
	/**
	 * Default constructor
	 * Initializes all the data members 
	 */
	public Model(String gameType){
		//random object for assigning random values in the tiles
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
				tiles[i][j]=rand.nextInt(9);
			}
		}
		//initialize the queue
		this.tilesQueue=new Queue(5);
		//initialize the score (initial)
		this.score=0;
		//initialize the gameType
		this.gameType=gameType;
	}

	public Integer[][] getTiles() {
		return tiles;
	}

	public void setTiles(Integer[][] tiles) {
		this.tiles = tiles;
	}

	public Queue getTilesQueue() {
		return tilesQueue;
	}

	public void setTilesQueue(Queue tilesQueue) {
		this.tilesQueue = tilesQueue;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getMoveLimit() {
		return moveLimit;
	}
}