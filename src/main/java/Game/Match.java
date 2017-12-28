package Game;

import java.util.Set;

public class Match {
    private  Integer id;
    private Set<Player> blue;
    private Set<Player> red;
    private Integer winner;

    public Match(Integer id, Set<Player> blue, Set<Player> red, Integer winner) {
        this.id = id;
        this.blue = blue;
        this.red = red;
        this.winner = winner;
    }

    public Integer getId() {
        return id;
    }

    public Set<Player> getBlue() {
        return blue;
    }

    public Set<Player> getRed() {
        return red;
    }

    public Integer getWinner() {
        return winner;
    }
}
