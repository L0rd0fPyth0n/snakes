package game;

import java.util.LinkedList;
import java.util.List;

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
		Direction dir = Direction.getRandomDirection();


		int newX = this.getCells().get(0).getPosition().x + dir.getX();
		int newY = this.getCells().get(0).getPosition().y + dir.getY();

		if(newX < 0 || newY <0 || newX >= Board.NUM_COLUMNS || newY >= Board.NUM_ROWS){
			//TODO

		}

		Cell newCell = getBoard().getCell(new BoardPosition(newX,newY));

		try {
			newCell.request(this);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	private int generateX(){
		return this.getCells().get(0).getPosition()
	}

	@Override
	public void run() {
		doInitialPositioning();
		System.err.println(this.getCells().get(0).toString());
		System.err.println("initial size:"+cells.size());



		try {
			cells.getLast().request(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: automatic movement
		this.move();
	}
	

	
}
