package game;

import environment.LocalBoard;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class stateSender extends Thread{
    private ObjectOutputStream oos;

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
                oos.writeObject(new GameState(board.getCells(), board.getSnakes(), board.getObstacles(), board.getGoal(), board.getGoalPosition() ));
                oos.flush();
                oos.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

