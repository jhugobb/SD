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
    private Socket client;

    ServerReader(Hub hub, Socket client, Engine engine) throws IOException {
        this.hub = hub;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.engine = engine;
        this.running = true;
        this.player = null;
        this.client = client;
    }

    @Override
    public void run() {
        String response=null;
        while ( (response = readLine()) !=null ) {
            try {
                if (player == null || (player != null && !player.getIsPlaying()))
                    hub.write(parseResponse(response));
                    else player.getGameHub().write(parseResponse(response));
            } catch (IndexOutOfBoundsException e) {
                hub.write("INVALID");
            } catch (InvalidRequestException | InvalidAuthenticationException | PlayerAlredyExistsException e) {
                hub.write(e.getMessage());
            }
        }
        hub.write("LEAVING");
        try {
            client.shutdownInput();
            client.shutdownOutput();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String parseResponse(String response) throws InvalidRequestException, InvalidAuthenticationException, PlayerAlredyExistsException {
        String[] info = response.split(" ", 2);
        switch (info[0].toUpperCase()) {
            case "LOGIN":
                requiresAuthentication(false);
                requiresInQueue(false);
                requiresInGame(false);
                return this.login(info[1]);
            case "SIGNUP":
                requiresAuthentication(false);
                requiresInQueue(false);
                requiresInGame(false);
                return this.signUp(info[1]);
            case "INFO":
                requiresAuthentication(true);
                requiresInQueue(false);
                requiresInGame(false);
                return "INFO " + this.player.toString();
            case "QUEUE":
                requiresAuthentication(true);
                requiresInQueue(false);
                requiresInGame(false);
                return this.queueUp();
            case "CANCEL":
                requiresAuthentication(true);
                requiresInQueue(true);
                requiresInGame(false);
                return this.cancelQueue();
            case "CHOOSE":
                requiresAuthentication(true);
                requiresInQueue(false);
                requiresInGame(true);
                return this.chooseHero(info[1]);
            case "LOGOUT":
                requiresAuthentication(true);
                requiresInQueue(false);
                requiresInGame(false);
                return this.logout();
            default: return "OOPS";
        }
    }

    private String cancelQueue() {
        engine.removeFromQueue(player);
        return "BYEQUEUE";
    }

    private String chooseHero(String input) throws InvalidRequestException {
        String[] info = input.split(" ");
        if (info.length != 1) throw new InvalidRequestException("INVALID");
        return "CHOOSE " + info[0] + " " + player.getUsername();
    }

    private void requiresAuthentication(Boolean shouldBe) throws InvalidRequestException {
        if (player == null && shouldBe)
            throw new InvalidRequestException("DENIED");
        if (player != null && !shouldBe)
            throw new InvalidRequestException("DOUBLE");
    }

    private void requiresInQueue(Boolean shouldBe) throws InvalidRequestException {
        if (!shouldBe && player != null && player.isInQueue())
            throw new InvalidRequestException("INQUEUE");
        if (shouldBe && player != null && !player.isInQueue())
            throw new InvalidRequestException("NOTINQUEUE");
    }

    private void requiresInGame(Boolean shouldBe) throws InvalidRequestException {
        if (shouldBe && player != null && !player.getIsPlaying())
            throw new InvalidRequestException("NOTINGAME");
        if (!shouldBe && player != null && player.getIsPlaying())
            throw new InvalidRequestException("INGAME");
    }

    private String login(String input) throws InvalidRequestException, InvalidAuthenticationException {
        String[] info = input.split(" ");
        if (info.length != 2) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        }
        player = engine.login(info[0], info[1],hub);
        return "LOGGEDIN";
    }

    private String signUp(String input) throws InvalidRequestException, PlayerAlredyExistsException {
        String[] info = input.split(" ");
        if (info.length != 2) {
            throw new InvalidRequestException("Os dados inseridos não são válidos");
        }
        engine.signUp(info[0], info[1]);
        return "SIGNEDUP";
    }

    private String queueUp() {
        engine.intoQueue(player);
        return "QUEUED-UP";
    }

    private String logout() {
        player.logout();
        if (player.isInQueue()) engine.removeFromQueue(player);
        player = null;
        return "SEEYA";
    }

    private String readLine() {
        String line = null;

        try {
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }
}
