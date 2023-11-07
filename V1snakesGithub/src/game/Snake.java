package game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 */
public abstract class Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private Board board;
	private int amountToGrow = 0;
	protected static Random rand =  new Random(1030234356);

	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	protected boolean hasToGrow(){
		//TODO bad
		return --amountToGrow > 0;
	}

	/*	protected void decreaseHasToGrow(){
		hasToGrow--;
	}*/
	public void startGrowing(int amountToGrow){
		//TODO isto devia ser += mas só com = é q funciona
		this.amountToGrow = amountToGrow;
	}


	public int getSize() {
		return size;
	}
	//TODO diferença entre o de cima e o de baixo
	public int getLength() {
		return cells.size();
	}
	public int getIdentification() {
		return id;
	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

//	protected void move(Cell cell) throws InterruptedException {
//		cell.request(this);
//	}
	protected void move(Cell bp)  {
		bp.request(this);
		cells.add(0,bp);//TODO passar para class Cell

		if(!hasToGrow()) {
			Cell temp = cells.removeLast(); //TODO passar para class Cell
			temp.release();
		}

		getBoard().setChanged();
	}

	protected abstract Cell getNextCell();

	public LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<>();
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
			Cell firstPos = board.getCell(new BoardPosition(posX, posY));
			if(!firstPos.isOcupiedBySnake()) {
				cells.add(firstPos);
				firstPos.request(this);
				break;
			}
		}
		System.err.println("Snake "+getIdentification()+" starting at:"+getCells().getLast());
	}
	public Board getBoard() {
		return board;
	}
	
	
}
