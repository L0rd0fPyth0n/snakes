package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.util.Random;

import static gui.SnakeGui.NUM_ROWS;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;


	public Goal( Board board2) {
		this.board = board2;
	}

	public int getValue() {
		return value;
	}

	public void incrementValue() throws InterruptedException {

		if(value < MAX_VALUE ) {
			value++;
		} else {
			//isFinished = true na automatic snake ??
		}
	}

	//TODO fazer com que o 10 nao apareça? Talvez its ok pq o prof tmb tinha assim
	public int captureGoal() throws InterruptedException {
		incrementValue();

		//posiçao atual do GOAL antes de comer
		BoardPosition c = board.getGoalPosition();
		//CELULA ATUAL DO GOAL
		Cell cellACTUAL = board.getCell(c);
		//GOLO
		Goal g = cellACTUAL.getGoal();


		//GameElement do Golo que esta representado no GUI
		GameElement ge = cellACTUAL.getGameElement();

		BoardPosition newGoalPos = getRandomPosition();
		Cell nova = board.getCell(newGoalPos);
		board.setGoalPosition(newGoalPos);
		nova.setGameElement(ge);

		//remover a anterior
		cellACTUAL.setGameElement(null);
		//cellACTUAL.removeGoal();
		board.setChanged();

		return value;
	}

	public BoardPosition getRandomPosition() {
		Random r = new Random();
		return new BoardPosition((int) (r.nextInt(NUM_ROWS)),
				(int) (r.nextInt(NUM_ROWS)));
	}

}
