package br.com.twinsflammer.factionscaribe.mcmmo.api.exceptions;

public class InvalidPlayerException extends RuntimeException {

    private static final long serialVersionUID = 907213002618581385L;

    public InvalidPlayerException() {
        super("That player does not exist in the database.");
    }
}
