package br.com.twinsflammer.factionscaribe.mcmmo.tasks;

//import br.com.titanwar.api.API;
//import br.com.titanwar.api.user.SpigotUser;
//import br.com.titanwar.common.spigot.utils.PlayerUtils;
//import br.com.titanwar.common.spigot.v8.utils.NMS;
import br.com.twinsflammer.factionscaribe.mcmmo.mcMMO;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class McMMOMusicTextLevel implements Runnable {

    @Override
    public void run() {
        if (!mcMMO.musicText) {
            return;
        }

        Set<Player> players = Sets.newConcurrentHashSet(Bukkit.getOnlinePlayers());

        players.stream()
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .forEach(player -> {

                    try {
                        /*
                        Player target = PlayerUtils.getLookingAt(player);
                        SpigotUser playerUser = API.getInstance().getUserFactory().getUser(player);

                        if (/*playerUser.getPreferences().getValue(PreferenceSeeTextLevel.INSTANCE) == 0
                                &&*//*target != null && player != null && player.canSee(target)) {

                            SpigotUser targetUser = API.getInstance().getUserFactory().getUser(target);
                            McMMOPlayer mcPlayer = UserManager.getPlayer(target);

                            if (mcPlayer != null && targetUser != null
                                    /* && targetUser.getPreferences().getValue(PreferenceVanish.INSTANCE) == 0*/
                                    /*&& !target.getGameMode().equals(GameMode.SPECTATOR)) {

                                Integer rank = McMMORanksTask.getPlayerRank(target.getName()).get(null);
                                StringBuilder builder = new StringBuilder();

                                builder.append(targetUser.getDisplayName())
                                        .append(ChatColor.GRAY)
                                        .append(" - ")
                                        .append(ChatColor.WHITE)
                                        .append("Nível: ")
                                        .append(ChatColor.YELLOW)
                                        .append(mcPlayer.getPowerLevel());

                                if (rank != null) {
                                    builder.append(ChatColor.WHITE)
                                            .append(" Ranking: ")
                                            .append(ChatColor.YELLOW)
                                            .append(rank)
                                            .append("º");
                                }

                                NMS.sendMusicText(builder.toString(), player);
                            }
                        }
                        */
                    } catch (Exception e) {
                    }
                });
    }

}
