package Server.Game;

import Server.Hub;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Serializable{
    private String username;
    private String password;
    private Integer rank;
    private Double wins;
    private Double losses;
    private Boolean isInQueue;
    private Boolean isAuthenticated;
    private Boolean isPlaying;
    private Hub playerHub;
    private Hub gameHub;
    private Lock playerLock;

    Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.rank = 0;
        this.wins = 0.0;
        this.losses = 0.0;
        this.isInQueue = false;
        this.isAuthenticated = false;
        this.isPlaying = false;
        this.playerHub = null;
        this.gameHub = null;
        this.playerLock = new ReentrantLock();
    }

    public String getUsername() {
        return username;
    }

    public Integer getRank() {
        return rank;
    }

    public void setGameOutcome(Boolean outcome) { //true in case of win, false otherwise
        if (outcome) {
            this.wins++;

        } else {
            this.losses++;
        }
        Double totalGames = this.wins + this.losses;
        Double ratio = this.wins / totalGames;
        ratio*=10;
        this.rank = ratio.intValue();
    }

    public Boolean auth(String password) {
        return this.password.equals(password);
    }

    public Hub getPlayerHub() {
        return playerHub;
    }

    public void setPlayerHub(Hub playerHub) {
        this.playerHub = playerHub;
    }

    public void resetPlayerHub() {
        this.playerHub.reset();
    }

    public void setGameHub(Hub gameHub) {
        this.gameHub = gameHub;
    }

    public void queueUp() {
        playerLock.lock();
        try {
            this.isInQueue = true;
        } finally {
            playerLock.unlock();
        }
    }

    public void dequeue() {
        playerLock.lock();
        try {
            this.isInQueue = false;
        } finally {
            playerLock.unlock();
        }
    }

    public Boolean isInQueue() {
        playerLock.lock();
        try {
            return isInQueue;
        }finally {
            playerLock.unlock();
        }
    }

    public void login() {
        isAuthenticated = true;
    }

    public void logout() {
        isAuthenticated = false;
        resetPlayerHub();
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setPlaying(Boolean status) {
        playerLock.lock();
        try {
            this.isPlaying = status;
        } finally {
            playerLock.unlock();
        }
    }

    public Boolean getIsPlaying() {
        playerLock.lock();
        try {
            return isPlaying;
        } finally {
            playerLock.unlock();
        }
    }

    public Hub getGameHub() {
        return gameHub;
    }

    @Override
    public String toString() {
        return username + " " + rank.toString() + " " + wins.toString() + " " + losses.toString();
    }
}
