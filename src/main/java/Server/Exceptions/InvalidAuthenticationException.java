package Server.Exceptions;

public class InvalidAuthenticationException extends Exception {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    public InvalidAuthenticationException() {
        super();
    }
    public InvalidAuthenticationException(String s) {
        super(RED+s+RESET);
    }
}
