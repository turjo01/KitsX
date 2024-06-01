package dev.darkxx.kitsx.api;

import dev.darkxx.kitsx.utils.*;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A provider class for accessing various APIs related to KitsX.
 */
public class KitsApiProvider {

    private static KitsAPI kitsAPI;
    private static PremadeKitAPI premadeKitAPI;
    private static KitRoomAPI kitRoomAPI;
    private static EnderChestAPI enderChestAPI;
    private static AutoRekitAPI autoRekitAPI;

    private KitsApiProvider() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initializes the Kits API provider.
     *
     * @param plugin The JavaPlugin instance.
     * @return An instance of KitsApiProvider.
     */
    public static KitsApiProvider init(JavaPlugin plugin) {
        ConfigManager configManager = ConfigManager.get(plugin);

        kitsAPI = new KitUtil(configManager);
        premadeKitAPI = new PremadeKitUtil(configManager);
        kitRoomAPI = new KitRoomUtil(configManager);
        enderChestAPI = new EnderChestUtil(configManager);
        autoRekitAPI = new AutoRekitUtil(configManager);
        return new KitsApiProvider();
    }

    // Getters for each API

    /**
     * Retrieves the Kits API.
     *
     * @return An instance of KitsAPI.
     */
    public KitsAPI getKitsAPI() {
        return kitsAPI;
    }

    /**
     * Retrieves the Premade Kit API.
     *
     * @return An instance of PremadeKitAPI.
     */
    public PremadeKitAPI getPremadeKitAPI() {
        return premadeKitAPI;
    }

    /**
     * Retrieves the Kit Room API.
     *
     * @return An instance of KitRoomAPI.
     */
    public KitRoomAPI getKitRoomAPI() {
        return kitRoomAPI;
    }

    /**
     * Retrieves the Ender Chest API.
     *
     * @return An instance of EnderChestAPI.
     */
    public EnderChestAPI getEnderChestAPI() {
        return enderChestAPI;
    }

    /**
     * Retrieves the Auto Rekit API.
     *
     * @return An instance of AutoRekitAPI.
     */
    public AutoRekitAPI getAutoRekitAPI() {
        return autoRekitAPI;
    }
}
