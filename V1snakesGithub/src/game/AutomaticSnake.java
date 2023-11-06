package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}
	private BoardPosition movementVector(Cell from, Cell to) {
		BoardPosition d = new BoardPosition(to.getPosition().x - from.getPosition().x,
				to.getPosition().y - from.getPosition().y);
		if (Math.abs(d.x) > Math.abs(d.y)) {
			d = new BoardPosition((int) Math.signum(d.x), 0);
		} else if (Math.abs(d.x) <= Math.abs(d.y)) {
			d = new BoardPosition(0, (int) Math.signum(d.y));
		}
		return d;
	}
	//NewPosition = currentPosition + VectorToTheGoal
	protected Cell getNextCell(){
		BoardPosition currPos = getCells().getFirst().getPosition();
		Cell currCell = getBoard().getCell(currPos);

		BoardPosition goalBp = getBoard().getGoalPosition();
		Cell goalCell = getBoard().getCell( goalBp);

		BoardPosition newBP = null;
		while(true) {
			BoardPosition vector = movementVector(currCell, goalCell);
			newBP = new BoardPosition(currPos.x + vector.x, currPos.y + vector.y);
			if (!getBoard().isOutOfBound(newBP)) {
				break;
			}
		}
		Cell newPos = getBoard().getCell(newBP);
		return newPos;
	}
	@Override
	public void run() {
		doInitialPositioning();
		System.out.println("initial size: "+ cells.size());
		try {
			while(true){
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
				Cell toMove = getNextCell();
				this.move(toMove);
			}
		} catch (InterruptedException e) {
			System.out.println("GAME OVER!!!");
		}
	}
}