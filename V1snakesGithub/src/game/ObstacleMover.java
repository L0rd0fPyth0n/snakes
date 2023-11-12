package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;
import game.Obstacle;

import java.util.concurrent.ThreadPoolExecutor;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private Board board;
	public ObstacleMover(Obstacle obstacle, Board board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		while(obstacle.getRemainingMoves() > 0) {
			try {
				sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
				while(true){
					BoardPosition nextPos = board.getRandomPosition();
					Cell nextCell = board.getCell(nextPos);
					if (nextCell.isCompletelyUnoccupied()){
						//TODO
						obstacle.move(nextCell);
						break;
					}
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
