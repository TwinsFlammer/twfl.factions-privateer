package br.com.twinsflammer.factionscaribe.mcmmo.util.commands;

import br.com.twinsflammer.factionscaribe.mcmmo.commands.chat.AdminChatCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.chat.PartyChatCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.database.McpurgeCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.database.McremoveCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.database.MmoshowdbCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.experience.AddlevelsCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.experience.AddxpCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.experience.MmoeditCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.experience.SkillresetCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.hardcore.HardcoreCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.hardcore.VampirismCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.party.PartyCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.party.teleport.PtpCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.config.Config;
import br.com.twinsflammer.factionscaribe.mcmmo.locale.LocaleLoader;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.KrakenCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McImportCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McabilityCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McconvertCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McgodCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McmmoCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McnotifyCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.McrefreshCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.MobhealthCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.XprateCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.player.InspectCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.player.MccooldownCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.player.McrankCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.player.McstatsCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.player.MctopCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.AcrobaticsCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.AlchemyCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.ArcheryCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.AxesCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.ExcavationCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.FishingCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.HerbalismCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.MiningCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.RepairCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.SalvageCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.SmeltingCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.SwordsCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.TamingCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.UnarmedCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.commands.skills.WoodcuttingCommand;
import br.com.twinsflammer.factionscaribe.mcmmo.datatypes.skills.SkillType;
import br.com.twinsflammer.factionscaribe.mcmmo.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

import br.com.twinsflammer.factionscaribe.FactionsCaribe;
import lombok.NoArgsConstructor;
import org.bukkit.command.PluginCommand;

@NoArgsConstructor
public final class CommandRegistrationManager {

    private static String permissionsMessage = LocaleLoader.getString("mcMMO.NoPermission");

    private static void registerSkillCommands() {
        for (SkillType skill : SkillType.values()) {
            if (!skill.isActive()) {
                continue;
            }

            registerSkillCommand(skill);
        }
    }

    public static void registerSkillCommand(SkillType skill) {
        String commandName = skill.toString().toLowerCase();
        String localizedName = skill.getName().toLowerCase();

        PluginCommand command;

        command = FactionsCaribe.getInstance().getCommand(commandName);
        command.setDescription(LocaleLoader.getString("Commands.Description.Skill", StringUtils.getCapitalized(localizedName)));
        command.setPermission("mcmmo.commands." + commandName);
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", localizedName));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.2", localizedName, "?", "[" + LocaleLoader.getString("Commands.Usage.Page") + "]"));

