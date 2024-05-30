package dev.darkxx.kitsx.api;

import org.bukkit.entity.Player;

/**
 * Interface for managing auto-rekit functionality.
 */
public interface AutoRekitAPI {

    /**
     * Sets auto-rekit settings for a player.
     *
     * @param player   The player for whom the auto-rekit settings will be set.
     * @param enabled  Whether auto-rekit is enabled or disabled.
     * @param kitName  The name of the kit for auto-rekit.
     */
    void set(Player player, Boolean enabled, String kitName);

    /**
     * Sets the kit for auto-rekit for a player.
     *
     * @param player   The player for whom the auto-rekit kit will be set.
     * @param kitName  The name of the kit for auto-rekit.
     */
    void setKit(Player player, String kitName);

    /**
     * Toggles auto-rekit for a player.
     *
     * @param player  The player for whom auto-rekit will be toggled.
     */
    void toggle(Player player);

    /**
     * Gets the auto-rekit status for a player.
     *
     * @param player  The player for whom the auto-rekit status will be retrieved.
     * @return True if auto-rekit is enabled for the player, otherwise false.
     */
    boolean get(Player player);

    /**
     * Gets the auto-rekit kit name for a player.
     *
     * @param player  The player for whom the auto-rekit kit name will be retrieved.
     * @return The name of the kit set for auto-rekit.
     */
    String getKit(Player player);

    /**
     * Checks if auto-rekit is enabled for a player.
     *
     * @param player  The player to check for auto-rekit.
     * @return True if auto-rekit is enabled for the player, otherwise false.
     */
    boolean isEnabled(Player player);

    /**
     * Checks if a player has auto-rekit settings.
     *
     * @param player  The player to check for auto-rekit settings.
     * @return True if the player has auto-rekit settings, otherwise false.
     */
    boolean hasAutoRekit(Player player);

    /**
     * Retrieves the status of auto-rekit for a player.
     *
     * @param player  The player for whom the auto-rekit status will be retrieved.
     * @return A string representing the status of auto-rekit for the player.
     */
    String status(Player player);
}
