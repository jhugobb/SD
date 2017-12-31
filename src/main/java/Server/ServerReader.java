package Server;

import Exceptions.InvalidAuthenticationException;
import Exceptions.InvalidRequestException;
import Exceptions.PlayerAlredyExistsException;
import Game.Engine;
import Game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReader implements Runnable{

    private Hub hub;
    private BufferedReader in;
    private Boolean running;
    private Player player;
    private Engine engine;

    ServerReader(Hub hub, Socket client, Engine engine) throws IOException {
        this.hub = hub;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.engine = engine;
        this.running = true;
        this.player = null;
    }

    @Override
    public void run() {
        while (running) {
            String response = null;
            try {
                response = in.readLine();
                if ((player != null && !player.isInQueue()) || player == null){
                    hub.write(parseResponse(response));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidRequestException | InvalidAuthenticationException | PlayerAlredyExistsException e) {
                hub.write(e.getMessage());
            }
        }
    }

    public void terminate() {
        this.running = false;
    }

    private String parseResponse(String response) throws InvalidRequestException, InvalidAuthenticationException, PlayerAlredyExistsException {
        String[] info = response.split(" ", 2);
        switch (info[0].toUpperCase()) {
            case "LOGIN":
                requiresAuthentication(false);
                return this.login(info[1]);
            case "SIGNUP":
                requiresAuthentication(false);
                return this.signUp(info[1]);
            case "QUEUE":
                requiresAuthentication(true);
                return this.queueUp();
            case "LOGOUT":
                requiresAuthentication(true);
                return this.logout();
            default: return "OOPS";
        }
    }

    private void requiresAuthentication(boolean status) throws InvalidRequestException {
        if (player == null && status)
            throw new InvalidRequestException("É necessário estar autenticado para executar esse comando!");
        if (player != null && !status)
            throw new InvalidRequestException("Já existe uma sessão iniciada");
    }

    private String login(String input) throws InvalidRequestException, InvalidAuthenticationException {
        String[] info = input.split(" ");
        if (info.length > 2) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        }
        player = engine.login(info[0], info[1]);
        return "OK";
    }

    private String signUp(String input) throws InvalidRequestException, PlayerAlredyExistsException {
        String[] info = input.split(" ");
        if (info.length > 2) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        }
        engine.signUp(info[0], info[1]);
        return "OK";
    }

    private String queueUp() {
        engine.intoQueue(player);
        return "QUEUED-UP";
    }

    private String logout() {
        player.logout();
        this.terminate();
        return "SEEYA";
    }
}
