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
    private  Socket s;
    private final LocalBoard board;

    public stateSender(Socket s, LocalBoard board) {
        this.s = s;
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
        while (!board.isGameOverV2()){
            try {
                BoardPosition goalPosition = board.getGoalPosition();
                //CELULA ATUAL DO GOAL
                Cell GoalCell = board.getCell(goalPosition);

                oos.writeObject(
                        new GameState(
                                board.getCells(),
                                board.getSnakes(),
                                board.getObstacles(),
                                (Goal) GoalCell.getGameElement(),
                                board.getGoalPosition()
                        )
                );

                oos.flush();
                oos.reset();

               // Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
            } catch (IOException /*| InterruptedException */e) {
                e.printStackTrace();
            }
        }


        try {
            oos.close();
            s.close();
            System.out.println("Server fechou os Sockets e canais");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

