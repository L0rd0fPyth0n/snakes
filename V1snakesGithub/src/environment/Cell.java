package environment;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.midi.SysexMessage;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;
import game.AutomaticSnake;
/** Main class for game representation.
 *
 * @author luismota
 *
 */
public class Cell implements Serializable{
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private transient GameElement gameElement=null;
	private transient Lock lock = new ReentrantLock();
	private transient Condition isEmpty = lock.newCondition();

	public GameElement getGameElement() {
		return gameElement;
	}

	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}


	public void request(Snake snake) throws InterruptedException {
		lock.lock();
		try {
			while (this.isOcupied())
				isEmpty.await();
			ocuppyingSnake=snake;
			if(this.isOcupiedByGoal() )
				snake.capture(getGoal());

			if(snake.getBoard().isGameOverV2()) snake.getCells().addFirst(this);

		} catch (InterruptedException e) {
			snake.interrupt();
			snake.getBoard().interruptAllObs();

		} finally {
			lock.unlock();
		}
	}

	public void release() {
		lock.lock();
		this.ocuppyingSnake = null;
		isEmpty.signalAll();
		lock.unlock();
	}

	public boolean isCompletelyUnoccupied(){
		return ocuppyingSnake == null && gameElement == null;
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}


	public void setGameElement(GameElement element)  {
		lock.lock();
		gameElement = element;
		lock.unlock();
	}

	public void removeObstacle() {
		lock.lock();
		gameElement = null;
		isEmpty.signalAll();
		lock.unlock();
	}

	public  Goal removeGoal() {
		Goal g = getGoal();
		gameElement = null;
		return g;
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() ||
				(gameElement!=null && gameElement instanceof Obstacle);
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}

	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}

	@Override
	public String toString(){
		return "(" + position.x + "," + position.y + ")";
	}
}
