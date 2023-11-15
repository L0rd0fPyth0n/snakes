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


		BoardPosition goalPos = getBoard().getGoalPosition();
		List<Cell> freePositions = neighbourPos.stream()
				.map((bp) -> getBoard().getCell(bp))
				.filter((c) -> !getCells().contains(c))
				.sorted((c1,c2)-> (int) Math.signum(c1.getPosition().distanceTo(goalPos)  - c2.getPosition().distanceTo(goalPos)))
				.toList();
		//		Map<Double,Cell> m = new HashMap<>();
		//		BoardPosition goalPos = getBoard().getGoalPosition();
		//		for(Cell c : freePositions) {
		//			Double distToGoal = c.getPosition().distanceTo(goalPos);
		//			m.put(distToGoal,c);
		//		}
		//		double min= Collections.min(m.keySet());
		//		return m.get(min);
		if(this.flag==true){
			//System.out.println("Dps do reset new POs 2"+ freePositions.get(0));
			//System.out.println(freePositions.get(0).isOcupied());
			this.flag=false;
		}
		//ISTO PQ MM ASSIM SE A SNAKE FICAR SEM SITIO PRA IR TAVA A DAR INDEX OUT OF BOUNDS
		if(freePositions.isEmpty()){
			return null;
		}

		return freePositions.get(0);

	}

	@Override
	public void run() {
		doInitialPositioning();
		//System.out.println("initial size: "+ cells.size());
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
				if(getBoard().isGameOverV2() ){
					System.out.println(getName() + " eu entrei aqui");
					break;
				} else {
				//Go another direction
				Cell head = this.getCells().getFirst();


				List<BoardPosition> neighbourPos = this.getBoard().getNeighboringPositions(head);


				BoardPosition goalPos = this.getBoard().getGoalPosition();
				List<Cell> freePositions = neighbourPos.stream()
						.map((bp) -> this.getBoard().getCell(bp))
						.filter((c) -> !this.getCells().contains(c) && (!c.isOcupied()) )
						.sorted((c1,c2)-> (int) Math.signum(c1.getPosition().distanceTo(goalPos)  - c2.getPosition().distanceTo(goalPos) ))
						.toList();

				try {
					if(freePositions.isEmpty()){
						break;
					} else {
						this.move(freePositions.get(0));
					}
				} catch (InterruptedException ex) {
					System.out.println("erro 1ª reposiçao");
				}
			}
		}
	}

}
}