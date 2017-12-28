package Server;

import java.io.BufferedWriter;
import java.io.IOException;

public class ServerWriter {
    private BufferedWriter out;

    public ServerWriter(BufferedWriter out) {
        this.out = out;
    }

    public void printToScreen(String command) throws IOException {
        this.out.write(command);
    }

}
