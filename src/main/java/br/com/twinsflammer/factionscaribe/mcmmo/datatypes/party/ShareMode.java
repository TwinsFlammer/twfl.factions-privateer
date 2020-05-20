package br.com.twinsflammer.factionscaribe.mcmmo.datatypes.party;

import br.com.twinsflammer.factionscaribe.mcmmo.util.commands.CommandUtils;

public enum ShareMode {

    NONE,
    EQUAL,
    RANDOM;

    public static ShareMode getShareMode(String string) {
        try {
            return valueOf(string);
        } catch (IllegalArgumentException ex) {
            if (string.equalsIgnoreCase("even")) {
                return EQUAL;
            } else if (CommandUtils.shouldDisableToggle(string)) {
                return NONE;
            }

            return null;
        }
    }
}
