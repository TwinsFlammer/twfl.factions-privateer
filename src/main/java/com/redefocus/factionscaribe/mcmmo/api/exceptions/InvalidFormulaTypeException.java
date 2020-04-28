package com.redefocus.factionscaribe.mcmmo.api.exceptions;

public class InvalidFormulaTypeException extends RuntimeException {

    private static final long serialVersionUID = 3368670229490121886L;

    public InvalidFormulaTypeException() {
        super("That is not a valid FormulaType.");
    }
}
