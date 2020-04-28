package com.redefocus.factionscaribe.mcmmo.skills.swords;

import com.redefocus.factionscaribe.mcmmo.config.AdvancedConfig;

public class Swords {

    public static int bleedMaxTicks = AdvancedConfig.getInstance().getBleedMaxTicks();
    public static int bleedBaseTicks = AdvancedConfig.getInstance().getBleedBaseTicks();

    public static boolean counterAttackRequiresBlock = AdvancedConfig.getInstance().getCounterRequiresBlock();
    public static double counterAttackModifier = AdvancedConfig.getInstance().getCounterModifier();

    public static double serratedStrikesModifier = AdvancedConfig.getInstance().getSerratedStrikesModifier();
    public static int serratedStrikesBleedTicks = AdvancedConfig.getInstance().getSerratedStrikesTicks();
}
