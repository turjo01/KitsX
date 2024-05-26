package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnderChestUtil {

    private static final Logger logger = Logger.getLogger(EnderChestUtil.class.getName());

    private final File enderchestFile;
    private final FileConfiguration enderchestStorage;

    public EnderChestUtil(File enderchestFile, FileConfiguration enderchestStorage) {
        this.enderchestFile = enderchestFile;
        this.enderchestStorage = enderchestStorage;
    }

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
            String enderchestSaved = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.enderchest-saved")).replace("%kit%", kitName);
            player.sendMessage(ColorizeText.hex(enderchestSaved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "failed to save ender chest ); ", e);
        }
    }

    public void load(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        player.getEnderChest().clear();

        if (enderchestStorage.contains(playerName + "." + kitName + ".enderchest")) {
            for (int i = 0; i < 27; i++) {
                ItemStack item = enderchestStorage.getItemStack(playerName + "." + kitName + ".enderchest." + i);
                if (item != null) {
                    player.getEnderChest().setItem(i, item);
                }
            }
        }
    }

    public void set(Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getUniqueId().toString();

        if (enderchestStorage.contains(playerName + "." + kitName + ".enderchest")) {
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
    }

    public void delete(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        enderchestStorage.set(playerName + "." + kitName + ".enderchest", null);

        try {
            enderchestStorage.save(enderchestFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "failed to save ender chest ); ", e);
        }
    }

    public boolean exists(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        return enderchestStorage.contains(playerName + "." + kitName + ".enderchest");
    }
    public void saveAll() {
        try {
            enderchestStorage.save(enderchestFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "failed to save ender chest ); ", e);
        }
    }
}
