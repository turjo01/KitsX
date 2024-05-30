package dev.darkxx.kitsx.api;

import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import org.bukkit.entity.Player;

/**
 * Interface for managing player ender chests.
 */
public interface EnderChestAPI {

    /**
     * Saves the contents of the player's open top inventory (ender chest menu) as a kit.
     *
     * @param player  The player whose ender chest contents will be saved.
     * @param kitName The name of the kit to save.
     */
    void save(Player player, String kitName);

    /**
     * Loads a kit's ender chest items.
     *
     * @param player  The player who will receive the kit in their ender chest.
     * @param kitName The name of the kit to load.
     */
    void load(Player player, String kitName);

    /**
     * Sets the kit's ender chest items in a GuiBuilder inventory.
     *
     * @param player    The player whose ender chest inventory will be set.
     * @param kitName   The name of the kit.
     * @param inventory The inventory to set for the kit.
     */
    void set(Player player, String kitName, GuiBuilder inventory);

    /**
     * Deletes an ender chest for a kit.
     *
     * @param player  The player whose ender chest kit will be deleted.
     * @param kitName The name of the kit to delete.
     */
    void delete(Player player, String kitName);

    /**
     * Checks if an ender chest for a kit exists in a player's ender chest.
     *
     * @param player  The player to check for the kit.
     * @param kitName The name of the kit.
     * @return True if the kit exists in the player's ender chest, otherwise false.
     */
    boolean exists(Player player, String kitName);

    /**
     * Saves all player ender chest kits.
     */
    void saveAll();
}
