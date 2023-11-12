package ConcurrencyUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.State.TIMED_WAITING;

public class FixedTPool {
    //We will use an extra worker

    //TODO stop threadpool
    final int NWorker;//TODO maior q zero

    private BQueue<Runnable> tasks;
    private boolean running = true;
    PoolWorker[] pool;

    //FixedTPoolManager manager;
    public FixedTPool(int NWorker) throws IllegalArgumentException {
        if(NWorker <=0) {
            throw new IllegalArgumentException();
        }
        this.NWorker = NWorker;
        this.tasks = new BQueue<>();

        this.pool = new PoolWorker[NWorker];

        for(int i = 0;i < NWorker; i++){
            this.pool[i] = new PoolWorker();
            this.pool[i].start();
        }
        //this.manager = new FixedTPoolManager(this);
        //this.manager.start();
    }
    public void stop(){
        this.running = false;
    }

    public void submitTask(Runnable task) throws InterruptedException {
        tasks.put(task);
    }
    public Runnable getTask() throws InterruptedException {
        return tasks.take();
    }

    private class PoolWorker extends Thread{
        public Runnable task = null;

        public void setTask(Runnable t){
            this.task = t;
        }
        @Override
        public void run() {
            while(running){
                task = null;
                try {
                    task = getTask();
                    task.run();
                    //Task completed

                } catch (InterruptedException e) {

                }
                }
        }
/*

                if(task.getState().equals(TIMED_WAITING)){//sees if thread isn't in sleep
                    //if it sleeps
                    tasks.submitTask(task);
                }else{

                    task.run();
                    //When thread is put to sleep,
                    tasks.submitTask(task);
                }
*/

            }
        }


/*
    public static void main(String[] args) {
            //FixedTPool pool = new FixedTPool(2);
            ExecutorService pool = Executors.newFixedThreadPool(1);
            //ThreadPoolExecutor pool = F
            pool.submit(new Thread(){
                public void run(){
                    while(true){
                        System.out.println("AAAA");

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            } );

            pool.submit(new Thread(){
                public void run(){
                    while(true){
                        System.out.println("BBBB");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } );

            pool.submit(new Thread(){
                public void run(){
                    while(true){
                        System.out.println("CCCCC");
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } );

    }



 */
