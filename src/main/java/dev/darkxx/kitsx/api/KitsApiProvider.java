package dev.darkxx.kitsx.api;

import dev.darkxx.kitsx.utils.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
        File kitsFolder = new File(plugin.getDataFolder(), "data");
        kitsAPI = new KitUtil(new File(kitsFolder, "kits.yml"), loadConfig(plugin, "kits.yml"));
        premadeKitAPI = new PremadeKitUtil(new File(kitsFolder, "premadekit.yml"), loadConfig(plugin, "premadekit.yml"));
        kitRoomAPI = new KitRoomUtil(new File(kitsFolder, "kitroom.yml"), loadConfig(plugin, "kitroom.yml"));
        enderChestAPI = new EnderChestUtil(new File(kitsFolder, "enderchest.yml"), loadConfig(plugin, "enderchest.yml"));
        autoRekitAPI = new AutoRekitUtil(new File(kitsFolder, "autorekit.yml"), loadConfig(plugin, "autorekit.yml"));
        return new KitsApiProvider();
    }

    /**
     * Loads a YAML configuration file from the plugin's data folder.
     *
     * @param plugin   The JavaPlugin instance.
     * @param fileName The name of the YAML file.
     * @return The loaded FileConfiguration.
     */
    private static FileConfiguration loadConfig(JavaPlugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder() + File.separator + "data", fileName);
        if (!file.exists()) {
            plugin.saveResource("data" + File.separator + fileName, false);
        }
        return YamlConfiguration.loadConfiguration(file);
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
