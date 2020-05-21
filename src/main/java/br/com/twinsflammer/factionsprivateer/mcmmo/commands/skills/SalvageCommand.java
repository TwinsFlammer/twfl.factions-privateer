package br.com.twinsflammer.factionsprivateer.mcmmo.commands.skills;

import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SecondaryAbility;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.salvage.Salvage;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.salvage.SalvageManager;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Permissions;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class SalvageCommand extends SkillCommand {

    private boolean canAdvancedSalvage;
    private boolean canArcaneSalvage;

    public SalvageCommand() {
        super(SkillType.SALVAGE);
    }

    @Override
    protected void dataCalculations(Player player, float skillValue, boolean isLucky) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void permissionsCheck(Player player) {
        canAdvancedSalvage = Permissions.secondaryAbilityEnabled(player, SecondaryAbility.ADVANCED_SALVAGE);
        canArcaneSalvage = Permissions.secondaryAbilityEnabled(player, SecondaryAbility.ARCANE_SALVAGE);
    }

    @Override
    protected List<String> effectsDisplay() {
        List<String> messages = new ArrayList<String>();

        if (canAdvancedSalvage) {
            messages.add(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Salvage.Effect.0"), LocaleLoader.getString("Salvage.Effect.1")));
        }

        if (canArcaneSalvage) {
            messages.add(LocaleLoader.getString("Effects.Template", LocaleLoader.getString("Salvage.Effect.2"), LocaleLoader.getString("Salvage.Effect.3")));
        }

        return messages;
    }

    @Override
    protected List<String> statsDisplay(Player player, float skillValue, boolean hasEndurance, boolean isLucky) {
        List<String> messages = new ArrayList<String>();
        SalvageManager salvageManager = UserManager.getPlayer(player).getSalvageManager();

        if (canAdvancedSalvage) {
            if (skillValue < Salvage.advancedSalvageUnlockLevel) {
                messages.add(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Salvage.Ability.Locked.0", Salvage.advancedSalvageUnlockLevel)));
            } else {
                messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Salvage.Ability.Bonus.0"), LocaleLoader.getString("Salvage.Ability.Bonus.1", percent.format(salvageManager.getMaxSalvagePercentage()))));
            }
        }

        if (canArcaneSalvage) {
            messages.add(LocaleLoader.getString("Salvage.Arcane.Rank", salvageManager.getArcaneSalvageRank(), Salvage.Tier.values().length));

            if (Salvage.arcaneSalvageEnchantLoss) {
                messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Salvage.Arcane.ExtractFull"), percent.format(salvageManager.getExtractFullEnchantChance() / 100)));
            }

            if (Salvage.arcaneSalvageDowngrades) {
                messages.add(LocaleLoader.getString("Ability.Generic.Template", LocaleLoader.getString("Salvage.Arcane.ExtractPartial"), percent.format(salvageManager.getExtractPartialEnchantChance() / 100)));
            }
        }

        return messages;
    }
}
