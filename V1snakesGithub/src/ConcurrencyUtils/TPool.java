package ConcurrencyUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedTPool {

    private final int NWorker;//TODO maior q zero

    private BQueue tasks;

    private PoolWorker[] pool;
    public FixedTPool(int NWorker){
        if(NWorker <=0)
            throw IllegalArgumentException();
        this.NWorker = NWorker;

        this.tasks = new BQueue();

        this.pool = new PoolWorker[NWorker];

        for(int i = 0;i < NWorker; i++){
            this.pool[i] = new PoolWorker();
            this.pool[i].start();
        }
    }

    public void submitTask(Runnable task){
        tasks.put(task);
    }

    private  class PoolWorker extends Thread{
    }

    private  class BQueue<Runnable> {
        //BlockingQueue


        //recurso partilhado <T> é o tipo de elementos na fila !

        private Queue<T> list; //add mete no fim da fila!

        public BQueue(){
            this.list = new LinkedList< java.lang.Runnable >();
        }

        public BQueue(Queue<T> list) {
            this.list = list;
        }

        //As operações bloqueantes devem propagar as
        //exceções de interrupção de thread (InterruptedException) para os clientes.
        public synchronized void put(T element) throws InterruptedException {
                list.add( element);
        }

        public synchronized T take() throws InterruptedException {
            while (list.isEmpty()) {
                this.wait();
            }
            T aux = list.remove();
            this.notifyAll();
            return aux;
        }

        public void size(){
            list.size();
        }

        public void clear(){
            list.clear();
        }
    }
}
