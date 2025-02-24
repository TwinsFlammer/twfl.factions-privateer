package br.com.twinsflammer.factionsprivateer.mcmmo.party;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.party.ItemWeightConfig;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.ItemShareType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.Party;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.party.ShareMode;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.McMMOPlayer;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.XPGainReason;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.Misc;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.player.UserManager;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ShareHandler {

    private ShareHandler() {
    }

    /**
     * Distribute Xp amongst party members.
     *
     * @param xp Xp without party sharing
     * @param mcMMOPlayer Player initiating the Xp gain
     * @param skillType Skill being used
     * @return True is the xp has been shared
     */
    public static boolean handleXpShare(float xp, McMMOPlayer mcMMOPlayer, SkillType skillType, XPGainReason xpGainReason) {
        Party party = mcMMOPlayer.getParty();

        if (party.getXpShareMode() != ShareMode.EQUAL) {
            return false;
        }

        List<Player> nearMembers = PartyManager.getNearMembers(mcMMOPlayer);

        if (nearMembers.isEmpty()) {
            return false;
        }

        nearMembers.add(mcMMOPlayer.getPlayer());

        int partySize = nearMembers.size();
        double shareBonus = Math.min(Config.getInstance().getPartyShareBonusBase() + (partySize * Config.getInstance().getPartyShareBonusIncrease()), Config.getInstance().getPartyShareBonusCap());
        float splitXp = (float) (xp / partySize * shareBonus);

        for (Player member : nearMembers) {
            UserManager.getPlayer(member).beginUnsharedXpGain(skillType, splitXp, xpGainReason);
        }

        return true;
    }

    /**
     * Distribute Items amongst party members.
     *
     * @param drop Item that will get shared
     * @param mcMMOPlayer Player who picked up the item
     * @return True if the item has been shared
     */
    public static boolean handleItemShare(Item drop, McMMOPlayer mcMMOPlayer) {
        ItemStack itemStack = drop.getItemStack();
        ItemShareType dropType = ItemShareType.getShareType(itemStack);

        if (dropType == null) {
            return false;
        }

        Party party = mcMMOPlayer.getParty();

        if (!party.sharingDrops(dropType)) {
            return false;
        }

        ShareMode shareMode = party.getItemShareMode();

        if (shareMode == ShareMode.NONE) {
            return false;
        }

        List<Player> nearMembers = PartyManager.getNearMembers(mcMMOPlayer);

        if (nearMembers.isEmpty()) {
            return false;
        }

        Player winningPlayer = null;
        ItemStack newStack = itemStack.clone();

        nearMembers.add(mcMMOPlayer.getPlayer());
        int partySize = nearMembers.size();

        drop.remove();
        newStack.setAmount(1);

        switch (shareMode) {
            case EQUAL:
                int itemWeight = ItemWeightConfig.getInstance().getItemWeight(itemStack.getType());

                for (int i = 0; i < itemStack.getAmount(); i++) {
                    int highestRoll = 0;

                    for (Player member : nearMembers) {
                        McMMOPlayer mcMMOMember = UserManager.getPlayer(member);
                        int itemShareModifier = mcMMOMember.getItemShareModifier();
                        int diceRoll = Misc.getRandom().nextInt(itemShareModifier);

                        if (diceRoll <= highestRoll) {
                            mcMMOMember.setItemShareModifier(itemShareModifier + itemWeight);
                            continue;
                        }

                        highestRoll = diceRoll;

                        if (winningPlayer != null) {
                            McMMOPlayer mcMMOWinning = UserManager.getPlayer(winningPlayer);
                            mcMMOWinning.setItemShareModifier(mcMMOWinning.getItemShareModifier() + itemWeight);
                        }

                        winningPlayer = member;
                    }

                    McMMOPlayer mcMMOTarget = UserManager.getPlayer(winningPlayer);
                    mcMMOTarget.setItemShareModifier(mcMMOTarget.getItemShareModifier() - itemWeight);
                    awardDrop(winningPlayer, newStack);
                }

                return true;

            case RANDOM:
                for (int i = 0; i < itemStack.getAmount(); i++) {
                    winningPlayer = nearMembers.get(Misc.getRandom().nextInt(partySize));
                    awardDrop(winningPlayer, newStack);
                }

                return true;

            default:
                return false;
        }
    }

    public static XPGainReason getSharedXpGainReason(XPGainReason xpGainReason) {
        if (xpGainReason == XPGainReason.PVE) {
            return XPGainReason.SHARED_PVE;
        } else if (xpGainReason == XPGainReason.PVP) {
            return XPGainReason.SHARED_PVP;
        } else {
            return xpGainReason;
        }
    }

    private static void awardDrop(Player winningPlayer, ItemStack drop) {
        if (winningPlayer.getInventory().addItem(drop).size() != 0) {
            winningPlayer.getWorld().dropItemNaturally(winningPlayer.getLocation(), drop);
        }

        winningPlayer.updateInventory();
    }
}
