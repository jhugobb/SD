package Game;

import Server.Hub;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Match implements Runnable{
    private Integer id;
    private Set<Player> blue;
    private Set<Player> red;
    private Set<Hub> playersHub;
    private Hub gameHub;
    private Boolean winner;
    private Boolean running;
    private Map<String,Integer> champs;
    private Thread timer;

    public Match(Integer id, Set<Player> players) {
        this.id = id;
        this.blue = new HashSet<>();
        this.red = new HashSet<>();
        this.gameHub = new Hub();
        this.playersHub = new HashSet<>();
        this.champs = new TreeMap<>();
        int i = 0;
        for(Player player : players){
            if (i<5){
                blue.add(player);

            }else red.add(player);
            playersHub.add(player.getPlayerHub());
            player.setGameHub(gameHub);
            champs.put(player.getUsername(),0);
            i++;
        }
        this.winner = null;
        this.running = true;
        timer = new Thread(new Timer(gameHub));
    }

    public void terminate() {
        running = false;
    }

    public void getWinner(){
        Random rand = new Random();
        winner = rand.nextBoolean();
        blue.forEach(p -> p.setGameOutcome(winner));
        red.forEach(p -> p.setGameOutcome(!winner));
    }

    public boolean isChoose(String user, Integer champ){
       return champs.values().stream().anyMatch(c -> c.equals(champ));
    }

    public String answer(String user, Integer champ){
        champs.put(user,champ);
        StringBuilder ans = new StringBuilder();
        ans.append("CHAMPS ");
        champs.forEach((k,v) -> ans.append(","+k+" "+ v+" "));
        return  ans.toString();
    }

    public void champSelect(){
        timer.start();
        while(gameHub.isValid()){
            try {
                String msg = gameHub.read();
                String[] info = msg.split(" ", 3);
                if(info[0].equals("CHOOSE")){
                    if(!isChoose(info[1],Integer.parseInt(info[2]))) {
                        String answer = this.answer(info[1], Integer.parseInt(info[2]));
                        playersHub.forEach(p -> p.write(answer));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        champSelect();
        getWinner();
        terminate();
    }

}
