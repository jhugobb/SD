package Server.Exceptions;

public class InvalidRequestException extends Exception {
    public InvalidRequestException() {
        super();
    }
    public InvalidRequestException(String s) {
        super(s);
    }
}
