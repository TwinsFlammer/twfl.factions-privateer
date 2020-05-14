package com.redefocus.factionscaribe.commands.staff;

import com.redefocus.api.spigot.commands.CustomCommand;
import com.redefocus.api.spigot.commands.defaults.tps.manager.TPSManager;
import com.redefocus.api.spigot.commands.enums.CommandRestriction;
import com.redefocus.common.shared.permissions.group.GroupNames;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.util.TimeFormatter;
import com.redefocus.factionscaribe.FactionsCaribe;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

/**
 * @author SrGutyerrez
 */
public class MemoryCommand extends CustomCommand {
    public MemoryCommand() {
        super(
                "gc",
                CommandRestriction.ALL,
                GroupNames.DIRECTOR
        );
    }

    @Override
    public void onCommand(CommandSender commandSender, User user, String[] args) {
        Server server = Bukkit.getServer();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();


        DecimalFormat TpsFormat = new DecimalFormat("#.##");
        Double tps = TPSManager.getTPS();

        String message = "";

        if (tps >= 15.0) {
            message = "§a" + TpsFormat.format(tps);
        } else if (tps >= 10.0) {
            message = "§e" + TpsFormat.format(tps);
        } else if (tps >= 5.1) {
            message = "§c" + TpsFormat.format(tps);
        } else if (tps <= 5.0) {
            message = "§4" + TpsFormat.format(tps);
        }

        commandSender.sendMessage(new String[]{
                "",
                "§2Informações disponíveis:",
                "",
                "§fStatus: §aOnline",
                "§fNome do servidor: §7" + server.getName(),
                "§fTPS atual: " + message,
                "§fTempo online: §7" + TimeFormatter.format(System.currentTimeMillis() - FactionsCaribe.getStartTime()),
                "§fJogadores atuais: §7" + server.getOnlinePlayers().size() + "/" + server.getMaxPlayers(),
                "§fVersão do servidor: §7" + server.getBukkitVersion(),
                "",
                "§fVersão do java: §7" + System.getProperty("java.version"),
                "§fSistema operacional: §7" + this.OSName(),
                "§fArquitetura do Sistema: §7" + this.OsArch(),
                "§fVersão do sistema operacional: §7" + System.getProperty("os.version").split("-")[0],
                "§fUso da memória: §7" + this.usedMemory() + "/" + this.totalMemory(),
                "§fCarga média: §7" + osBean.getSystemLoadAverage(),
                "§fQuantidade de CPUs: §7" + Runtime.getRuntime().availableProcessors(),
                ""
        });
    }

    String OSName() {
        return System.getProperty("os.name");
    }

    String OSVersion() {
        return System.getProperty("os.version");
    }

    String OsArch() {
        return System.getProperty("os.arch");
    }

    Long totalMemory() {
        return Runtime.getRuntime().maxMemory() / 1048576L;
    }

    Long usedMemory() {
        Runtime runtime = Runtime.getRuntime();

        return (runtime.maxMemory() / 1048576L) - (Runtime.getRuntime().freeMemory() / 1048576L);
    }
}
