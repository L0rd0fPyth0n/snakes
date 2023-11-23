package game;

import environment.Board;
import environment.Cell;

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
		try {
			while(obstacle.getRemainingMoves() > 0) {
				Thread.sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
				while(true){
					Cell nextCell = board.getCell(board.getRandomPosition());
					//TODO comentei isto porque tinhamos o erro das 50 SNAKES.
					//TODO dar fix nos obstaculos pq podem ir pra cima de outros i think
					if(!nextCell.setGameElementObstacle(this.obstacle))
						continue;
					board.getCell(this.obstacle.getPos()).removeObstacle();
					this.obstacle.setPos(nextCell.getPosition());
					board.setChanged();
					obstacle.decrementRemainingMoves();
					break;
				}
			}
		} catch (InterruptedException ignore) {}
	}
}