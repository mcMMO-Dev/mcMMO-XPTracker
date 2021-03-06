package org.mcmmo.mcmmoxptracker.util.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.mcmmo.mcmmoxptracker.datatypes.player.McMMOPlayer;
import org.mcmmo.mcmmoxptracker.mcMMOXPTracker;

public final class UserManager {
    private final static Map<String, McMMOPlayer> players = new HashMap<String, McMMOPlayer>();

    private UserManager() {}

    /**
     * Add a new user.
     *
     * @param player The player to create a user record for
     *
     * @return the player's {@link McMMOPlayer} object
     */
    public static McMMOPlayer addUser(Player player) {
        String playerName = player.getName();
        McMMOPlayer mcMMOPlayer = players.get(playerName);

        if (mcMMOPlayer != null) {
            mcMMOPlayer.setPlayer(player); // The player object is different on each reconnection and must be updated
        }
        else {
            mcMMOPlayer = new McMMOPlayer(player);
            players.put(playerName, mcMMOPlayer);
        }

        return mcMMOPlayer;
    }

    /**
     * Remove a user.
     *
     * @param playerName The name of the player to remove
     */
    public static void remove(String playerName) {
        players.remove(playerName);
    }

    /**
     * Clear all users.
     */
    public static void clearAll() {
        players.clear();
    }

    public static Set<String> getPlayerNames() {
        return players.keySet();
    }

    public static Collection<McMMOPlayer> getPlayers() {
        return players.values();
    }

    /**
     * Get the McMMOPlayer of a player by name.
     *
     * @param playerName The name of the player whose McMMOPlayer to retrieve
     *
     * @return the player's McMMOPlayer object
     */
    public static McMMOPlayer getPlayer(String playerName) {
        return retrieveMcMMOPlayer(playerName, false);
    }

    /**
     * Get the McMMOPlayer of a player.
     *
     * @param player The player whose McMMOPlayer to retrieve
     *
     * @return the player's McMMOPlayer object
     */
    public static McMMOPlayer getPlayer(OfflinePlayer player) {
        return retrieveMcMMOPlayer(player.getName(), false);
    }

    public static McMMOPlayer getPlayer(String playerName, boolean offlineValid) {
        return retrieveMcMMOPlayer(playerName, offlineValid);
    }

    private static McMMOPlayer retrieveMcMMOPlayer(String playerName, boolean offlineValid) {
        McMMOPlayer mcMMOPlayer = players.get(playerName);

        if (mcMMOPlayer == null) {
            Player player = mcMMOXPTracker.p.getServer().getPlayerExact(playerName);

            if (player == null) {
                if (!offlineValid) {
                    mcMMOXPTracker.p.getLogger().warning("A valid mcMMOPlayer object could not be found for " + playerName + ".");
                }

                return null;
            }

            mcMMOPlayer = new McMMOPlayer(player);
            players.put(playerName, mcMMOPlayer);
        }

        return mcMMOPlayer;
    }
}
