package Server;

import Game.Engine;
import Game.Player;

import java.io.IOException;
import java.net.Socket;

public class Barman implements Runnable {
    private Engine engine;
    private Player player;
    private Socket clientSocket;
    private ServerReader in;
    private ServerWriter out;
    private Notifier notifier;

    public Barman(Engine engine, Socket client) {
        this.engine = engine;
        this.clientSocket = client;
        player = null;
        notifier = null;
    }

    @Override
    public void run() {
        String input = null;
        String command = null;
        while ((input = in.readLine()) != null) {
            command = this.parseInput(input);
            try {
                if (!command.isEmpty()) {
                    out.printToScreen(command);
                } else {
                    out.printToScreen("Unknown Command!");
                }
            } catch (IOException e) {
                this.end();
            }
        }
        this.end();
    }

    private String parseInput(String input) {
        //  TODO
        return "";
    }

    private void end() {
        if (this.notifier != null)
            this.notifier.terminate();

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Couldn't close client socket... Client won't care");
        }
    }
}
