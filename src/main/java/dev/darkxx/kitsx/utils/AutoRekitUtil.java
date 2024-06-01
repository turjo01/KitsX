package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.AutoRekitAPI;
import dev.darkxx.kitsx.utils.config.MenuConfig;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRekitUtil implements AutoRekitAPI {

    private final MenuConfig CONFIG = new MenuConfig(KitsX.getInstance(), "menus/auto-rekit.yml");
    private static ConfigManager configManager;

    public AutoRekitUtil(ConfigManager configManager) {
        AutoRekitUtil.configManager = configManager;
    }

    public static void of(JavaPlugin plugin) {
        configManager = ConfigManager.get(plugin);
        configManager.create("data/autorekit.yml");
    }

    @Override
    public void set(Player player, Boolean enabled, String kitName) {
        String playerName = player.getUniqueId().toString();
        configManager.set("data/autorekit.yml", playerName + ".auto-rekit.enabled", enabled);
        configManager.set("data/autorekit.yml", playerName + ".auto-rekit.kit", kitName);
        save();
    }

    @Override
    public void setKit(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        configManager.set("data/autorekit.yml", playerName + ".auto-rekit.kit", kitName);
        save();
    }

    @Override
    public void toggle(Player player) {
        String playerName = player.getUniqueId().toString();
        boolean currentSetting = configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto-rekit.enabled");
        configManager.set("data/autorekit.yml", playerName + ".auto-rekit.enabled", !currentSetting);
        save();
    }

    @Override
    public boolean get(Player player) {
        String playerName = player.getUniqueId().toString();
        return configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto-rekit.enabled", false);
    }

    @Override
    public String getKit(Player player) {
        String playerName = player.getUniqueId().toString();
        return configManager.getConfig("data/autorekit.yml").getString(playerName + ".auto-rekit.kit", "Kit 1");
    }

    @Override
    public boolean isEnabled(Player player) {
        String playerName = player.getUniqueId().toString();
        return configManager.getConfig("data/autorekit.yml").getBoolean(playerName + ".auto-rekit.enabled", false);
    }

    @Override
    public boolean hasAutoRekit(Player player) {
        String playerName = player.getUniqueId().toString();
        return configManager.contains("data/autorekit.yml", playerName + ".auto-rekit");
    }

    @Override
    public String status(Player player) {
        boolean autoRekitEnabled = get(player);
        String enabled = CONFIG.getConfig().getString("auto-rekit.placeholders.enabled", "&#7cff6eEnabled");
        String disabled = CONFIG.getConfig().getString("auto-rekit.placeholders.disabled", "&#ffa6a6Disabled");
        return autoRekitEnabled ? ColorizeText.hex(enabled) : ColorizeText.hex(disabled);
    }

    private void save() {
        configManager.saveConfig("data/autorekit.yml");
    }
}