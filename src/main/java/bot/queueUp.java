package bot;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class queueUp implements Runnable{
    private Integer champ;
    private String username;
    private String password;
    private BufferedWriter out;

    public queueUp(Integer username, Integer champ) {
        this.champ = champ;
        this.username = username.toString();
        this.password = champ.toString();
    }

    public void start(String hostname, Integer porto){

        try {
            Socket socket = new Socket(hostname, porto);
            System.out.println("> Connection accepted!");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String str=null;
            send("SIGNUP "+username+" "+password);
            while((str = in.readLine()) != null && !str.equals("SIGNEDUP") && !str.equals("O nome de utilizador não está disponível")) {
                System.out.println(str);
            }

            send("LOGIN "+username+" "+password);
            while((str = in.readLine()) != null && !str.equals("LOGGEDIN")){
                System.out.println(str);
            }

            send("QUEUE");

            while((str = in.readLine())!= null && !str.equals("START")){
                System.out.println(str);
            }

            send("CHOOSE "+ champ);
            while((str = in.readLine())!= null && (!str.equals("FINISH") && !str.equals("DODGE"))){
                System.out.println(str);
            }
            System.out.println(str +" "+ username);
            send("INFO");
            System.out.println(in.readLine());
            send("LOGOUT");
            while(!in.readLine().equals("SEEYA")){
            }

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

    @Override
    public void run() {
        this.start("127.0.0.1",1337);
    }
}

