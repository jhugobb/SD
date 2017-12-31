package Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Match implements Runnable{
    private Lock playerLock;
    private Integer id;
    private Set<String> blue;
    private Set<String> red;
    private Integer winner;
    private Boolean running;

    public Match(Integer id, Set<String> players) {
        this.playerLock = new ReentrantLock();
        this.id = id;
        this.blue = new HashSet<>();
        this.red = new HashSet<>();
        Iterator<String> it = players.iterator();
        while (it.hasNext()) {
            blue.add(it.next());
            red.add(it.next());
        }
        this.winner = null;
        running = true;
    }

    public Match(int size, Set<String> players) {
    }

    public Integer getId() {
        return id;
    }

    public Set<String> getBlue() {
        return blue;
    }

    public Set<String> getRed() {
        return red;
    }

    public Integer getWinner() {
        return winner;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {

    }

}
