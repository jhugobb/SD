package Exceptions;

public class ChampionAlreadyPickedException extends Exception{
    public ChampionAlreadyPickedException() {
        super();
    }

    public ChampionAlreadyPickedException(String message) {
        super(message);
    }
}
