package game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 */
public abstract class  Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();
	protected int size = 5;
	private int id;
	private transient Board board;
	private int amountToGrow = 0;
	protected transient static Random rand =  new Random();
	protected transient boolean wasInterrupted = false;
	private Lock lock = new ReentrantLock();

	public void setInterruptedTrue(){
		this.wasInterrupted = true;
	}

	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	public boolean hasToGrow(){
		return --amountToGrow > 0;
	}

	/*	protected void decreaseHasToGrow(){
        hasToGrow--;
    }*/
	public void capture(Goal goal) throws InterruptedException {
		this.amountToGrow = goal.captureGoal();
	}


	public int getSize() {
		return size;
	}

	public int getLength() {
		return cells.size();
	}

	public int getIdentification() {
		return id;
	}

	public LinkedList<Cell> getCells() {
		return cells;
	}

	public void move(Cell bp) throws InterruptedException  {
			bp.request(this);
			if( this.isInterrupted() || getBoard().isGameOverV2() )
				return;
			cells.add(0, bp);
			if (!hasToGrow()) {
				Cell temp = cells.removeLast();
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
				if(!firstPos.isOcupied()) {
					cells.add(firstPos);
					try {
						firstPos.request(this);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			//System.err.println("Snake "+getIdentification()+" starting at:"+getCells().getLast());
		}
		public Board getBoard() {
			return board;
		}
}
