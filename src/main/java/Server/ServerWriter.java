package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerWriter implements Runnable{

    private Hub hub;
    private BufferedWriter out;
    private Boolean running;

    public ServerWriter(Hub hub, Socket client) throws IOException {
        this.hub = hub;
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                String request = hub.read();
                out.write(request);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void terminate() {
        this.running = false;
    }

}
