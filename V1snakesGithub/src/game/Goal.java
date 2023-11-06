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



	public Goal( Board board2) {
		this.board = board2;
	}

	public int getValue() {
		return value;
	}

	private void incrementValue() throws InterruptedException {

		if(++value >= MAX_VALUE )
			for(Snake s : board.getSnakes()){
				s.interrupt();
			}
	}

	public  int captureGoal() throws InterruptedException {
		lockGoal.lock();
		incrementValue();

		//posiçao atual do GOAL antes de comer
		BoardPosition c = board.getGoalPosition();
		//CELULA ATUAL DO GOAL
		Cell cellACTUAL = board.getCell(c);
		//GOLO
		Goal g = cellACTUAL.getGoal();


		//GameElement do Golo que esta representado no GUI
		GameElement ge = cellACTUAL.getGameElement();

		BoardPosition newGoalPos = board.getRandomPosition();
		Cell nova = board.getCell(newGoalPos);
		board.setGoalPosition(newGoalPos);
		nova.setGameElement(ge);

		//remover a anterior
		cellACTUAL.setGameElement(null);
		//cellACTUAL.removeGoal();
		board.setChanged();
		lockGoal.unlock();
		return value;
	}


}
