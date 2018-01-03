package Game;

import Server.Hub;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Match implements Runnable{
    private Integer id;
    private Engine engine;
    private Set<Player> blue;
    private Set<Player> red;
    private HashMap<String, Hub> playersHub;
    private Hub gameHub;
    private Boolean winner;
    private Boolean running;
    private Map<String,Integer> champs;
    private Thread timer;

    Match( Set<Player> players, Engine engine) {
        this.engine = engine;
        this.blue = new HashSet<>();
        this.red = new HashSet<>();
        this.gameHub = new Hub();
        this.playersHub = new HashMap<>();
        this.champs = new TreeMap<>();
        int i = 0;
        for(Player player : players){
            if (i<5){
                blue.add(player);

            }else red.add(player);
            playersHub.put(player.getUsername(), player.getPlayerHub());
            player.setGameHub(gameHub);
            champs.put(player.getUsername(),-1);
            i++;
        }
        this.winner = null;
        this.running = true;
        timer = new Thread(new Timer(gameHub));
    }

    private void terminate() {
        running = false;
    }

    private void getWinner(){
        Random rand = new Random();
        winner = rand.nextBoolean();
        String winningTeam = winner ? "BLUE" : "RED";
        playersHub.values().forEach(hub -> hub.write(winningTeam));
        blue.forEach(p -> p.setGameOutcome(winner));
        red.forEach(p -> p.setGameOutcome(!winner));
    }

    private boolean isChosen(Integer champ){
       return champs.values().stream().anyMatch(c -> c.equals(champ));
    }

    private String answer(String user, Integer champ){
        champs.put(user,champ);
        StringBuilder ans = new StringBuilder();
        ans.append("CHAMPS ");
        blue.forEach(p -> ans.append(p.getUsername()+" "+champs.get(p.getUsername())+";"));
        red.forEach(p -> ans.append(p.getUsername()+" "+champs.get(p.getUsername())+";"));
        return  ans.toString();
    }

    private Integer randomChampion(){
        List<Integer> range = IntStream.rangeClosed(1, 30).boxed().collect(Collectors.toList());
        range.removeIf(c -> champs.containsValue(c));
        return range.get(new Random().nextInt(range.size()));
    }

    private void champSelect() {
        timer.start();
        while(gameHub.isValid()){
            try {
                String msg = gameHub.read();
                String[] info = msg.split(" ");
                if (info[0].equals("CHOOSE")) {
                    Integer hero = Integer.parseInt(info[1]);
                    if (hero < 0 || hero > 30) playersHub.get(info[2]).write("INVALID");
                    else if (!isChosen(hero)) {
                        if(hero.equals(0)) hero = randomChampion();
                        String answer = this.answer(info[2], hero);
                        playersHub.values().forEach(p -> p.write(answer));
                    } else {
                        playersHub.get(info[2]).write("TAKEN");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void notifyClients(Boolean status) {
        blue.forEach(p -> p.setPlaying(status));
        red.forEach(p -> p.setPlaying(status));
        String message;
        if (status) message = "START";
        else message = "FINISH";
        playersHub.values().forEach(hub -> hub.write(message));
    }

    private void validateMatch(){
        if (isChosen(-1)){
            handleDodge(blue);
            handleDodge(red);
            playersHub.values().forEach(hub -> hub.write("DODGE"));
        } else {
            getWinner();
            notifyClients(false);
        }
    }

    private void handleDodge(Set<Player> team) {
        team.forEach(p -> {
            p.setPlaying(false);
            if (champs.get(p.getUsername()) == -1)
                p.setGameOutcome(false);
        });
    }

    @Override
    public void run() {
        notifyClients(true);
        champSelect();
        validateMatch();
        terminate();
    }
}
