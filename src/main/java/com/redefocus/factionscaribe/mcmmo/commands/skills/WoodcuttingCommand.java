package com.redefocus.factionscaribe.mcmmo.commands.skills;

import com.redefocus.factionscaribe.mcmmo.config.AdvancedConfig;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SecondaryAbility;
import com.redefocus.factionscaribe.mcmmo.datatypes.skills.SkillType;
import com.redefocus.factionscaribe.mcmmo.locale.LocaleLoader;
import com.redefocus.factionscaribe.mcmmo.util.Permissions;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class WoodcuttingCommand extends SkillCommand {

    private String treeFellerLength;
    private String treeFellerLengthEndurance;
    private String doubleDropChance;
    private String doubleDropChanceLucky;

    private boolean canTreeFell;
    private boolean canLeafBlow;
    private boolean canDoubleDrop;

    public WoodcuttingCommand() {
        super(SkillType.WOODCUTTING);
    }

    @Override
    protected void dataCalculations(Player player, float skillValue, boolean isLucky) {
        // TREE FELLER
        if (canTreeFell) {
            String[] treeFellerStrings = calculateLengthDisplayValues(player, skillValue);
            treeFellerLength = treeFellerStrings[0];
            treeFellerLengthEndurance = treeFellerStrings[1];
        }

        // DOUBLE DROPS
        if (canDoubleDrop) {
            String[] doubleDropStrings = calculateAbilityDisplayValues(skillValue, SecondaryAbility.WOODCUTTING_DOUBLE_DROPS, isLucky);
            doubleDropChance = doubleDropStrings[0];
            doubleDropChanceLucky = doubleDropStrings[1];
        }
    }

    @Override
    protected void permissionsCheck(Player player) {
        canTreeFell = Permissions.treeFeller(player);
        canDoubleDrop = Permissions.secondaryAbilityEnabled(player, SecondaryAbility.WOODCUTTING_DOUBLE_DROPS) && !skill.getDoubleDropsDisabled();
        canLeafBlow = Permissions.secondaryAbilityEnabled(player, SecondaryAbility.LEAF_BLOWER);
    }

    @Override
    protected List<String> effectsDisplay() {
        List<String> messages = new ArrayList<String>();

        if (canTreeFell) {
            messages.add(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Woodcutting.Effect.0"), LocaleLoader.getString("Woodcutting.Effect.1")));
        }

        if (canLeafBlow) {
            messages.add(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Woodcutting.Effect.2"), LocaleLoader.getString("Woodcutting.Effect.3")));
        }

        if (canDoubleDrop) {
            messages.add(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Woodcutting.Effect.4"), LocaleLoader.getString("Woodcutting.Effect.5")));
        }

        return messages;
    }

    @Override
    protected List<String> statsDisplay(Player player, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<String>();

        if (canLeafBlow) {
            int leafBlowerUnlockLevel = AdvancedConfig.getInstance().getLeafBlowUnlockLevel();

            if (skillValue < leafBlowerUnlockLevel) {
                messages.add(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Woodcutting.Ability.Locked.0", leafBlowerUnlockLevel)));
            } else {
                messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Woodcutting.Ability.0"), LocaleLoader.getString("Woodcutting.Ability.1")));
            }
        }

        if (canDoubleDrop) {
            messages.add(LocaleLoader.getString("Woodcutting.Ability.Chance.DDrop", doubleDropChance) + (isLucky ? LocaleLoader.getString("Perks.Lucky.Bonus", doubleDropChanceLucky) : ""));
        }

        if (canTreeFell) {
            messages.add(LocaleLoader.getString("Woodcutting.Ability.Length", treeFellerLength) + (hasEndurance ? LocaleLoader.getString("Perks.ActivationTime.Bonus", treeFellerLengthEndurance) : ""));
        }

        return messages;
    }
}
