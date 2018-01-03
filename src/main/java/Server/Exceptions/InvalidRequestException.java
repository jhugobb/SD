package Server.Exceptions;

public class InvalidRequestException extends Exception {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    public InvalidRequestException() {
        super();
    }
    public InvalidRequestException(String s) {
        super(RED+s+RESET);
    }
}
