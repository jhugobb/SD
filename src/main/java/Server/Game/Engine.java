package Server.Game;

import Server.Exceptions.InvalidAuthenticationException;
import Server.Exceptions.PlayerAlredyExistsException;
import Server.Hub;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Engine {
    private HashMap<Integer, Queue<String>> queue;
    private HashMap<String,Player> players;
    private Lock playerLock;
    private Lock queueLock;

    public Engine() {
        this.playerLock = new ReentrantLock();
        this.queueLock = new ReentrantLock();
        this.queue = new HashMap<>();
        for (int i = 0; i < 10; i++)
            this.queue.put(i, new LinkedList<>());
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

    public Player login(String username, String password, Hub playerHub) throws InvalidAuthenticationException {
        Player player;
        playerLock.lock();
        try {
            player = players.get(username);
            if (player == null || !player.auth(password))
                throw new InvalidAuthenticationException("Os dados inseridos estão incorretos");
            if (player.getAuthenticated())
                throw new InvalidAuthenticationException("Já existe outro utilizador autenticado");
            player.login();
            player.setPlayerHub(playerHub);
        } finally {
            playerLock.unlock();
        }
        return player;
    }

    public String intoQueue(Player player) {
        Integer rank = player.getRank();
        if (rank == 10) rank--;
        Set<String> playersID = null;
        playerLock.lock();
        try {
            player.queueUp();
        } finally {
            playerLock.unlock();
        }
        queueLock.lock();
        try {
            Queue<String> rankQueue = this.queue.get(rank);
            rankQueue.offer(player.getUsername());
            if (weHaveAParty(rank)) {
                playersID = this.getAParty(rank);
            }
        } finally {
            queueLock.unlock();
        }
        if (playersID != null) {
            playerLock.lock();
            Set<Player> players;
            try {
                this.deQueuePlayers(playersID);
                players = playersID.stream().map(p -> this.players.get(p)).collect(Collectors.toSet());
            } finally {
                playerLock.unlock();
            }
            Match m = new Match(players, this);
            Thread t = new Thread(m);
            t.start();
        }
        return "QUEUED-UP";
    }

    private void deQueuePlayers(Set<String> players) {
        players.forEach(p -> this.players.get(p).dequeue());
    }

    private Boolean weHaveAParty(Integer rank) {
        Integer size = queue.get(rank).size();
        Queue<String> above = queue.get(rank+1);
        Queue<String> below = queue.get(rank-1);
        if (above != null) size+= above.size();
        if (below != null) size+=below.size();
        return size >= 10;
    }

    private Set<String> getAParty(Integer rank) {
        Set<String> res = new HashSet<>();
        Queue<String> queueRank = queue.get(rank);
        Queue<String> above = queue.get(rank+1);
        Queue<String> below = queue.get(rank-1);
        getPlayers(res, queueRank);
        getPlayers(res, above);
        getPlayers(res, below);
        return res;
    }

    private void getPlayers(Set<String> res, Queue<String> rank) {
        if (rank != null) {
            while (res.size() < 10 && !rank.isEmpty()) {
                res.add(rank.poll());
            }
            rank.removeAll(res);
        }
    }

    public void removeFromQueue(Player player) {
        queueLock.lock();
        try {
            Integer rank = player.getRank();
            if (rank == 10) rank--;
            queue.get(rank).remove(player.getUsername());
        } finally {
            queueLock.unlock();
        }
        playerLock.lock();
        try {
            player.dequeue();
        } finally {
            playerLock.unlock();
        }
    }
}
