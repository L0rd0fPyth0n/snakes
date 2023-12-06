package remote;


import environment.BoardPosition;
import environment.Cell;
import game.GameState;
import game.Obstacle;
import game.Snake;
import gui.Main;
import gui.SnakeGui;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import java.net.UnknownHostException;
import java.util.List;
/** Remore client, only for part II
 * 
 * @author luismota
 *
 */
public class Client extends Thread{

	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	private RemoteBoard rb;
	private SnakeGui game;
	public Client(){
		try {
			this.s = new Socket(InetAddress.getLocalHost(), 8888);
		} catch (IOException e){
			 throw new RuntimeException("No games happennig right now!!");
		}
		//TODO FECHAR SOCKETS E CANAIS
		try {
			this.oos = new ObjectOutputStream(s.getOutputStream());
			PrintWriter out = new PrintWriter (new BufferedWriter(new OutputStreamWriter ( s . getOutputStream ())) ,true );
			this.rb = new RemoteBoard(out);
			this.game = new SnakeGui(rb,600,0);
			this.ois = new ObjectInputStream(s.getInputStream()); //canal receber estado atual do jogo
			game.init();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void run() {
		try {
			while(true){
				try {
					GameState gameState = (GameState) ois.readObject();
					rb.clearAllCells();
					rb.setCells(gameState.cells());
					rb.setSnakes(gameState.snakes());

					rb.setGoalPosition(gameState.goalPosition());

					rb.getCell(gameState.goalPosition()).setGameElementGoal(gameState.goal());
					System.out.println(gameState.goal());
					rb.setObstacles(gameState.obstacles());
					for (Obstacle obs : gameState.obstacles() ){
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		Main.main(args);


		new Client().start();
		//new Client().start();
	}
}
