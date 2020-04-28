package com.redefocus.factionscaribe.mcmmo.locale;

import com.redefocus.factionscaribe.mcmmo.config.Config;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.ChatColor;

public final class LocaleLoader {

    private static final String BUNDLE_ROOT = "br.com.titanwar.server.locale.locale";
    private static ResourceBundle bundle = null;
    private static ResourceBundle enBundle = null;

    private LocaleLoader() {
    }

    ;

    public static String getString(String key) {
        return getString(key, (Object[]) null);
    }

    /**
     * Gets the appropriate string from the Locale files.
     *
     * @param key The key to look up the string with
     * @param messageArguments Any arguments to be added to the string
     * @return The properly formatted locale string
     */
    public static String getString(String key, Object... messageArguments) {
        if (bundle == null) {
            initialize();
        }

        try {
            return getString(key, bundle, messageArguments);
        } catch (MissingResourceException ex) {
            try {
                return getString(key, enBundle, messageArguments);
            } catch (MissingResourceException ex2) {
                if (!key.contains("Guides")) {
                    FactionsCaribe.getInstance().getLogger().warning("Could not find locale string: " + key);
                }

                return '!' + key + '!';
            }
        }
    }

    private static String getString(String key, ResourceBundle bundle, Object... messageArguments) throws MissingResourceException {
        String value = bundle.getString(key);
//        if (value == null) {
        return formatString(value, messageArguments);
//        } else {
//            try {
//                return formatString(new String(value.getBytes("ISO-8859-1"), "UTF-8"), messageArguments);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                return formatString(null, messageArguments);
//            }
//        }
    }

    public static String formatString(String string, Object... messageArguments) {
        if (messageArguments != null) {
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(string.replace("'", "''"));
            string = formatter.format(messageArguments);
        }

        string = addColors(string);

        return string;
    }

    public static Locale getCurrentLocale() {
        if (bundle == null) {
            initialize();
        }
        return bundle.getLocale();
    }

    private static void initialize() {
        if (bundle == null) {
            Locale.setDefault(new Locale("pt", "BR"));
            Locale locale = null;
            String[] myLocale = Config.getInstance().getLocale().split("[-_ ]");

            if (myLocale.length == 1) {
                locale = new Locale(myLocale[0]);
            } else if (myLocale.length >= 2) {
                locale = new Locale(myLocale[0], myLocale[1]);
            }

            bundle = ResourceBundle.getBundle("locale/locale_pt_BR", locale);
            enBundle = ResourceBundle.getBundle("locale/locale_pt_BR", locale);
        }
    }

    private static String addColors(String input) {
        input = input.replaceAll("\\Q[[BLACK]]\\E", ChatColor.BLACK.toString());
        input = input.replaceAll("\\Q[[DARK_BLUE]]\\E", ChatColor.DARK_BLUE.toString());
        input = input.replaceAll("\\Q[[DARK_GREEN]]\\E", ChatColor.DARK_GREEN.toString());
        input = input.replaceAll("\\Q[[DARK_AQUA]]\\E", ChatColor.DARK_AQUA.toString());
        input = input.replaceAll("\\Q[[DARK_RED]]\\E", ChatColor.DARK_RED.toString());
        input = input.replaceAll("\\Q[[DARK_PURPLE]]\\E", ChatColor.DARK_PURPLE.toString());
        input = input.replaceAll("\\Q[[GOLD]]\\E", ChatColor.GOLD.toString());
        input = input.replaceAll("\\Q[[GRAY]]\\E", ChatColor.GRAY.toString());
        input = input.replaceAll("\\Q[[DARK_GRAY]]\\E", ChatColor.DARK_GRAY.toString());
        input = input.replaceAll("\\Q[[BLUE]]\\E", ChatColor.BLUE.toString());
        input = input.replaceAll("\\Q[[GREEN]]\\E", ChatColor.GREEN.toString());
        input = input.replaceAll("\\Q[[AQUA]]\\E", ChatColor.AQUA.toString());
        input = input.replaceAll("\\Q[[RED]]\\E", ChatColor.RED.toString());
        input = input.replaceAll("\\Q[[LIGHT_PURPLE]]\\E", ChatColor.LIGHT_PURPLE.toString());
        input = input.replaceAll("\\Q[[YELLOW]]\\E", ChatColor.YELLOW.toString());
        input = input.replaceAll("\\Q[[WHITE]]\\E", ChatColor.WHITE.toString());
        input = input.replaceAll("\\Q[[BOLD]]\\E", ChatColor.BOLD.toString());
        input = input.replaceAll("\\Q[[UNDERLINE]]\\E", ChatColor.UNDERLINE.toString());
        input = input.replaceAll("\\Q[[ITALIC]]\\E", ChatColor.ITALIC.toString());
        input = input.replaceAll("\\Q[[STRIKE]]\\E", ChatColor.STRIKETHROUGH.toString());
        input = input.replaceAll("\\Q[[MAGIC]]\\E", ChatColor.MAGIC.toString());
        input = input.replaceAll("\\Q[[RESET]]\\E", ChatColor.RESET.toString());

        return input;
    }
}
