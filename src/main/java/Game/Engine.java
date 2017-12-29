package Game;

import Exceptions.InvalidAuthenticationException;
import Exceptions.PlayerAlredyExistsException;

import java.util.*;
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
            Integer rank = player.getRank();
            if (player.getRank() == 10) rank--;
            Set<String> players = this.queue.get(rank);
            players.add(player.getUsername());
        } finally {
            gameLock.unlock();
        }
        return player.getRank();
    }

    public Integer doWeHaveAParty(Integer rank) {
        gameLock.lock();
        Integer res = 0; //0 is no, 1 is yes, 2 is yes but with above, 3 is yes but with all 3
        try {
            Set<String> players = this.queue.get(rank);
            if (rank >= 9 && (players.size() >= 10)) res = 1;
            else {
                Set<String> playersAbove = this.queue.get(rank+1);
                if (rank == 0 && (players.size() + playersAbove.size() >= 10)) res = 2;
                else {
                    Set<String> playersBelow = this.queue.get(rank-1);
                    if ((players.size() +  playersAbove.size() + playersBelow.size()) >= 10) res = 3;
                }
            }
        } finally {
            gameLock.unlock();
        }
        return res;
    }

    public Set<String> getTwoTeams(Integer rank, Integer scenario) {
        gameLock.lock();
        Set<String> res = new HashSet<>();
        try {
            Set<String> players = this.queue.get(rank);
            if (scenario.equals(1)) {
                for (String player : players) {
                    if (res.size() == 10) break;
                    res.add(player);
                }
            } else {
                res.addAll(players);
                Set<String> playersAbove = this.queue.get(rank+1);
                if (scenario.equals(2)) {
                    for (String player : playersAbove) {
                        if (res.size() == 10) break;
                        res.add(player);
                    }
                } else {
                    Set<String> playersBelow = this.queue.get(rank-1);
                    res.addAll(playersAbove);
                    for (String player : playersBelow) {
                        if (res.size() == 10) break;
                        res.add(player);
                    }
                    playersBelow.removeAll(res);

                }
                playersAbove.removeAll(res);
            }
        } finally {
            gameLock.unlock();
        }
        return res;
    }

    public void registerMatch(Set<String> blue, Set<String> red, Boolean outcome) {
        playerLock.lock();
        Match m;
        try {
            m = new Match(this.matchLog.size()+1, blue, red, outcome);
            blue.forEach(nick -> this.players.get(nick).setGameOutcome(!outcome));
            red.forEach(nick -> this.players.get(nick).setGameOutcome(outcome));
            this.matchLog.put(m.getId(), m);
        } finally {
            playerLock.unlock();
        }
    }

    public void getMatchOutcome(Set<String> blue, Set<String> red) {
        Random rand = new Random();
        this.registerMatch(blue, red, rand.nextBoolean());
    }
}
