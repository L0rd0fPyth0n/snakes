package game;

import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Obstacle extends GameElement {
	
	private static final int NUM_MOVES=10;
	public static final int OBSTACLE_MOVE_INTERVAL = 2000;
	private int remainingMoves=NUM_MOVES;
	private final Board board;

	private BoardPosition pos;

	public Obstacle(Board board) {
		super();
		this.board = board;

	}
	public BoardPosition getPos() {
		return pos;
	}

	public void setPos(BoardPosition pos) {
		this.pos = pos;
	}
	public int getRemainingMoves() {
		return remainingMoves;
	}
	public void decrementRemainingMoves(){
		remainingMoves--;
	}
}
