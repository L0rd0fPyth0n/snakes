package ConcurrencyUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import ConcurrentUtils.BQueue;
public class FixedTPool {

    private final int NWorker;//TODO maior q zero

    private BQueue tasks;
    private boolean running = true;
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
    public void stop(){
        this.running = false;
    }

    public void submitTask(Runnable task) {
        tasks.put(task);
    }
    public Runnable getTask(){
        return tasks.take();
    }

    private  class PoolWorker extends Thread{
        @Override
        public void run() {
            while(running){
                Runnable task = getTask();

                if(task.getState()){//sees if thread isn't in sleep
                    //if it sleeps
                    tasks.submitTask(task);
                }else{

                    task.run();
                    //When thread is put to sleep,
                    tasks.submitTask(task);
                }

            }
        }
    }
}
