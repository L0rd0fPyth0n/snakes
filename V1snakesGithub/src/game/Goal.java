package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static gui.SnakeGui.NUM_ROWS;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
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
		lockGoal.lock();  //TODO ver lock se é preciso
		incrementValue();
		if(value >= MAX_VALUE){
			board.setGameOver();
			board.interruptAllSnakes();
			board.pool.close();
			board.interruptAllObs();
		}
		//posiçao atual do GOAL antes de comer
		BoardPosition goalPosition = board.getGoalPosition();
		//CELULA ATUAL DO GOAL
		Cell GoalCell = board.getCell(goalPosition);
		GameElement ge = GoalCell.getGameElement();

		GoalCell.removeGoal();

		BoardPosition newGoalPos = board.getRandomPosition();
		Cell nova = board.getCell(newGoalPos);

		board.setGoalPosition(newGoalPos);
		nova.setGameElement(ge);

		board.setChanged();
		lockGoal.unlock();
		return value;
	}
}
