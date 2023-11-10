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

	private void incrementValue() throws InterruptedException {
		if(++value >= MAX_VALUE )
			board.interruptAllSnakes();
	}
	public  int captureGoal() throws InterruptedException {
		lockGoal.lock();
		incrementValue();

		//posiçao atual do GOAL antes de comer
		BoardPosition goalPosition = board.getGoalPosition();
		//CELULA ATUAL DO GOAL
		Cell GoalCell = board.getCell(goalPosition);
		GoalCell.removeGoal();

		//GameElement do Golo que esta representado no GUI
		GameElement ge = GoalCell.getGameElement();

		BoardPosition newGoalPos = board.getRandomPosition();
		Cell nova = board.getCell(newGoalPos);
		//TODO is nova occupied?
		board.setGoalPosition(newGoalPos);
		nova.setGameElement(ge);

		//remover a anterior
		//cellACTUAL.setGameElement(null);

		board.setChanged();
		lockGoal.unlock();
		return value;
	}


}
