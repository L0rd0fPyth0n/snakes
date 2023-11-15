package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import remote.Direction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/** Class for a remote snake, controlled by a human

 @author luismota
 **/
public  class HumanSnake extends Snake {
	protected final ObjectInputStream inputStream;

	public HumanSnake(int id, Board board, ObjectInputStream inputStream) {
		super(id,board);
		this.inputStream = inputStream;
	}


	@Override
	protected Cell getNextCell() {
		try {
			Direction newDir =(Direction) this.inputStream.readObject();

			Cell head = cells.getFirst();
			BoardPosition headPos =head.getPosition();

			BoardPosition newPos = new BoardPosition(headPos.x + newDir.getX(), headPos.y + newDir.getY());

			return getBoard().getCell(newPos);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}