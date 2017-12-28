package Server;

import Game.Player;

import java.io.BufferedWriter;
import java.io.IOException;

public class Notifier implements Runnable{
    private BufferedWriter writer;
    private Player player;
    private volatile boolean running = true;

    public Notifier(Player player, BufferedWriter writer) {
        this.writer = writer;
        this.player = player;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                String message = player.readMessage();
                writer.write("Message\n" + message + "\n");
            } catch (IOException | InterruptedException e) {
                this.running = false;
            }
        }

    }
}