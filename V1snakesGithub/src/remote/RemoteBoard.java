package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Obstacle;
import game.Snake;

import static java.awt.event.KeyEvent.*;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */



public class RemoteBoard extends Board{
	public RemoteBoard(PrintWriter oos) {
		this.oos = oos;
	}

	private PrintWriter oos;
	public static final Map<Integer,String> keyToDirection = new HashMap<>();

	static {
		keyToDirection.put(VK_UP, "U");
		keyToDirection.put(VK_DOWN, "D");
		keyToDirection.put(VK_LEFT, "L");
		keyToDirection.put(VK_RIGHT, "R");
	}

	@Override
	public void handleKeyPress(int keyCode) {
		String dir = keyToDirection.get(keyCode);
		if(dir != null) {
			oos.println(dir);
		}
	}

	@Override
	public void handleKeyRelease() {
	}

	@Override
	public void init() {
	}

	public void clearAllCells(){
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y].release();
			}
		}
	}
}
