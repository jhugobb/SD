package Server;

import Game.Engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int port = 1337;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        Engine engine = new Engine();

        while(true) {
            Hub hub = new Hub();
            Socket client = server.accept();
            ServerWriter writer = new ServerWriter(hub, client);
            ServerReader reader = new ServerReader(hub, client);
            Thread twrite = new Thread(writer);
            Thread tread = new Thread(reader);
            twrite.start();
            tread.start();
        }
    }
}
