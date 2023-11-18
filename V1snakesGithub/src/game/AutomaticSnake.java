package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

import java.util.*;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}

	protected Cell getNextCell(){
		Cell head = getCells().getFirst();

		List<BoardPosition> neighbourPos = getBoard().getNeighboringPositions(head);
		List<Cell> freePositions = null;
		if(this.wasInterrupted){
			 freePositions = neighbourPos.stream()
							.map((bp) -> this.getBoard().getCell(bp))
							.filter((c) -> (!this.getCells().contains(c)) && (!c.isOcupied()) )
							.sorted(this::compare)
							.toList();
		}else {
			freePositions = neighbourPos.stream()
					.map((bp) -> getBoard().getCell(bp))
					.filter((c) -> !getCells().contains(c))
					.sorted(this::compare)
					.toList();
		}
		this.wasInterrupted =false;
		return freePositions.isEmpty() ? null : freePositions.get(0);
	}

	@Override
	public void run() {
		doInitialPositioning();
		while(!getBoard().isGameOverV2()) {
			try {
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
				Cell toMove = getNextCell();
				if(toMove == null){
					break;
				}else {
					this.move(toMove);
				}
			} catch (InterruptedException e) {
//				if(!getBoard().isGameOverV2()){
//					Cell head = this.getCells().getFirst();
//					List<BoardPosition> neighbourPos = this.getBoard().getNeighboringPositions(head);
//					List<Cell> freePositions = neighbourPos.stream()
//							.map((bp) -> this.getBoard().getCell(bp))
//							.filter((c) -> (!this.getCells().contains(c)) && (!c.isOcupied()) )
//							.sorted(this::compare)
//							.toList();
//					if(freePositions.isEmpty()){
//						break;
//					} else {
//						try {
//							this.move(freePositions.get(0));
//						} catch (InterruptedException ex) {
//							ex.printStackTrace();
//						}
//					}
//
//				} else {
//					break;
//			}
		}
	}
		System.out.println(Thread.currentThread() + " Class: AitoSnake ended");
}

	public int compare(Cell c1, Cell c2){
		BoardPosition goalPos = this.getBoard().getGoalPosition();
		return  (int) Math.signum(c1.getPosition().distanceTo(goalPos)  - c2.getPosition().distanceTo(goalPos));
	}
}