package Client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientWriter implements Runnable {

    private Socket cliSocket;
    private BufferedWriter clientOut;
    private Boolean running;

    public ClientWriter(Socket cliSocket, BufferedWriter clientOut) {
        this.cliSocket = cliSocket;
        this.clientOut = clientOut;
        this.running = true;
    }


    @Override
    public void run() {
        while (running) {
            String response = this.receiveString();
            try {
                clientOut.write(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String receiveString() {
        return "";
    }

    public void terminate() {
        running = false;
    }
}

