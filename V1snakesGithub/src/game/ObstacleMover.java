package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;
import game.Obstacle;

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

		while(true){
			try {
				sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
			} catch (InterruptedException e) {
			}

			Cell cellOndeEsta =	board.getCell(obstacle.getPos());
			cellOndeEsta.removeObstacle();
			board.setChanged();
			while(obstacle.getRemainingMoves() > 0) {
				BoardPosition nextPos = board.getRandomPosition();
				Cell nextCell = board.getCell(nextPos);
				if (!(nextCell.isOcupied() || nextCell.isOcupiedByGoal())){
					nextCell.setGameElement(obstacle);
					obstacle.setPos(nextPos);
					board.setChanged();
					obstacle.decrementRemainingMoves();
					break;
				}
			}
		}
	}
}
