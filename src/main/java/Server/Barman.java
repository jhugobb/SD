package Server;

import Exceptions.ChampionAlreadyPickedException;
import Exceptions.ChampionAlredyPickedException;
import Exceptions.InvalidAuthenticationException;
import Exceptions.InvalidRequestException;
import Exceptions.PlayerAlredyExistsException;
import Game.Engine;
import Game.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class Barman implements Runnable {
    private Engine engine;
    private Player player;
    private Socket clientSocket;
    private ServerReader in;
    private BufferedWriter out;
    private Notifier notifier;

    public Barman(Engine engine, Socket client) {
        this.engine = engine;
        this.clientSocket = client;
        player = null;
        notifier = null;
    }

    @Override
    public void run() {
        String input = null;
        String command = null;
        while ((input = in.readLine()) != null) {
            command = this.parseInput(input);
            try {
                if (!command.isEmpty()) {
                    out.write(command);
                } else {
                    out.write("Unknown Command!");
                }
            } catch (IOException e) {
                this.end();
            }
        }
        this.end();
    }

    private String parseInput(String input) {
        //  TODO
        return "";
    }

    private String translate(String input) throws InvalidRequestException {
        String[] cmds = input.split(" ", 2);
        switch (cmds[0].toUpperCase()) {
            case "REGISTAR":
                requiresLogin(false);
                return this.signUp(cmds[1]);
            case "LOGIN":
                requiresLogin(false);
                return this.login(cmds[1]);
            case "JOGAR":
                requiresLogin(true);
                return this.play();
            case "ACEITAR":
                requiresLogin(true);
                return this.accept();
            case "CHOOSE":
                requiresLogin(true);
                return this.choose(cmds[1]);
            case "INFO":
                requiresLogin(true);
                return this.info();
            case "LOGOUT":
                requiresLogin(true);
                return this.logout();
            default: throw new InvalidRequestException();

        }
    }

    private String signUp(String cmd) throws InvalidRequestException {
        String[] info = cmd.split(" ");
        try {
            if (info.length > 2){
                throw new InvalidRequestException("O nickname e a password não podem ter espaços");
            }
            engine.signUp(info[0], info[1]);
        } catch (PlayerAlredyExistsException e) {
            throw new InvalidRequestException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        }
        return "OK";
    }

    private String login(String cmd) throws InvalidRequestException {
        String[] info = cmd.split(" ");
        try {
            if (info.length > 2){
                throw new InvalidRequestException("O dados não são válidos");

            }
            this.player = engine.login(info[0], info[1]);
            player.setSession(clientSocket);
            this.notifier = new Notifier(player, out);
            Thread noti = new Thread(this.notifier);
            noti.start();
        } catch (InvalidAuthenticationException e) {
            throw new InvalidRequestException(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        } catch (IOException e) {
            throw new InvalidRequestException("Algo impediu o início de sessão");
        }
        return "OK";
    }

    private String play() {
        return engine.intoQueue(player);
    }

    private String accept() {
        // TODO
        // player.currentQueue = engine.accept(player);
        return "";
    }

    private String choose(String cmd) throws InvalidRequestException {
        String[] info = cmd.split(" ");
        try {
            if (info.length > 1) {
                throw new InvalidRequestException("Só se pode escolher 1 Champion!");
            }
            //TODO
            engine.pick(player.getCurrentQueue(), player.getUsername(), info[1]);
        } catch (ChampionAlreadyPickedException e) {
            throw new InvalidRequestException("Só se pode escolher 1 Champion!");
        }

        return "CHAMPIONPICKED";
    }

    private String info() throws InvalidRequestException {
        // TODO
        return player.toString();
    }

    private String logout() throws InvalidRequestException {
        this.end();
        return "BYE";
    }

    private void requiresLogin(boolean status) throws InvalidRequestException {
        if (status && player == null)
            throw new InvalidRequestException("É necessário iniciar sessão para jogar!");

        if (!status && player != null)
            throw new InvalidRequestException("Já existe uma sessão iniciada!");
    }

    private void end() {
        if (this.notifier != null)
            this.notifier.terminate();

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Couldn't close client socket... Client won't care");
        }
    }
}
