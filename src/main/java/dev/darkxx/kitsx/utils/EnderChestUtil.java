package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.EnderChestAPI;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnderChestUtil implements EnderChestAPI {

    private static final Logger logger = Logger.getLogger(EnderChestUtil.class.getName());

    private final File enderchestFile;
    private final FileConfiguration enderchestStorage;

    public EnderChestUtil(File enderchestFile, FileConfiguration enderchestStorage) {
        this.enderchestFile = enderchestFile;
        this.enderchestStorage = enderchestStorage;
    }

    public static EnderChestUtil of(JavaPlugin plugin) {
        File enderchestFile = new File(plugin.getDataFolder(), "data/enderchest.yml");
        if (!enderchestFile.exists()) {
            plugin.saveResource("data/enderchest.yml", false);
        }
        return new EnderChestUtil(enderchestFile, YamlConfiguration.loadConfiguration(enderchestFile));
    }

    @Override
    public void save(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        if (exists(player, kitName)) {
            delete(player, kitName);
        }

        for (int i = 0; i < 27; i++) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
            if (item != null) {
                enderchestStorage.set(playerName + "." + kitName + ".enderchest." + i, item);
            }
        }

        try {
            enderchestStorage.save(enderchestFile);
            String enderchestSaved = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.enderchest-saved")).replace("%kit%", kitName);
            player.sendMessage(ColorizeText.hex(enderchestSaved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save ender chest ); ", e);
        }
    }

    @Override
    public void load(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        player.getEnderChest().clear();

        ConfigurationSection section = enderchestStorage.getConfigurationSection(playerName + "." + kitName + ".enderchest");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ItemStack item = section.getItemStack(key);
                if (item != null) {
                    player.getEnderChest().setItem(Integer.parseInt(key), item);
                }
            }
        }
    }

    @Override
    public void set(Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getUniqueId().toString();

        ConfigurationSection section = enderchestStorage.getConfigurationSection(playerName + "." + kitName + ".enderchest");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ItemStack item = section.getItemStack(key);
                if (item != null) {
                    inventory.setItem(Integer.parseInt(key), item);
                }
            }
        }
    }

    @Override
    public void delete(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        enderchestStorage.set(playerName + "." + kitName + ".enderchest", null);

        try {
            enderchestStorage.save(enderchestFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save ender chest ); ", e);
        }
    }

    @Override
    public boolean exists(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        return enderchestStorage.contains(playerName + "." + kitName + ".enderchest");
    }

    @Override
    public void saveAll() {
        try {
            enderchestStorage.save(enderchestFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save ender chest ); ", e);
        }
    }
}
