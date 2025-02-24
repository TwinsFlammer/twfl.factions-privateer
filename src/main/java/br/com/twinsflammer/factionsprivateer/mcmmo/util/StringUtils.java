package br.com.twinsflammer.factionsprivateer.mcmmo.util;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.PartyFeature;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.AbilityType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.EntityType;

import java.text.Normalizer;

public class StringUtils {

    /**
     * Gets a capitalized version of the target string.
     *
     * @param target String to capitalize
     * @return the capitalized string
     */
    public static String getCapitalized(String target) {
        return target.substring(0, 1).toUpperCase() + target.substring(1).toLowerCase();
    }

    public static String cleanPortugueseCharacters(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String getPrettyItemString(Material material) {
        return createPrettyEnumString(material.toString());
    }

    public static String getPrettyEntityTypeString(EntityType entity) {
        return createPrettyEnumString(entity.toString());
    }
    public static String getCustomNameEntityTypeString(EntityType entity) {
        return createPrettyEnumString(getEntityBrazilianName(entity));
    }

    public static String getPrettyAbilityString(AbilityType ability) {
        return createPrettyEnumString(ability.toString());
    }

    public static String getPrettyTreeSpeciesString(TreeSpecies species) {
        return createPrettyEnumString(species.toString());
    }

    public static String getPrettySecondaryAbilityString(SecondaryAbility secondaryAbility) {
        switch (secondaryAbility) {
            case HERBALISM_DOUBLE_DROPS:
            case MINING_DOUBLE_DROPS:
            case WOODCUTTING_DOUBLE_DROPS:
                return "Double Drops";
            case FISHING_TREASURE_HUNTER:
            case EXCAVATION_TREASURE_HUNTER:
                return "Treasure Hunter";
            case GREEN_THUMB_BLOCK:
            case GREEN_THUMB_PLANT:
                return "Green Thumb";
            default:
                return createPrettyEnumString(secondaryAbility.toString());
        }
    }

    public static String getPrettyPartyFeatureString(PartyFeature partyFeature) {
        return createPrettyEnumString(partyFeature.toString());
    }

    private static String createPrettyEnumString(String baseString) {
        String[] substrings = baseString.split("_");
        String prettyString = "";
        int size = 1;

        for (String string : substrings) {
            prettyString = prettyString.concat(getCapitalized(string));

            if (size < substrings.length) {
                prettyString = prettyString.concat(" ");
            }

            size++;
        }

        return prettyString;
    }

    /**
     * Determine if a string represents an Integer
     *
     * @param string String to check
     * @return true if the string is an Integer, false otherwise
     */
    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    /**
     * Determine if a string represents a Double
     *
     * @param string String to check
     * @return true if the string is a Double, false otherwise
     */
    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    private static String getEntityBrazilianName(EntityType entity) {
        switch (entity) {
            case HORSE:
                return "Cavalo";
            case WOLF:
                return "Lobo";
            case OCELOT:
                return "Gato";
            default:
                return entity.toString();
        }
    }

}
