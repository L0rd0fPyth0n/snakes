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
	public RemoteBoard(ObjectOutputStream oos) {
		this.oos = oos;
	}

	private ObjectOutputStream oos;
	public static final Map<Integer,Direction> keyToDirection = new HashMap<>();

	static {
		keyToDirection.put(VK_UP, Direction.UP);
		keyToDirection.put(VK_DOWN, Direction.DOWN);
		keyToDirection.put(VK_LEFT, Direction.LEFT);
		keyToDirection.put(VK_RIGHT, Direction.RIGHT);
	}
	@Override
	public void handleKeyPress(int keyCode) {
		try {
			oos.writeObject(keyToDirection.get(keyCode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleKeyRelease() {
		// TODO
	}

	@Override
	public void init() {

	}


	

}
