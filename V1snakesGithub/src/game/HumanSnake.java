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
	protected transient final ObjectInputStream inputStream;

	public HumanSnake(int id, Board board, ObjectInputStream inputStream) {
		super(id,board);
		this.inputStream = inputStream;
	}


	@Override
	protected Cell getNextCell(){
		try {
			Direction newDir =(Direction) this.inputStream.readObject();

			BoardPosition headPos =cells.getFirst().getPosition();

			BoardPosition newPos = new BoardPosition(headPos.x + newDir.getX(), headPos.y + newDir.getY());

			return getBoard().getCell(newPos);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void run(){
		doInitialPositioning();
		while (!getBoard().isGameOverV2()){
				try {
					Cell toMove = getNextCell();
					if (toMove == null)
						break;
					this.move(toMove);
				} catch (InterruptedException ignored) {}
		}
	}
}