package dev.darkxx.kitsx.api;

import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import org.bukkit.entity.Player;

/**
 * Interface for managing premade kit.
 */
public interface PremadeKitAPI {

    /**
     * Saves the contents of the player's inventory as a premade kit.
     *
     * @param player The player whose inventory will be saved as a premade kit.
     */
    void save(Player player);

    /**
     * Sets the premade kit contents in a GuiBuilder inventory.
     *
     * @param inventory The inventory to set the premade kit contents in.
     */
    void set(GuiBuilder inventory);

    /**
     * Loads a premade kit into a player's inventory.
     *
     * @param player The player who will receive the premade kit.
     */
    void load(Player player);

    /**
     * Saves all premade kits.
     */
    void saveAll();
}
