package Game;

import java.util.Set;

public class Match {
    private  Integer id;
    private Set<String> blue;
    private Set<String> red;
    private Boolean winner; //false for blue, true for red

    public Match(Integer id, Set<String> blue, Set<String> red, Boolean winner) {
        this.id = id;
        this.blue = blue;
        this.red = red;
        this.winner = winner;
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

    public Boolean getWinner() {
        return winner;
    }
}
