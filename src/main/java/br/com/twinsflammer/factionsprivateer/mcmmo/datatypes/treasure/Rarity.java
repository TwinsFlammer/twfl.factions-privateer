package br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.treasure;

public enum Rarity {

    RECORD,
    LEGENDARY,
    EPIC,
    RARE,
    UNCOMMON,
    COMMON,
    TRAP;

    public static Rarity getRarity(String string) {
        try {
            return valueOf(string);
        } catch (IllegalArgumentException ex) {
            return COMMON;
        }
    }
}
