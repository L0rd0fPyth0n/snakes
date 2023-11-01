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

		this.getCells();
	}


	@Override
	public void run() {
		doInitialPositioning();
		System.err.println(this.getCells());
		System.err.println("initial size:"+cells.size());



		try {
			cells.getLast().request(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: automatic movement
	}
	

	
}
