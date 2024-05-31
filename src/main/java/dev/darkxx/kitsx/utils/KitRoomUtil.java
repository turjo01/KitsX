package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.api.KitRoomAPI;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitRoomUtil implements KitRoomAPI {

    private static final Logger logger = Logger.getLogger(KitRoomUtil.class.getName());

    private final File kitRoomFile;
    private final FileConfiguration kitRoomStorage;

    public KitRoomUtil(File kitRoomFile, FileConfiguration kitRoomStorage) {
        this.kitRoomFile = kitRoomFile;
        this.kitRoomStorage = kitRoomStorage;
    }

    public static KitRoomUtil of(JavaPlugin plugin) {
        File kitRoomFile = new File(plugin.getDataFolder(), "data/kitroom.yml");
        if (!kitRoomFile.exists()) {
            plugin.saveResource("data/kitroom.yml", false);
        }
        return new KitRoomUtil(kitRoomFile, YamlConfiguration.loadConfiguration(kitRoomFile));
    }

    @Override
    public void save(Player player, String category) {
        if (exists(category)) {
            delete(category);
        }

        for (int i = 0; i < 45; i++) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
            if (item != null) {
                kitRoomStorage.set("categories." + category + "." + i, item);
            }
        }

        try {
            kitRoomStorage.save(kitRoomFile);
            player.sendMessage(ColorizeText.hex("&#7cff6eKit room category " + category + " saved!"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save category ): ", e);
        }
    }

    @Override
    public void load(GuiBuilder inventory, String category) {
        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
            ItemStack item = kitRoomStorage.getItemStack("categories." + category + "." + i);
            if (item != null) {
                inventory.setItem(i, item);
            }
        }
    }

    @Override
    public void delete(String category) {
        if (exists(category)) {
            kitRoomStorage.set("categories." + category, null);
        }

        try {
            kitRoomStorage.save(kitRoomFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save category ): ", e);
        }
    }

    @Override
    public boolean exists(String category) {
        return kitRoomStorage.contains("categories." + category + ".");
    }
}
