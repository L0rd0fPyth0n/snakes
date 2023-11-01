package game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {





	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}
	public void move(){
		while(true){
			Direction dir = Direction.getRandomDirection();

			int newX = this.getCells().get(0).getPosition().x + dir.getX();
			int newY = this.getCells().get(0).getPosition().y + dir.getY();

			if (!(newX < 0 || newY < 0 || newX >= Board.NUM_COLUMNS || newY >= Board.NUM_ROWS)) {
				Cell newCell = getBoard().getCell(new BoardPosition(newX, newY));

				try {
					this.move(newCell);


				} catch (InterruptedException e) {
				}
				getBoard().setChanged();
				break;
			}

			try {
				this.sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}



	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());

		//TODO: automatic movement

		while(true){
			//TODO remover while true
			try {
				this.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
			}
			this.move();
		}

	}
	

	
}
