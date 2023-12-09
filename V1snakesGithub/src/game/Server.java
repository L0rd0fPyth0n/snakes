package game;

import environment.LocalBoard;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static environment.LocalBoard.NUM_SNAKES;


public class Server extends Thread{

    public static final int PORT = 8885;

    public static final String ADDR = "localhost";

    private final LocalBoard board;


    public Server(LocalBoard board){
        this.board = board;
    }
    private int i = NUM_SNAKES + 1;
    @Override
    public void run() {
        ServerSocket serverSocket =  null;
            try {
                serverSocket = new ServerSocket(PORT);
                while (true){
                    Socket clientSocket = serverSocket.accept();

                    System.out.println("Connection established!! Welcome ");
                    HumanSnake newPlayer = new HumanSnake(i++,
                            this.board,
                            new InputStreamReader( clientSocket . getInputStream ())
                    );

                    new stateSender(clientSocket,this.board).start();
                    newPlayer.start();
                    board.addSnake(newPlayer);
                    board.setChanged();
                }
            }
            catch (IOException e) {
            }
            finally {
                try {
                  serverSocket.close();
                  System.out.println("Server socket closed!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
