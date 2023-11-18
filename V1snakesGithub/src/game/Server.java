package game;

import environment.LocalBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static environment.LocalBoard.NUM_SNAKES;

public class Server extends Thread{
    private final LocalBoard board;

    public Server(LocalBoard board){
        this.board = board;
    }
    private int i = NUM_SNAKES + 1;
    @Override
    public void run() {
            try (ServerSocket serverSocket =  new ServerSocket(8888)) {
                while (true){
                    Socket clientSocket = serverSocket.accept();
                    ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                    HumanSnake newPlayer = new HumanSnake(i++, this.board, inputStream);


                    new stateSender(clientSocket,this.board).start();
                    newPlayer.start();
                    board.addSnake(newPlayer);
                    board.setChanged();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        private static class stateSender extends Thread{
            private  ObjectOutputStream oos;

            private final LocalBoard board;

            public stateSender(Socket s, LocalBoard board) {
                oos = null;
                try {
                    oos = new ObjectOutputStream(s.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.board = board;
            }

            @Override
            public void run() {
                while (true){
                    try {
                        oos.writeObject(new GameState(board.getSnakes()));
                        System.out.println("Escrito");
                        Thread.sleep(50);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

}
