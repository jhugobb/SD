package Client;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Menu {
    private Scanner input;
    private Integer state; //0 --> not auth, 1--> auth, 2--> auth && in Queue, 3--> Champ choosing, 4--> Stats, 5-->EndScreen
    private String user;
    private String rank;
    private String wins;
    private String losses;
    private String team;
    private LinkedHashMap<String, Integer> choices; //username, champion

    Menu(){
        input = new Scanner(System.in);
        state =0;
        this.user="";
        this.rank="";
        this.wins="";
        this.losses="";
        this.team="";
        this.choices = new LinkedHashMap<>(10);
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public void setLosses(String losses) {
        this.losses = losses;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void addChoice(String s, Integer i) {
        this.choices.put(s,i);
    }

    public Integer getState() {
        return state;
    }

    public void show() {
        switch (state) {
            case 0:
                System.out.println("**************** MENU ***************\n" +
                        "* 1 - Login                         *\n" +
                        "* 2 - Sign up                       *\n" +
                        "* 0 - Exit                          *\n" +
                        "*************************************\n");
                break;
            case 1:
                System.out.println("************** WELCOME **************\n" +
                        "* 1 - Play                          *\n" +
                        "* 2 - Stats                         *\n" +
                        "* 0 - Logout                        *\n" +
                        "*************************************\n");
                break;
            case 2:
                System.out.println("**************** IN Q ***************\n" +
                        "*                                   *\n" +
                        "*             PLEASE WAIT           *\n" +
                        "*              PATIENTLY            *\n" +
                        "*                                   *\n" +
                        "* 0 - Cancel game                   *\n" +
                        "*************************************\n");
                this.choices.clear();
                break;
            case 3:
                showChamps();
                break;
            case 4:
                System.out.println("**************** STATS ***************\n" +
                        "  USERNAME: " + user + "\n" +
                        "  RANK: " + rank + "\n" +
                        "  WINS: " + wins + "\n" +
                        "  LOSSES: " + losses + "\n\n" +
                        "  0 - Exit\n" +
                        "**************************************\n");
                break;
            case 5:
                System.out.println("*************** GAME OVER *************\n" +
                        "  WINNER - "+ team.toUpperCase() + " TEAM!\n\n" +
                        "  0 - Exit\n" +
                        "**************************************\n");
                break;
        }
        System.out.println("Escolha uma opção: ");
    }

    private void showChamps() {
        System.out.println("******************* CHOOSE A HERO *******************************    PLAYERS\n"+
                "* 01 - Luigi               11 - Red           21 - Jack Sparrow *   [BLUE]  " + getChoice(1)+ "\n"+
                "* 02 - Eevee               12 - Mathew Mercer 22 - Darth Vader  *   [BLUE]  " + getChoice(2)+ "\n"+
                "* 03 - Scott Pilgrim       13 - Hacker man    23 - Yoda         *   [BLUE]  "+ getChoice(3)+"\n"+
                "* 04 - Persephone          14 - Naruto        24 - Asmodeous    *   [BLUE]  "+ getChoice(4)+"\n"+
                "* 05 - Link                15 - Pikachu       25 - Batman       *   [BLUE]  "+ getChoice(5)+"\n"+
                "* 06 - Zelda               16 - Megaman       26 - Raven Queen  *   [RED]   "+ getChoice(6)+"\n"+
                "* 07 - Sans                17 - Pacwoman      27 - IT           *   [RED]   "+ getChoice(7)+"\n"+
                "* 08 - Mr T                18 - Gambit        28 - Joker        *   [RED]   "+ getChoice(8)+"\n"+
                "* 09 - R35                 19 - Scanlan       29 - The Flash    *   [RED]   "+ getChoice(9)+"\n"+
                "* 10 - Mario               20 - Papyrus       30 - Sonic        *   [RED]   "+ getChoice(10)+"\n"+
                "*                       00 - RANDOM                             *\n" +
                "*****************************************************************\n");
    }

    public Integer choice() {
        int choice = readChoice();
        if (state==2 || state==5) {
            while(choice!=0) {
                System.out.println("Escolha uma opção: ");
                choice = readChoice();
            }
        }else if (state==0 || state==1){
            while (choice <= -1 || choice >= 3) {
                System.out.println("Escolha uma opção: ");
                choice = readChoice();
            }
        }
        return choice;
    }

    private Integer readChoice() {
        int mode=-1;
        try {
            mode = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            mode =-1;
        }
        return mode;
    }

    public String readString(String question) {
        System.out.println(question);
        return input.nextLine();
    }

    private String getChoice(Integer p) {
        Integer i=1;
        String ret="";
        if (!(choices==null)) {
            for (String s : choices.keySet()) {
                if (i == p) {
                    ret = s + ": ";
                    if (choices.get(s) != -1) ret += choices.get(s).toString();
                    break;
                }
                i++;
            }
        }
        return ret;
    }
}
