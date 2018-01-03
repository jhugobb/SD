package Server.Exceptions;

public class PlayerAlredyExistsException extends Exception {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    public PlayerAlredyExistsException() {
        super();
    }
    public PlayerAlredyExistsException(String s) {
        super(RED+s+RESET);
    }
}
