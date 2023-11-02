package game;

import java.awt.*;
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

	public Cell generatePosition(){
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

	public void move(Cell bp) {
		//System.out.print(Thread.currentThread().getName() + " " + bp.toString());
		try {
			bp.request(this);
			getBoard().setChanged();
			if(bp.isOcupiedByGoal()){
				Goal remove = bp.removeGoal();
				remove.captureGoal();
			}
		} catch (InterruptedException e) {
			System.out.println("erro");
		}
	}


	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());

		while(true) {
			try {
				Cell toMove = generatePosition();
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