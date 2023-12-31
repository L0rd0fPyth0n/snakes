package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static gui.SnakeGui.NUM_ROWS;

public class Goal extends GameElement implements Serializable {
	private int value=1;
	private transient Board board;

	public  static final int MAX_VALUE=100; //default: 10
	private Lock lockGoal = new ReentrantLock();


	public boolean isGameOver(){
		return value >= MAX_VALUE;
	}

	public Goal( Board board2) {
		this.board = board2;
	}

	public int getValue() {
		return value;
	}

	private void incrementValue(){
		value++;
	}


	public int captureGoal() {
		lockGoal.lock();
		incrementValue();
		if(value >= MAX_VALUE){
			board.setGameOver();
			board.interruptAllSnakes();
			board.pool.close();//.shutdownNow
			board.interruptAllObs();
		}
		//posiçao atual do GOAL antes de comer
		BoardPosition goalPosition = board.getGoalPosition();
		//CELULA ATUAL DO GOAL
		Cell GoalCell = board.getCell(goalPosition);
		GameElement ge = GoalCell.getGameElement();

		GoalCell.removeGoal();

		while(true) {
			BoardPosition newGoalPos = board.getRandomPosition();
			Cell nova = board.getCell(newGoalPos);

			if(nova.setGameElementGoal(ge)) {
				board.setGoalPosition(newGoalPos);
				break;
				//nova.setGameElement(ge);
			}

		}
		board.setChanged();
		lockGoal.unlock();
		return value;
	}
}
