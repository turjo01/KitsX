package dev.darkxx.kitsx.api;

import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import org.bukkit.entity.Player;

/**
 * Interface for managing kit rooms.
 */
public interface KitRoomAPI {

    /**
     * Saves the contents of the player's open top inventory as a kit room category.
     *
     * @param player   The player whose inventory will be saved.
     * @param category The category name for the kit room.
     */
    void save(Player player, String category);

    /**
     * Loads a kit room category into a GuiBuilder inventory.
     *
     * @param inventory The inventory to load the kit room into.
     * @param category  The category name for the kit room.
     */
    void load(GuiBuilder inventory, String category);

    /**
     * Deletes a kit room category.
     *
     * @param category The category name to delete.
     */
    void delete(String category);

    /**
     * Checks if a kit room category exists.
     *
     * @param category The category name to check.
     * @return True if the category exists, otherwise false.
     */
    boolean exists(String category);
}
