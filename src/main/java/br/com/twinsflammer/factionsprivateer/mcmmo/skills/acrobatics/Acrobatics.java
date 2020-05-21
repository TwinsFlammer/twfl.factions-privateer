package br.com.twinsflammer.factionsprivateer.mcmmo.skills.acrobatics;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.AdvancedConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.experience.ExperienceConfig;

public final class Acrobatics {

    public static double rollThreshold = AdvancedConfig.getInstance().getRollDamageThreshold();
    public static double gracefulRollThreshold = AdvancedConfig.getInstance().getGracefulRollDamageThreshold();
    public static double dodgeDamageModifier = AdvancedConfig.getInstance().getDodgeDamageModifier();

    public static int dodgeXpModifier = ExperienceConfig.getInstance().getDodgeXPModifier();
    public static int rollXpModifier = ExperienceConfig.getInstance().getRollXPModifier();
    public static int fallXpModifier = ExperienceConfig.getInstance().getFallXPModifier();

    public static double featherFallXPModifier = ExperienceConfig.getInstance().getFeatherFallXPModifier();

    public static boolean dodgeLightningDisabled = Config.getInstance().getDodgeLightningDisabled();

    private Acrobatics() {
    }

    ;

    protected static double calculateModifiedDodgeDamage(double damage, double damageModifier) {
        return Math.max(damage / damageModifier, 1.0);
    }

    protected static double calculateModifiedRollDamage(double damage, double damageThreshold) {
        return Math.max(damage - damageThreshold, 0.0);
    }
}
