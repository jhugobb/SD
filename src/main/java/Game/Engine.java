package Game;

import Exceptions.InvalidAuthenticationException;
import Exceptions.PlayerAlredyExistsException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Engine {
    private TreeMap<Integer,Set<String>> queue;
    private HashMap<Integer,Match> matchLog;
    private HashMap<String,Player> players;
    private Lock playerLock;
    private Lock gameLock;

    public Engine() {
        this.playerLock = new ReentrantLock();
        this.gameLock = new ReentrantLock();
        this.queue = new TreeMap<>();
        for (int i=0; i<=10; i++) {
            this.queue.put(i, new HashSet<>());
        }
        this.matchLog = new HashMap<>();
        this.players = new HashMap<>();
   }

    public void signUp(String username, String password) throws PlayerAlredyExistsException {
        playerLock.lock();
        try {
            if (players.containsKey(username)){
                throw new PlayerAlredyExistsException("O nome de utilizador não está disponível");
            }
            else {
                players.put(username, new Player(username, password));
            }
        } finally {
            playerLock.unlock();
        }
    }

    public Player login(String username, String password) throws InvalidAuthenticationException {
        Player player;
        playerLock.lock();
        try {
            player = players.get(username);
            if (player == null || !player.auth(password))
                throw new InvalidAuthenticationException("Os dados inseridos estão incorretos");
            player.resetHub();
        } finally {
            playerLock.unlock();
        }

        return player;
    }

    public Integer intoQueue(Player player) {
        gameLock.lock();
        try {
            Set<String> rank = this.queue.get(player.getRank());
            rank.add(player.getUsername());
        } finally {
            gameLock.unlock();
        }
        return player.getRank();
    }

}
