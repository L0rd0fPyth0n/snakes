package remote;


import environment.BoardPosition;
import environment.Cell;
import game.GameState;
import game.Snake;
import gui.SnakeGui;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import java.util.List;
/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	public static void main(String[] args) {
		Socket s = null;
		try {
			try {
				s = new Socket(InetAddress.getLocalHost(), 8888);
			} catch (ConnectException e){
				System.out.println("No games happennig right now!!");
				return;
			}
			//TODO FECHAR SOCKETS E CANAIS
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

			PrintWriter out =
					new PrintWriter (new BufferedWriter(
							new OutputStreamWriter ( s . getOutputStream ())) ,
							true );
			RemoteBoard rb = new RemoteBoard(out);
			SnakeGui game = new SnakeGui(rb,600,0);
			game.init();


			ObjectInputStream ois = new ObjectInputStream(s.getInputStream()); //canal receber estado atual do jogo

			while(true){
				try {
					GameState gameState = (GameState) ois.readObject();
					rb.clearAllCells();
					System.out.println("Lido");

					List<Snake> sankeList = gameState.snakeList();
					System.out.println(sankeList);

					for(Snake snake : sankeList){
						System.out.println(snake.getCells());
						for(Cell c :snake.getCells()){
							BoardPosition pos = c.getPosition();
							//rb.getCell(pos).request(snake);
						}
						rb.setChanged();
					}
				}catch (ClassNotFoundException /*| InterruptedException */e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
			}
		}
	}
}
