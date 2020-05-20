package br.com.twinsflammer.factionscaribe.mcmmo.api.exceptions;

public class InvalidSkillException extends RuntimeException {

    private static final long serialVersionUID = 942705284195791157L;

    public InvalidSkillException() {
        super("That is not a valid skill.");
    }
}
