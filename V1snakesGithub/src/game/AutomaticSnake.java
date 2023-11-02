package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	public Cell generateNextPosition(){
		Cell aux = this.getCells().getFirst();

		BoardPosition bp = null;
		while(true) {
			Direction dir = Direction.getRandomDirection();

			int newX = aux.getPosition().x + dir.getX();
			int newY = aux.getPosition().y + dir.getY();

			bp = new BoardPosition(newX, newY);

			if (!getBoard().isOutOfBound(bp)) {
				break;
			}
		}
		Cell aux2 = getBoard().getCell(bp);

		//if(comer maca)


		aux.release();

		//this.getCells().remove(aux);
		this.getCells().remove(getCells().getLast());
		this.getCells().add(aux2);
		return aux2;
	}

	public void move(Cell bp) throws InterruptedException {
		bp.request(this);
		cells.add(0,bp);
		if(!hasToGrow())
			cells.removeLast();
		if(bp.isOcupiedByGoal()) {
			Goal remove = bp.removeGoal();

			int amuontToGrow = remove.captureGoal();
			super.startGrowing(amuontToGrow);
		}

		getBoard().setChanged();
	}


	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());
		while(true) {
			try {
				Cell toMove = generateNextPosition();
				this.move(toMove);
				if(this.getBoard().getCell(this.getBoard().getGoalPosition()).
						getGoal().getValue() ==10 ){
					this.interrupt();
					this.join();
				}
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			} catch (InterruptedException e) {
				System.out.println("sai do move automatic");
				return;
			}
		}
	}



}