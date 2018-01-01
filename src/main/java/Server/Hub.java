package Server;

import java.util.ArrayList;

/**
 * Buffer thread-safe para envio e receção de notificações. Um leitor bloqueia
 * quando o buffer está vazio. Um escritor nunca bloqueia, visto que o buffer
 * não tem restrições de tamanho, podendo crescer indefinidamente.
 */
public class Hub {

    private ArrayList<String> queue;
    private int index;
    private boolean timeOut;

    public Hub() {
        this.queue = new ArrayList<>();
        this.index = 0;
        this.timeOut = false;
    }

    synchronized public void write(String message) {
        if(!timeOut){
            this.queue.add(message);
            notifyAll();
        }
    }

    synchronized public String read() throws InterruptedException {
        while(isEmpty()) {
            wait();
        }

        String message = this.queue.get(index);
        this.index++;

        return message;
    }

    synchronized public void reset(int amount) {
        for (int i = 0; i < amount; i++){
            this.queue.remove(0);
        }
        this.index = 0;
    }

    private synchronized boolean isEmpty() {
        return queue.size() == index;
    }

    public synchronized void timeOut(){
        this.timeOut = true;
    }
}
