package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReader {

    private BufferedReader in;

    public ServerReader(Socket client) throws IOException {
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }
    public String readLine() {
        String l = null;
        try {
            l = in.readLine();
        } catch (IOException e) {
            return null;
        }
        return l;
    }
}
