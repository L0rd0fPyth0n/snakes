package ConcurrencyUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import ConcurrentUtils.BQueue;
public class FixedTPool {

    private final int NWorker;//TODO maior q zero

    private BQueue tasks;

    private PoolWorker[] pool;
    public FixedTPool(int NWorker) throws IllegalArgumentException {
        if(NWorker <=0) {
            throw new IllegalArgumentException();
        }
        this.NWorker = NWorker;
        this.tasks = new BQueue<Runnable>();

        this.pool = new PoolWorker[NWorker];

        for(int i = 0;i < NWorker; i++){
            this.pool[i] = new PoolWorker();
            this.pool[i].start();
        }
    }

    public void submitTask(Runnable task) {
        tasks.put(task);
    }
    public Runnable getTask(){
        return tasks.take();
    }

    private  class PoolWorker extends Thread{
    }


}
