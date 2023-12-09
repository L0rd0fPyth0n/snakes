package remote;


import game.GameState;
import game.Obstacle;
import game.Server;
import gui.Main;
import gui.SnakeGui;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import static game.Server.ADDR;
import static game.Server.PORT;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */
public class Client extends Thread{

	private Socket s;

	private ObjectInputStream ois;

	private RemoteBoard rb;
	private SnakeGui game;
	public Client(String addr, int port){
		try {
			this.s = new Socket(InetAddress.getByName(addr), port);
		} catch (IOException e){
			e.printStackTrace();
			 throw new RuntimeException("No games happennig right now!!");
		}
		try {
			PrintWriter out = new PrintWriter (new BufferedWriter(new OutputStreamWriter ( s . getOutputStream ())) ,true );
			this.rb = new RemoteBoard(out);
			this.game = new SnakeGui(rb,600,0);
			this.ois = new ObjectInputStream(s.getInputStream()); //canal receber estado atual do jogo
			game.init();
		} catch (IOException e) {
		}
	}
	@Override
	public void run() {
		try {
			while(true){
				try {
					GameState gameState = (GameState) ois.readObject();
					//TODO EOFExcetion
					//TODO OptionalDataException

					rb.clearAllCells();
					rb.setCells(gameState.cells());
					rb.setSnakes(gameState.snakes());

					rb.setGoalPosition(gameState.goalPosition());

					rb.getCell(gameState.goalPosition()).setGameElementGoal(gameState.goal());
					rb.setObstacles(gameState.obstacles());
					for (Obstacle obs : gameState.obstacles()){
						try {
							rb.getCell(obs.getPos()).setGameElementObstacle(obs);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					rb.setChanged();

				}catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				catch ( OptionalDataException e) {
					e.printStackTrace();
					System.out.println("eof: "+ e.eof);
					System.out.println("length: "+ e.length);
					throw new RuntimeException(e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			try {
				ois.close();
				s.close();
				System.out.println("Cliente fechou os Sockets e canais");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	public static void main(String[] args){
		Main.main(args);


		new Client(ADDR, PORT).start();
		new Client(ADDR,PORT).start();


	}
}
