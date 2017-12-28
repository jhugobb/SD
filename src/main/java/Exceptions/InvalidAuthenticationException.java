package Exceptions;

public class InvalidAuthenticationException extends Exception {
    public InvalidAuthenticationException() {
        super();
    }
    public InvalidAuthenticationException(String s) {
        super(s);
    }
}
