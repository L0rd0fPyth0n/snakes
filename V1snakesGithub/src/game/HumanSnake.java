package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

/** Class for a remote snake, controlled by a human

 @author luismota
 **/
public  class HumanSnake extends Snake {
	protected transient final InputStreamReader inputStream;
	protected transient final BufferedReader in;
	public HumanSnake(int id, Board board, InputStreamReader inputStream ) {
		super(id,board);
		this.inputStream = inputStream;
		//TODO fechar canais
		this.in = new BufferedReader (inputStream);
	}

	@Override
	protected Cell getNextCell() {
		while(true) {
			try {
				String dir = null;
				dir = in.readLine();


				int x = 0;
				int y = 0;
				switch (dir) {
					case "U":
						y = -1;
						x = 0;
						break;
					case "D":
						y = 1;
						x = 0;
						break;
					case "L":
						y = 0;
						x = -1;
						break;
					case "R":
						y = 0;
						x = 1;
				}

				BoardPosition headPos = cells.getFirst().getPosition();

				BoardPosition newPos = new BoardPosition(headPos.x + x, headPos.y + y);

				return getBoard().getCell(newPos);
			} catch (IOException io){

			}

		}
	}
	@Override
	public void run(){
		doInitialPositioning();
		while (!getBoard().isGameOverV2()){
				try {
					Cell toMove = getNextCell();
					if (toMove == null)
						continue;
					if(toMove.isOcupied())
						continue;
					this.move(toMove);
				} catch (InterruptedException ignored) {
				}
		}
	}
}