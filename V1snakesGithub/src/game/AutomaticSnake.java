package game;

import environment.LocalBoard;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

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
//	protected Cell getNextCell(){
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


	protected Cell getNextCell(){
		Cell head = getCells().getFirst();

		List<BoardPosition> neighbourPos = getBoard().getNeighboringPositions(head);

		List<Cell> freePositions = neighbourPos.stream()
				.map((bp) -> getBoard().getCell(bp))
				.filter((c) -> !getCells().contains(c))
				.toList();
		Map<Double,Cell> m = new HashMap<>();
		BoardPosition goalPos = getBoard().getGoalPosition();
		for(Cell c : freePositions) {
			Double distToGoal = c.getPosition().distanceTo(goalPos);
			m.put(distToGoal,c);
		}
		double min= Collections.min(m.keySet());
		return m.get(min);
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