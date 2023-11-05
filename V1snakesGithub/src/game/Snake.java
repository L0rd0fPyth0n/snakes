package game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 *
 */
public abstract class Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private Board board;

	private int amountToGrow;

	protected void grow(Cell c){

	}
	protected boolean hasToGrow(){
		return --amountToGrow > 0;
	}
	protected void startGrowing(int amountToGrow){
		this.amountToGrow = amountToGrow;
	}
	private Random rand =  new Random();
	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getLength() {
		return cells.size();
	}
	
	public LinkedList<Cell> getCells() {
		return cells;
	}

//	protected void move(Cell cell) throws InterruptedException {
//		cell.request(this);
//	}
protected abstract void move(Cell cell) throws InterruptedException;


	public LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<BoardPosition>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}

		return coordinates;
	}	
	protected void doInitialPositioning() {
		// Random position on the first column. 
		// At startup, snake occupies a single cell
		int posX = 0;
		while(true){
			int posY = rand.nextInt(Board.NUM_ROWS);
			BoardPosition at = new BoardPosition(posX, posY);
				if(!board.getCell(at).isOcupiedBySnake()) {
					try {
						this.move(board.getCell(at));
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					cells.add(board.getCell(at));
					break;
				}
		}
		System.err.println("Snake "+getIdentification()+" starting at:"+getCells().getLast());
	}
	public Board getBoard() {
		return board;
	}
	
	
}
