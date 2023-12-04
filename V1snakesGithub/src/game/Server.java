package game;

import environment.LocalBoard;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static environment.LocalBoard.NUM_SNAKES;

public class Server extends Thread{
    private final LocalBoard board;

    //TODO lista de clientes private List<>

    public Server(LocalBoard board){
        this.board = board;
    }
    private int i = NUM_SNAKES + 1;
    @Override
    public void run() {
            try (ServerSocket serverSocket =  new ServerSocket(8888)) {
                while (true){
                    Socket clientSocket = serverSocket.accept();

                    System.out.println("Connection established!! Welcome " );
                    HumanSnake newPlayer = new HumanSnake(i++, this.board, new InputStreamReader( clientSocket . getInputStream ()));


                    new stateSender(clientSocket,this.board).start();
                    newPlayer.start();
                    board.addSnake(newPlayer);
                    board.setChanged();
                }
            }
            catch (IOException e) {
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
