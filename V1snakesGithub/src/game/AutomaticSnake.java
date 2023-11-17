package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;
import gui.SnakeGui;

import java.util.*;
import java.util.stream.Collectors;
public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);
	}
//	private BoardPosition movementVector(Cell from, Cell to) {
//		BoardPosition d = new BoardPosition(to.getPosition().x - from.getPosition().x,
//				to.getPosition().y - from.getPosition().y);
//		if (Math.abs(d.x) > Math.abs(d.y)) {
//			d = new BoardPosition((int) Math.signum(d.x), 0);
//		} else if (Math.abs(d.x) <= Math.abs(d.y)) {
//			d = new BoardPosition(0, (int) Math.signum(d.y));
//		}
//		return d;
//	}
//	//NewPosition = currentPosition + VectorToTheGoal
//	protected Cell getNextCellv2(){
//		Cell currCell = getCells().getFirst();
//
//		BoardPosition goalBp = getBoard().getGoalPosition();
//		Cell goalCell = getBoard().getCell( goalBp);
//
//		while(true) {
//			BoardPosition vector = movementVector(currCell, goalCell);
//			BoardPosition currPos = currCell.getPosition();
//
//			BoardPosition newPos = new BoardPosition(currPos.x + vector.x, currPos.y + vector.y);
//			if (!getBoard().isOutOfBound(newPos)) {
//				Cell newCell = getBoard().getCell(newPos);
//				return newCell;
//			}
//		}
//	}


	//TODO FAZER NO FILTRO PARA NAO ANDAR NA DIAGONAL Q É ISSO Q ACONTECE DPS DO RESET
	protected Cell getNextCell(){
		Cell head = getCells().getFirst();

		List<BoardPosition> neighbourPos = getBoard().getNeighboringPositions(head);
		List<Cell> freePositions = null;
		if(this.flag){
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
		this.flag=false;

		//ISTO PQ MM ASSIM SE A SNAKE FICAR SEM SITIO PRA IR TAVA A DAR INDEX OUT OF BOUNDS
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