//        String cleanName = StringUtils.cleanPortugueseCharacters(localizedName);
//        if (!cleanName.equals(localizedName)) {
//            CommandMap commandMap = ((CraftServer) mcMMO.p.getServer()).getCommandMap();
//            command.unregister(commandMap);
//            command.setAliases(Lists.newArrayList(cleanName));
//        }
        setExecutor(command, skill);
    }

    private static void setExecutor(PluginCommand command, SkillType skill) {

        switch (skill) {
            case ACROBATICS:
                command.setExecutor(new AcrobaticsCommand());
                break;

            case ALCHEMY:
                command.setExecutor(new AlchemyCommand());
                break;

            case ARCHERY:
                command.setExecutor(new ArcheryCommand());
                break;

            case AXES:
                command.setExecutor(new AxesCommand());
                break;

            case EXCAVATION:
                command.setExecutor(new ExcavationCommand());
                break;

            case FISHING:
                command.setExecutor(new FishingCommand());
                break;

            case HERBALISM:
                command.setExecutor(new HerbalismCommand());
                break;

            case MINING:
                command.setExecutor(new MiningCommand());
                break;

            case REPAIR:
                command.setExecutor(new RepairCommand());
                break;

            case SALVAGE:
                command.setExecutor(new SalvageCommand());
                break;

            case SMELTING:
                command.setExecutor(new SmeltingCommand());
                break;

            case SWORDS:
                command.setExecutor(new SwordsCommand());
                break;

            case TAMING:
                command.setExecutor(new TamingCommand());
                break;

            case UNARMED:
                command.setExecutor(new UnarmedCommand());
                break;

            case WOODCUTTING:
                command.setExecutor(new WoodcuttingCommand());
                break;

            default:
                break;
        }
    }

    private static void registerAddlevelsCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("addlevels");
        command.setDescription(LocaleLoader.getString("Commands.Description.addlevels"));
        command.setPermission("mcmmo.commands.addlevels;mcmmo.commands.addlevels.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.3", "addlevels", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]", "<" + LocaleLoader.getString("Commands.Usage.Skill") + ">", "<" + LocaleLoader.getString("Commands.Usage.Level") + ">"));
        command.setExecutor(new AddlevelsCommand());
    }

    private static void registerAddxpCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("addxp");
        command.setDescription(LocaleLoader.getString("Commands.Description.addxp"));
        command.setPermission("mcmmo.commands.addxp;mcmmo.commands.addxp.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.3", "addxp", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]", "<" + LocaleLoader.getString("Commands.Usage.Skill") + ">", "<" + LocaleLoader.getString("Commands.Usage.XP") + ">"));
        command.setExecutor(new AddxpCommand());
    }

    private static void registerMcgodCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcgod");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcgod"));
        command.setPermission("mcmmo.commands.mcgod;mcmmo.commands.mcgod.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcgod", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]"));
        command.setExecutor(new McgodCommand());
    }

    private static void registerMcrefreshCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcrefresh");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcrefresh"));
        command.setPermission("mcmmo.commands.mcrefresh;mcmmo.commands.mcrefresh.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcrefresh", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]"));
        command.setExecutor(new McrefreshCommand());
    }

    private static void registerMmoeditCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mmoedit");
        command.setDescription(LocaleLoader.getString("Commands.Description.mmoedit"));
        command.setPermission("mcmmo.commands.mmoedit;mcmmo.commands.mmoedit.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.3", "mmoedit", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]", "<" + LocaleLoader.getString("Commands.Usage.Skill") + ">", "<" + LocaleLoader.getString("Commands.Usage.Level") + ">"));
        command.setExecutor(new MmoeditCommand());
    }

    private static void registerSkillresetCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("skillreset");
        command.setDescription(LocaleLoader.getString("Commands.Description.skillreset"));
        command.setPermission("mcmmo.commands.skillreset;mcmmo.commands.skillreset.others"); // Only need the main ones, not the individual skill ones
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.2", "skillreset", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]", "<" + LocaleLoader.getString("Commands.Usage.Skill") + ">"));
        command.setExecutor(new SkillresetCommand());
    }

    private static void registerXprateCommand() {
        List<String> aliasList = new ArrayList<String>();
        aliasList.add("mcxprate");

        PluginCommand command = FactionsCaribe.getInstance().getCommand("xprate");
        command.setDescription(LocaleLoader.getString("Commands.Description.xprate"));
        command.setPermission("mcmmo.commands.xprate;mcmmo.commands.xprate.reset;mcmmo.commands.xprate.set");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.2", "xprate", "<" + LocaleLoader.getString("Commands.Usage.Rate") + ">", "<true|false>"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "xprate", "reset"));
        command.setAliases(aliasList);
        command.setExecutor(new XprateCommand());
    }

    public static void registerInspectCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("inspect");
        command.setDescription(LocaleLoader.getString("Commands.Description.inspect"));
        command.setPermission("mcmmo.commands.inspect;mcmmo.commands.inspect.far;mcmmo.commands.inspect.offline");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "inspect", "<" + LocaleLoader.getString("Commands.Usage.Player") + ">"));
        command.setExecutor(new InspectCommand());
    }

    private static void registerMccooldownCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mccooldown");
        command.setDescription(LocaleLoader.getString("Commands.Description.mccooldown"));
        command.setPermission("mcmmo.commands.mccooldown");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mccooldowns"));
        command.setExecutor(new MccooldownCommand());
    }

    private static void registerMcabilityCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcability");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcability"));
        command.setPermission("mcmmo.commands.mcability;mcmmo.commands.mcability.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcability", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]"));
        command.setExecutor(new McabilityCommand());
    }

    private static void registerMcmmoCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcmmo");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcmmo"));
        command.setPermission("mcmmo.commands.mcmmo.description;mcmmo.commands.mcmmo.help");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcmmo"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "mcmmo", "help"));
        command.setExecutor(new McmmoCommand());
    }

    private static void registerMcrankCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcrank");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcrank"));
        command.setPermission("mcmmo.commands.mcrank;mcmmo.commands.mcrank.others;mcmmo.commands.mcrank.others.far;mcmmo.commands.mcrank.others.offline");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcrank", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]"));
        command.setExecutor(new McrankCommand());
    }

    public static void registerMcstatsCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcstats");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcstats"));
        command.setPermission("mcmmo.commands.mcstats");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcstats"));
        command.setExecutor(new McstatsCommand());
    }

    public static void registerMctopCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mctop");
        command.setDescription(LocaleLoader.getString("Commands.Description.mctop"));
        command.setPermission("mcmmo.commands.mctop"); // Only need the main one, not the individual skill ones
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.2", "mctop", "[" + LocaleLoader.getString("Commands.Usage.Skill") + "]", "[" + LocaleLoader.getString("Commands.Usage.Page") + "]"));
        command.setExecutor(new MctopCommand());
    }

    private static void registerMcpurgeCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcpurge");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcpurge", Config.getInstance().getOldUsersCutoff()));
        command.setPermission("mcmmo.commands.mcpurge");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcpurge"));
        command.setExecutor(new McpurgeCommand());
    }

    private static void registerMcremoveCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcremove");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcremove"));
        command.setPermission("mcmmo.commands.mcremove");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcremove", "<" + LocaleLoader.getString("Commands.Usage.Player") + ">"));
        command.setExecutor(new McremoveCommand());
    }

    private static void registerMmoshowdbCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mmoshowdb");
        command.setDescription(LocaleLoader.getString("Commands.Description.mmoshowdb"));
        command.setPermission("mcmmo.commands.mmoshowdb");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mmoshowdb"));
        command.setExecutor(new MmoshowdbCommand());
    }

    private static void registerMcconvertCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcconvert");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcconvert"));
        command.setPermission("mcmmo.commands.mcconvert;mcmmo.commands.mcconvert.experience;mcmmo.commands.mcconvert.database");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.2", "mcconvert", "database", "<flatfile|sql>"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.2", "mcconvert", "experience", "<linear|exponential>"));
        command.setExecutor(new McconvertCommand());
    }

    private static void registerAdminChatCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("adminchat");
        command.setDescription(LocaleLoader.getString("Commands.Description.adminchat"));
        command.setPermission("mcmmo.chat.adminchat");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "adminchat"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "adminchat", "<on|off>"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "adminchat", "<" + LocaleLoader.getString("Commands.Usage.Message") + ">"));
        command.setExecutor(new AdminChatCommand());
    }

    private static void registerPartyChatCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("partychat");
        command.setDescription(LocaleLoader.getString("Commands.Description.partychat"));
        command.setPermission("mcmmo.chat.partychat;mcmmo.commands.party");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "partychat"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "partychat", "<on|off>"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "partychat", "<" + LocaleLoader.getString("Commands.Usage.Message") + ">"));
        command.setExecutor(new PartyChatCommand());
    }

    private static void registerPartyCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("party");
        command.setDescription(LocaleLoader.getString("Commands.Description.party"));
        command.setPermission("mcmmo.commands.party;mcmmo.commands.party.accept;mcmmo.commands.party.create;mcmmo.commands.party.disband;"
                + "mcmmo.commands.party.xpshare;mcmmo.commands.party.invite;mcmmo.commands.party.itemshare;mcmmo.commands.party.join;"
                + "mcmmo.commands.party.kick;mcmmo.commands.party.lock;mcmmo.commands.party.owner;mcmmo.commands.party.password;"
                + "mcmmo.commands.party.quit;mcmmo.commands.party.rename;mcmmo.commands.party.unlock");
        command.setPermissionMessage(permissionsMessage);
        command.setExecutor(new PartyCommand());
    }

    private static void registerPtpCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("ptp");
        command.setDescription(LocaleLoader.getString("Commands.Description.ptp"));
        command.setPermission("mcmmo.commands.ptp"); // Only need the main one, not the individual ones for toggle/accept/acceptall
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "ptp", "<" + LocaleLoader.getString("Commands.Usage.Player") + ">"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "ptp", "<toggle|accept|acceptall>"));
        command.setExecutor(new PtpCommand());
    }

    private static void registerHardcoreCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("hardcore");
        command.setDescription(LocaleLoader.getString("Commands.Description.hardcore"));
        command.setPermission("mcmmo.commands.hardcore;mcmmo.commands.hardcore.toggle;mcmmo.commands.hardcore.modify");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "hardcore", "[on|off]"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "hardcore", "<" + LocaleLoader.getString("Commands.Usage.Rate") + ">"));
        command.setExecutor(new HardcoreCommand());
    }

    private static void registerVampirismCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("vampirism");
        command.setDescription(LocaleLoader.getString("Commands.Description.vampirism"));
        command.setPermission("mcmmo.commands.vampirism;mcmmo.commands.vampirism.toggle;mcmmo.commands.vampirism.modify");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "vampirism", "[on|off]"));
        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.1", "vampirism", "<" + LocaleLoader.getString("Commands.Usage.Rate") + ">"));
        command.setExecutor(new VampirismCommand());
    }

    private static void registerMcnotifyCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcnotify");
        command.setDescription(LocaleLoader.getString("Commands.Description.mcnotify"));
        command.setPermission("mcmmo.commands.mcnotify");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcnotify"));
        command.setExecutor(new McnotifyCommand());
    }

    private static void registerMobhealthCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mobhealth");
        command.setDescription("Change the style of the mob healthbar"); //TODO: Localize
        command.setPermission("mcmmo.commands.mobhealth");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mobhealth", "<DISABLED | HEARTS | BAR>"));
        command.setExecutor(new MobhealthCommand());
    }

