package Client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader implements Runnable{
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private BufferedReader input;
    private Menu menu;

    ClientReader(BufferedReader input, Menu menu) {
        this.input = input;
        this.menu = menu;
    }

    @Override
    public void run() {
        String str;
        try {
            while ((str = input.readLine()) != null) {
                parseResponse(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void parseResponse(String response){
        String[] info = response.split(" ", 2);
        switch (info[0].toUpperCase()) {
            case("DENIED"):
                System.out.println(RED +"User not authenticated" + RESET);
                menu.show();
                break;
            case ("LOGGEDIN"):
                menu.setState(1);
                menu.show();
                break;
            case("SIGNEDUP"):
                menu.setState(0);
                menu.show();
                break;
            case("TAKEN"):
                System.out.println(RED + "Champion already picked" + RESET);
                menu.show();
                break;
            case("INVALID"):
                System.out.println(RED +"Invalid request"+ RESET);
                menu.show();
                break;
            case("BYEQUEUE"):
                menu.setState(1);
                menu.show();
                break;
            case("DODGE"):
                if (menu.getState()==3) {
                    System.out.println(RED + "TIME OUT - You forgot to pick your Hero!" + RESET);
                    menu.setState(1);
                    menu.show();
                }
                break;
            case("DOUBLE"):
                System.out.println(RED + "This user is already authenticated elsewhere" + RESET);
                menu.show();
                break;
            case("INFO"):
                stats(info[1]);
                break;
            case("QUEUED-UP"):
                if (menu.getState()==3) {
                    System.out.println(YELLOW +"Match restarted because some players didn't choose their champions" + RESET);
                }
                menu.setState(2);
                menu.show();
                break;
            case("START"):
                menu.setState(3);
                menu.show();
                break;
            case("CHAMPS"):
                champs(info[1]);
                break;
            case ("RED"):
                menu.setTeam("RED");
                break;
            case ("BLUE"):
                menu.setTeam("BLUE");
                break;
            case("FINISH"):
                menu.setState(5);
                menu.show();
                break;
            case("SEEYA"):
                menu.setState(0);
                menu.show();
                break;
            default:
                System.out.println(response);
                menu.show();
        }
    }

    private void stats(String info) {
        String[] stats = info.split(" ");
        String user = stats[0];
        String rank = stats[1];
        String wins = stats[2];
        String losses = stats[3];

        menu.setUser(user);
        menu.setRank(rank);
        menu.setWins(wins);
        menu.setLosses(losses);
        menu.setState(4);
        menu.show();
    }

    private void champs(String champs) {
        String[] info = champs.split(";");
        String[] user;
        for (String s : info) {
            user = s.split(" ");
            try {
                menu.addChoice(user[0], Integer.parseInt(user[1]));
            } catch (NumberFormatException e) {
                System.out.println("NOT A NUMBER. I RECEIVED " + s);
            }
        }
        menu.show();
    }

}
