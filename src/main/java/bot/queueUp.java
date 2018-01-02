package bot;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class queueUp {
    private Integer champ;
    private String username;
    private String password;
    private BufferedWriter out;

    public queueUp(Integer champ) {
        this.champ = champ;
        this.username = username + champ;
        this.password = champ.toString();
    }

    public void start(String hostname, Integer porto){

        try {
            Socket socket = new Socket(hostname, porto);
            System.out.println("> Connection accepted!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            send("SINGUP "+username+" "+password);
            while(!in.readLine().equals("SEEYA")) {
            }

            send("LOGIN"+username+" "+password);
            while(!in.readLine().equals("LOGGEDIN")){
            }

            send("QUEUE");
            while(!in.readLine().equals("QUEUED-UP")){
            }
            while(!in.readLine().equals("START")){
            }
            send("CHOOSE "+username+" "+champ);
            String str;
            while(!(str =in.readLine()).equals("FINISH")){
                System.out.println(str);
            }
            send("LOGOUT"+username+" "+password);
            while(!in.readLine().equals("SEEYA")){
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (UnknownHostException e) {
            System.out.println("ERRO: Server doesn't exist!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }

    public static void main(String[] args) {
        queueUp c = new queueUp(Integer.parseInt(args[0]));
        c.start("127.0.0.1",1337);
    }

}
