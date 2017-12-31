package Game;

import Server.Hub;

import java.io.Serializable;

public class Player implements Serializable{
    private String username;
    private String password;
    private Integer rank;
    private Double wins;
    private Double losses;
    private Boolean isInQueue;
    private Boolean isAuthenticated;
    private Hub hub;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.rank = 5;
        this.wins = 0.0;
        this.losses = 0.0;
        this.isInQueue = false;
        this.isAuthenticated = false;
        this.hub = new Hub();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRank() {
        return rank;
    }

    public Double getWins() {
        return wins;
    }

    public void setGameOutcome(Boolean outcome) { //true in case of win, false otherwise
        if (outcome) {
            this.wins++;

        } else {
            this.losses++;
        }
        Double totalGames = this.wins + this.losses;
        Double ratio = this.wins / this.losses;
        ratio*=10;
        this.rank = ratio.intValue();
    }

    public Double getLosses() {
        return losses;
    }

    public Boolean auth(String password) {
        return this.password.equals(password);
    }

    public void resetHub() {
        this.hub.reset();
    }

    public String readMessage() throws InterruptedException {
        return this.hub.read();
    }

    public void queueUp() {
        this.isInQueue = true;
    }

    public void dequeue() {
        this.isInQueue = false;
    }

    public Boolean isInQueue() {
        return isInQueue;
    }

    public void login() {
        isAuthenticated = true;
    }

    public void logout() {
        isAuthenticated = false;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }
}
