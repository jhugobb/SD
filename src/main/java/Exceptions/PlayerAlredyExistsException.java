package Exceptions;

public class PlayerAlredyExistsException extends Exception {
    public PlayerAlredyExistsException() {
        super();
    }
    public PlayerAlredyExistsException(String s) {
        super(s);
    }
}