//    private static void registerMcscoreboardCommand() {
//        PluginCommand command = mcMMO.p.getCommand("mcscoreboard");
//        command.setDescription("Change the current mcMMO scoreboard being displayed"); //TODO: Localize
//        command.setPermission("mcmmo.commands.mcscoreboard");
//        command.setPermissionMessage(permissionsMessage);
//        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "mcscoreboard", "<CLEAR | KEEP>"));
//        command.setUsage(command.getUsage() + "\n" + LocaleLoader.getString("Commands.Usage.2", "mcscoreboard", "time", "<seconds>"));
//        command.setExecutor(new McscoreboardCommand());
//    }
    private static void registerKrakenCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("kraken");
        command.setDescription("Unleash the kraken!"); //TODO: Localize
        command.setPermission("mcmmo.commands.kraken;mcmmo.commands.kraken.others");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.1", "kraken", "[" + LocaleLoader.getString("Commands.Usage.Player") + "]"));
        command.setExecutor(new KrakenCommand());
    }

    private static void registerMcImportCommand() {
        PluginCommand command = FactionsCaribe.getInstance().getCommand("mcimport");
        command.setDescription("Import mod config files"); //TODO: Localize
        command.setPermission("mcmmo.commands.mcimport");
        command.setPermissionMessage(permissionsMessage);
        command.setUsage(LocaleLoader.getString("Commands.Usage.0", "mcimport"));
        command.setExecutor(new McImportCommand());
    }

    public static void registerCommands() {
        // Generic Commands
//        registerMcImportCommand();
        registerKrakenCommand();
        registerMcabilityCommand();
//        registerMcgodCommand();
//        registerMcmmoCommand();
        registerMcnotifyCommand();
        registerMcrefreshCommand();
//        registerMcscoreboardCommand();
//        registerMobhealthCommand();
        registerXprateCommand();

        // Chat Commands
//        registerPartyChatCommand();
//        registerAdminChatCommand();
        // Database Commands
        registerMcpurgeCommand();
        registerMcremoveCommand();
//        registerMmoshowdbCommand();
//        registerMcconvertCommand();

        // Experience Commands
        registerAddlevelsCommand();
        registerAddxpCommand();
        registerMmoeditCommand();
        registerSkillresetCommand();

        // Hardcore Commands
        registerHardcoreCommand();
        registerVampirismCommand();

        // Party Commands
//        registerPartyCommand();
//        registerPtpCommand();
        // Player Commands
//        registerInspectCommand();
        registerMccooldownCommand();
        registerMcrankCommand();
//        registerMcstatsCommand();
//        registerMctopCommand();

        // Skill Commands
        registerSkillCommands();

//        CommandRegistry.registerCommand(new CommandSkills());
    }

    public static void enableMctopCommand() {
        registerMctopCommand();
    }

    public static void enableMcstatsCommand() {
        registerMcstatsCommand();
    }

    public static void enableInspectCommand() {
        registerInspectCommand();
    }
}
