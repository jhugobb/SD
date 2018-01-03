package Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private static final int port = 1337;

    public static void main(String[] args){

        Socket s = null;

        try{
            s = new Socket("localhost",port);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            Menu menu = new Menu();
            Stub stub = new Stub(menu,bw);
            ClientReader cr = new ClientReader(br, menu);

            Thread reader = new Thread(cr);
            Thread stubs = new Thread(stub);

            reader.start();
            stubs.start();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
