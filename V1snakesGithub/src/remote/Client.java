package remote;


import gui.SnakeGui;

import java.util.HashMap;
import java.util.Map;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	public static void main(String[] args) {
		RemoteBoard rb = new RemoteBoard();
		SnakeGui game = new SnakeGui(rb,600,0);
		game.init();
	}

}
