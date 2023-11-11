package environment;

import java.io.Serializable;
import java.util.*;

import ConcurrencyUtils.FixedTPool;
import game.*;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
public abstract class Board extends Observable {
	protected Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 200;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_ROWS = 30;
	protected LinkedList<Snake> snakes = new LinkedList<Snake>();
	private LinkedList<Obstacle> obstacles= new LinkedList<Obstacle>();
	protected boolean isFinished;


	public FixedTPool fixedTPool = new FixedTPool(1);
	public Board() {
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}
	}

	public void interruptAllSnakes() {
		for(Snake s : this.getSnakes()){
			s.interrupt();
		}
	}
	public boolean isOutOfBound(BoardPosition cell){
		return cell.x < 0 ||
				cell.x>=Board.NUM_COLUMNS ||
				cell.y <0 ||
				cell.y >= Board.NUM_ROWS;
	}
	public Cell getCell(BoardPosition cellCoord)   {
		if(isOutOfBound(cellCoord)) {
			throw  new IllegalArgumentException();
		}
		return cells[cellCoord.x][cellCoord.y];
	}

	public BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_ROWS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		if(isOutOfBound(goalPosition)) {
			throw  new IllegalArgumentException();
		}
		this.goalPosition = goalPosition;
	}
	
	public void addGameElement(GameElement gameElement) {
		boolean placed=false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(!getCell(pos).isOcupied() && !getCell(pos).isOcupiedByGoal()) {
				getCell(pos).setGameElement(gameElement);
				if(gameElement instanceof Goal) {
					setGoalPosition(pos);
//					System.out.println("Goal placed at:"+pos);
				}
				if(gameElement instanceof Obstacle obs) {
					obs.setPos(pos);
				}
				placed=true;
			}
		}
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;

	}


	protected Goal addGoal() {
		Goal goal=new Goal(this);
		addGameElement( goal);
		//setGoalPosition(new BoardPosition(4,4));
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		// clear obstacle list , necessary when resetting obstacles.
		getObstacles().clear();
		for (int i = 0; i < numberObstacles; i++) {
			Obstacle obs=new Obstacle(this);
			ObstacleMover lb = new ObstacleMover(obs,this);
			//lb.start();

			fixedTPool.submitTask(lb);

			//TODO adicionar a thread pool

			addGameElement(obs);
			getObstacles().add(obs);
		}
	}
	public LinkedList<Snake> getSnakes() {
		return snakes;
	}
	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	
	public abstract void init(); 
	
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}
}