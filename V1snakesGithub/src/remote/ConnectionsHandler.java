package remote;

import environment.LocalBoard;
import game.HumanSnake;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionsHandler extends Thread {

    private final LocalBoard board;

    public ConnectionsHandler(LocalBoard board){
        this.board = board;
    }
    private int i = 0;
    @Override
    public void run() {
        while (true){
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8888);

                Socket clientSocket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                HumanSnake newPlayer = new HumanSnake(i++,this.board,inputStream);
                board.addSnake(newPlayer);
                //TODO por snake numa cell
                //TODO board.addGameElement();

                board.setChanged();
            } catch (IOException e) {
            }


        }
    }
}
