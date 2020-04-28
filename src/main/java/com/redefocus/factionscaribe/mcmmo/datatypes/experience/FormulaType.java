package com.redefocus.factionscaribe.mcmmo.datatypes.experience;

public enum FormulaType {

    LINEAR,
    EXPONENTIAL,
    UNKNOWN;

    public static FormulaType getFormulaType(String string) {
        try {
            return valueOf(string);
        } catch (IllegalArgumentException ex) {
            return UNKNOWN;
        }
    }
}
