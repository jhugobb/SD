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
        this.queue.add(message);
        notifyAll();
    }

    synchronized public String read() throws InterruptedException {
        while(isEmpty()) {
            wait();
        }

        String message = this.queue.get(index);
        this.index++;

        return message;
    }

    synchronized public void reset() {
        queue.clear();
        this.index = 0;
    }

    synchronized private boolean isEmpty() {
        return queue.size() == index;
    }

    synchronized public void timeOut(){
        this.timeOut = true;
        this.write("DONE");
    }

    synchronized public boolean isValid(){
        return !(this.isEmpty() && this.timeOut);
    }
}
