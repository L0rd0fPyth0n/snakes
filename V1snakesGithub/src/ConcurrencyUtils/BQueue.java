package ConcurrencyUtils;

import java.util.LinkedList;
import java.util.Queue;

public   class BQueue<T> {
    //BlockingQueue

    //recurso partilhado <T> é o tipo de elementos na fila !
    private Queue<T> list; //add mete no fim da fila!

    public BQueue(){
        this.list = new LinkedList< T >();
    }

    //As operações bloqueantes devem propagar as
    //exceções de interrupção de thread (InterruptedException) para os clientes.
    public synchronized void put(T element) throws InterruptedException {
        list.add( element);
        this.notifyAll();
    }
    //TODO converter para locks explicitos
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

    public boolean isEmpty(){
        return list.isEmpty();
    }
    public void clear(){
        list.clear();
    }
}
