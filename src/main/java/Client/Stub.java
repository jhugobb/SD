package Client;

import java.io.BufferedWriter;
import java.io.IOException;

public class Stub implements Runnable {
    private Menu menu;
    private BufferedWriter output;

    Stub(Menu menu, BufferedWriter out) {
        this.menu = menu;
        this.output = out;
    }

    @Override
    public void run() {
        int choice;
        menu.show();
        while (((choice = getChoice()) != -1)) {
            try {
                parse(choice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Menu getMenu() {
        return menu;
    }


    private int getChoice() {
        int option;
        option = menu.choice();

        return option;
    }

    private void parse (Integer choice) throws IOException {
        switch (menu.getState()) {
            case 0:
                if (choice==1) {
                    login();
                }
                if (choice==2) {
                    signup();
                }
                if (choice==0) {
                    System.exit(0);
                }
                break;
            case 1:
                if (choice==1) {
                    sendMessage("QUEUE");
                }
                if (choice==2) {
                    sendMessage("INFO");
                }
                if (choice==0) {
                    sendMessage("LOGOUT");
                }
                break;
            case 2:
                if (choice==0) {
                    sendMessage("CANCEL");
                }
                break;
            case 3:
                if (choice>-1) chooseChampion(choice);
                break;
            case 4:
                if (choice==0) {
                    menu.setState(1);
                    menu.show();
                }
                break;
            case 5:
                if(choice==0) {
                    menu.setState(1);
                    menu.show();
                }
            break;
        }
    }

    private void sendMessage(String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
    }

    private void login() throws IOException {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        output.write(query);
        output.newLine();
        output.flush();
    }

    private void signup() throws IOException {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "SIGNUP", username, password);

        output.write(query);
        output.newLine();
        output.flush();
    }

    private void chooseChampion(Integer choice) throws IOException {
        String query = String.join(" ", "CHOOSE", choice.toString());
        System.out.println(query);
        output.write(query);
        output.newLine();
        output.flush();
    }
}
