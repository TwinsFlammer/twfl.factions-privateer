package br.com.twinsflammer.factionsprivateer.mcmmo.database;

import br.com.twinsflammer.factionsprivateer.mcmmo.config.Config;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.DatabaseType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.database.PlayerStat;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.player.PlayerProfile;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.SkillType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DatabaseManager {

    // One month in milliseconds
    public final long PURGE_TIME = 2630000000L * Config.getInstance().getOldUsersCutoff();
    // During convertUsers, how often to output a status
    public final int progressInterval = 200;

    /**
     * Purge users with 0 power level from the database.
     */
    public void purgePowerlessUsers();

    /**
     * Purge users who haven't logged on in over a certain time frame from the
     * database.
     */
    public void purgeOldUsers();

    /**
     * Remove a user from the database.
     *
     * @param playerName The name of the user to remove
     * @return true if the user was successfully removed, false otherwise
     */
    public boolean removeUser(String playerName);

    /**
     * Save a user to the database.
     *
     * @param profile The profile of the player to save
     * @return true if successful, false on failure
     */
    public boolean saveUser(PlayerProfile profile);

    /**
     * Retrieve leaderboard info.
     *
     * @param skill The skill to retrieve info on
     * @param pageNumber Which page in the leaderboards to retrieve
     * @param statsPerPage The number of stats per page
     * @return the requested leaderboard information
     */
    public List<PlayerStat> readLeaderboard(SkillType skill, int pageNumber, int statsPerPage);

    /**
     * Retrieve rank info into a HashMap from SkillType to the rank.
     * <p>
     * The special value <code>null</code> is used to represent the Power Level
     * rank (the combination of all skill levels).
     *
     * @param playerName The name of the user to retrieve the rankings for
     * @return the requested rank information
     */
    public Map<SkillType, Integer> readRank(String playerName);

    /**
     * Add a new user to the database.
     *
     * @param playerName The name of the player to be added to the database
     * @param uuid The uuid of the player to be added to the database
     */
    public void newUser(String playerName, UUID uuid);

    /**
     * Load a player from the database.
     *
     * @deprecated replaced by
     * {@link #loadPlayerProfile(String playerName, UUID uuid, boolean createNew)}
     *
     * @param playerName The name of the player to load from the database
     * @param createNew Whether to create a new record if the player is not
     * found
     * @return The player's data, or an unloaded PlayerProfile if not found and
     * createNew is false
     */
    @Deprecated
    public PlayerProfile loadPlayerProfile(String playerName, boolean createNew);

    /**
     * Load a player from the database.
     *
     * @param uuid The uuid of the player to load from the database
     * @return The player's data, or an unloaded PlayerProfile if not found
     */
    public PlayerProfile loadPlayerProfile(UUID uuid);

    /**
     * Load a player from the database. Attempt to use uuid, fall back on
     * playername
     *
     * @param playerName The name of the player to load from the database
     * @param uuid The uuid of the player to load from the database
     * @param createNew Whether to create a new record if the player is not
     * found
     * @return The player's data, or an unloaded PlayerProfile if not found and
     * createNew is false
     */
    public PlayerProfile loadPlayerProfile(String playerName, UUID uuid, boolean createNew);

    /**
     * Get all users currently stored in the database.
     *
     * @return list of playernames
     */
    public List<String> getStoredUsers();

    /**
     * Convert all users from this database to the provided database using
     * {@link #saveUser(PlayerProfile)}.
     *
     * @param destination The DatabaseManager to save to
     */
    public void convertUsers(DatabaseManager destination);

    public boolean saveUserUUID(String userName, UUID uuid);

    public boolean saveUserUUIDs(Map<String, UUID> fetchedUUIDs);

    /**
     * Retrieve the type of database in use. Custom databases should return
     * CUSTOM.
     *
     * @return The type of database
     */
    public DatabaseType getDatabaseType();

    /**
     * Called when the plugin disables
     */
    public void onDisable();

    public void saveUsers(Collection<PlayerProfile> collect);
}
