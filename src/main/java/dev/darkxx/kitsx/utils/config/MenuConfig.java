package dev.darkxx.kitsx.utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MenuConfig {

    private final FileConfiguration config;

    public MenuConfig(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public static void of(Plugin plugin) {
        new MenuConfig(plugin, "menus/kits.yml");
        new MenuConfig(plugin, "menus/kit-editor.yml");
        new MenuConfig(plugin, "menus/enderchest-editor.yml");
        new MenuConfig(plugin, "menus/auto-rekit.yml");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}