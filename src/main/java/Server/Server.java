package Server;

import Game.Engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.locks.Condition;

public class Server {
    private static final int port = 1337;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        Engine engine = new Engine();

        while(true) {
            Socket client = server.accept();
            Barman cli = new Barman(engine, client);
            Thread barman = new Thread(cli);
            barman.start();
        }
    }
}
