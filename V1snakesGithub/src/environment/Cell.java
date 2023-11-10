package environment;

import java.io.Serializable;
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
public class Cell {
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	private Lock lock = new ReentrantLock();
	private Condition isEmpty = lock.newCondition();
	private Condition hasSnake = lock.newCondition();

	//Conditional Variables


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

	//TODO aqui ver se duas snakes nao comem o mesmo goal e problema de snake nao bloquear
	//TODO contra outra snake
	public void request(Snake snake)  {
		lock.lock();
		try {
			while (this.isOcupied()) {
				isEmpty.await();
			}
			ocuppyingSnake=snake;
			if(this.isOcupiedByGoal()) {
				Goal remove = this.removeGoal();

				int amuontToGrow = 0;
				try {
					snake.startGrowing(remove.captureGoal());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

			}
			hasSnake.signalAll();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	//TODO EXLCUSION
	public void release() {
		lock.lock();
		try {
			while(!this.isOcupied()){
				hasSnake.await();
			}
			this.ocuppyingSnake = null;
			isEmpty.signalAll();
		} catch (InterruptedException e) {
			System.out.println("catched release() exclusion");
		} finally {
			lock.unlock();
		}
	}
	public boolean isCompletelyUnoccupied(){
		return ocuppyingSnake == null && gameElement == null;
	}

	public boolean isOcupiedBySnake() {
			return ocuppyingSnake!=null;
	}


		public  void setGameElement(GameElement element) {
			// TODO coordination and mutual exclusion
			gameElement=element;

		}

		public boolean isOcupied() {
			return isOcupiedBySnake() || (gameElement!=null && gameElement instanceof Obstacle);
		}


		public Snake getOcuppyingSnake() {
			return ocuppyingSnake;
		}


		// TODO coordination ?
		public  Goal removeGoal() {
			Goal g = getGoal();
			gameElement = null;
			return g;
		}

		public void removeObstacle() {
			lock.lock();
			try {
				while(!this.isOcupied()){
					hasSnake.await();
				}
				gameElement = null;
				isEmpty.signalAll();
			} catch (InterruptedException e) {
				System.out.println("catched release() exclusion");
			} finally {
				lock.unlock();
			}

		}


		public Goal getGoal() {
			return (Goal)gameElement;
		}


		public boolean isOcupiedByGoal() {
			return (gameElement!=null && gameElement instanceof Goal);
		}

		@Override
		public String toString(){
			return "(" + position.x + "," + position.y + ")";
		}
}
