package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.AutoRekitAPI;
import dev.darkxx.kitsx.menus.config.MenuConfig;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoRekitUtil implements AutoRekitAPI {

    private static final Logger logger = Logger.getLogger(AutoRekitUtil.class.getName());
    private final FileConfiguration autoRekitData;
    private final File autoRekitFile;
    private static final MenuConfig CONFIG = new MenuConfig(KitsX.getInstance(), "menus/auto-rekit.yml");


    public AutoRekitUtil(File autoRekitFile, FileConfiguration autoRekitData) {
        this.autoRekitFile = autoRekitFile;
        this.autoRekitData = autoRekitData;
    }

    public static AutoRekitUtil of(JavaPlugin plugin) {
        File autoRekitFile = new File(plugin.getDataFolder(), "data/autorekit.yml");
        if (!autoRekitFile.exists()) {
            plugin.saveResource("data/autorekit.yml", false);
        }
        return new AutoRekitUtil(autoRekitFile, YamlConfiguration.loadConfiguration(autoRekitFile));
    }

    @Override
    public void set(Player player, Boolean enabled, String kitName) {
        String playerName = player.getUniqueId().toString();
        autoRekitData.set(playerName + ".auto-rekit.enabled", enabled);
        autoRekitData.set(playerName + ".auto-rekit.kit", kitName);
        save();
    }

    @Override
    public void setKit(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        autoRekitData.set(playerName + ".auto-rekit.kit", kitName);
        save();
    }

    @Override
    public void toggle(Player player) {
        String playerName = player.getUniqueId().toString();
        boolean currentSetting = autoRekitData.getBoolean(playerName + ".auto-rekit.enabled");
        autoRekitData.set(playerName + ".auto-rekit.enabled", !currentSetting);
        save();
    }

    @Override
    public boolean get(Player player) {
        String playerName = player.getUniqueId().toString();
        return autoRekitData.getBoolean(playerName + ".auto-rekit.enabled", false);
    }

    @Override
    public String getKit(Player player) {
        String playerName = player.getUniqueId().toString();
        return autoRekitData.getString(playerName + ".auto-rekit.kit", "Kit 1");
    }

    @Override
    public boolean isEnabled(Player player) {
        String playerName = player.getUniqueId().toString();
        return autoRekitData.getBoolean(playerName + ".auto-rekit.enabled", false);
    }

    @Override
    public boolean hasAutoRekit(Player player) {
        String playerName = player.getUniqueId().toString();
        return autoRekitData.contains(playerName + ".auto-rekit");
    }

    @Override
    public String status(Player player) {
        boolean autoRekitEnabled = get(player);
        String enabled = CONFIG.getConfig().getString("auto-rekit.placeholders.enabled", "&#7cff6eEnabled");
        String disabled = CONFIG.getConfig().getString("auto-rekit.placeholders.disabled", "&#ffa6a6Disabled");
        return autoRekitEnabled ? ColorizeText.hex(enabled) : ColorizeText.hex(disabled);
    }

    private void save() {
        try {
            autoRekitData.save(autoRekitFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save auto-rekit file ); ", e);
        }
    }
}
