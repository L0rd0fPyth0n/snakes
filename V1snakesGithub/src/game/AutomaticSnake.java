package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}
	public BoardPosition movementVector(Cell from, Cell to) {
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
	public Cell generatePosToGoal(){
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

		currCell.release();

		//this.getCells().remove(aux);
//		this.getCells().remove(getCells().getLast());
//		this.getCells().add(0,newPos);
		return newPos;
	}


	public void move(Cell bp)  {
		bp.request(this);
		cells.add(0,bp);

		if(!hasToGrow()) {
			cells.removeLast();
		}

		getBoard().setChanged();
	}


	@Override
	public void run() {
		doInitialPositioning();
		System.out.println("initial size: "+cells.size());
		while(true) {
			try {
				Cell toMove = generatePosToGoal();

				this.move(toMove);

				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
				System.out.println("sai do move automatic");
				break;
				//return;
			}
		}
	}
}