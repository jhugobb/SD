package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerWriter implements Runnable{

    private Hub hub;
    private BufferedWriter out;

    public ServerWriter(Hub hub, Socket client) throws IOException {
        this.hub = hub;
        this.out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String request = hub.read();
                if(request.equals("LEAVING")) break;
                out.write(request);
                out.newLine();
                out.flush();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
