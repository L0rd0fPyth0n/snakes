package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
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
                BoardPosition goalPosition = board.getGoalPosition();
                //CELULA ATUAL DO GOAL
                Cell GoalCell = board.getCell(goalPosition);
                oos.writeObject(new GameState(board.getCells(), board.getSnakes(), board.getObstacles(), (Goal) GoalCell.getGameElement(), board.getGoalPosition() ));
                oos.flush();
                oos.reset();
                Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

