package Game;

import Exceptions.InvalidAuthenticationException;
import Exceptions.PlayerAlredyExistsException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Engine {
    private HashMap<Integer, Set<String>> queue;
    private HashMap<Integer,Match> matches;
    private HashMap<String,Player> players;
    private Lock playerLock;
    private Lock gameLock;
    private Lock queueLock;

    public Engine() {
        this.playerLock = new ReentrantLock();
        this.gameLock = new ReentrantLock();
        this.queueLock = new ReentrantLock();
        this.queue = new HashMap<>();
        for (int i = 0; i < 10; i++)
            this.queue.put(i, new HashSet<>());
        this.matches = new HashMap<>();
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
            if (player.getAuthenticated())
                throw new InvalidAuthenticationException("Já existe outro utilizador autenticado");
            player.login();
            player.resetHub();
        } finally {
            playerLock.unlock();
        }
        return player;
    }

    public String intoQueue(Player player) {
        Integer rank = player.getRank();
        Set<String> players = null;
        queueLock.lock();
        try {
            Set<String> rankQueue = this.queue.get(rank);
            rankQueue.add(player.getUsername());
            if (weHaveAParty(rank)) {
                players = this.getAParty(rank);
            }
        } finally {
            queueLock.unlock();
        }
        if (players != null) {
            gameLock.lock();
            try {
                Match m = new Match(matches.size()+1, players);
                this.matches.put(matches.size(), m);
                Thread t = new Thread(m);
                t.start();
            } finally {
                gameLock.unlock();
            }
            playerLock.lock();
            try {
                this.deQueuePlayers(players);
            } finally {
                playerLock.unlock();
            }
        } else {
            playerLock.lock();
            try {
                player.queueUp();
            } finally {
                playerLock.unlock();
            }
        }
        return "OK";
    }

    private void deQueuePlayers(Set<String> players) {
        players.forEach(p -> this.players.get(p).dequeue());
    }

    private Boolean weHaveAParty(Integer rank) {
        queueLock.lock();
        try {
            Integer size = queue.get(rank).size();
            Set<String> above = queue.get(rank+1);
            Set<String> below = queue.get(rank-1);
            if (above != null) size+= above.size();
            if (below != null) size+=below.size();
            return size >= 10;
        } finally {
            queueLock.unlock();
        }
    }

    private Set<String> getAParty(Integer rank) {
        Set<String> res = new HashSet<>();
        Set<String> queueRank = queue.get(rank);
        Set<String> above = queue.get(rank+1);
        Set<String> below = queue.get(rank-1);
        getPlayers(res, queueRank);
        getPlayers(res, above);
        getPlayers(res, below);
        return res;
    }

    private void getPlayers(Set<String> res, Set<String> rank) {
        if (rank != null) {
            Iterator<String> it = rank.iterator();
            while (res.size() < 10 && it.hasNext()) {
                res.add(it.next());
            }
            rank.removeAll(res);
        }
    }

    // engine -> login, logout, signUp, queue, criar matches, por matches a correr
    // match -> escrever para os hubs.

}
