package dev.darkxx.kitsx.api;

import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import org.bukkit.entity.Player;

/**
 * Interface for managing player kits.
 */
public interface KitsAPI {

    /**
     * Saves the contents of the player's open top inventory as a player kit.
     *
     * @param player   The player whose inventory will be saved.
     * @param kitName  The name of the kit.
     */
    void save(Player player, String kitName);

    /**
     * Loads a kit into a player's inventory.
     *
     * @param player   The player who will receive the kit.
     * @param kitName  The name of the kit to load.
     */
    void load(Player player, String kitName);

    /**
     * Sets the kit items in a GuiBuilder inventory.
     *
     * @param player    The player whose kit inventory will be set.
     * @param kitName   The name of the kit.
     * @param inventory The inventory to set for the kit.
     */
    void set(Player player, String kitName, GuiBuilder inventory);

    /**
     * Imports the inventory of a player into a GuiBuilder inventory.
     *
     * @param player    The player whose inventory will be imported.
     * @param inventory The GuiBuilder inventory to import the player's inventory into.
     */
    void importInventory(Player player, GuiBuilder inventory);

    /**
     * Deletes a kit.
     *
     * @param player   The player who owns the kit.
     * @param kitName  The name of the kit to delete.
     */
    void delete(Player player, String kitName);

    /**
     * Checks if a kit exists for a player.
     *
     * @param player   The player to check for the kit.
     * @param kitName  The name of the kit.
     * @return True if the kit exists for the player, otherwise false.
     */
    boolean exists(Player player, String kitName);

    /**
     * Saves all kits.
     */
    void saveAll();
}
