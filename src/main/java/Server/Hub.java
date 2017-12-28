package Server;

import java.util.ArrayList;

/**
 * Buffer thread-safe para envio e receção de notificações. Um leitor bloqueia
 * quando o buffer está vazio. Um escritor nunca bloqueia, visto que o buffer
 * não tem restrições de tamanho, podendo crescer indefinidamente.
 */
public class Hub {

    private ArrayList<String> notifications;
    private int index;

    public Hub() {
        this.notifications = new ArrayList<>();
        this.index = 0;
    }

    synchronized public void write(String message) {
        this.notifications.add(message);
        notifyAll();
    }

    synchronized public String read() throws InterruptedException {
        while(isEmpty()) {
            wait();
        }

        String message = this.notifications.get(index);
        this.index++;

        return message;
    }

    synchronized public void reset() {
        this.index = 0;
    }

    synchronized public void acknowledge(int amount) {
        for (int i = 0; i < amount; i++){
            this.notifications.remove(0);
        }
        this.index = 0;
    }

    synchronized public boolean isEmpty() {
        return notifications.size() == index;
    }
}
