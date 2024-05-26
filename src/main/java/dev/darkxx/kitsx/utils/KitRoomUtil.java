package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitRoomUtil {
    private static final Logger logger = Logger.getLogger(KitRoomUtil.class.getName());
    private final File kitRoomFile;
    private final FileConfiguration kitRoomStorage;

    public KitRoomUtil(File kitRoomFile, FileConfiguration kitRoomStorage) {
        this.kitRoomFile = kitRoomFile;
        this.kitRoomStorage = kitRoomStorage;
    }

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
            logger.log(Level.SEVERE, "Failed to save category ", e);
        }
    }


    public void load(GuiBuilder inventory, String category) {
        for (int i1 = 0; i1 < 45; i1++) {
            inventory.setItem(i1, new ItemStack(Material.AIR));
        }
        for (int i = 0; i < 45; i++) {
            ItemStack item = kitRoomStorage.getItemStack("categories." + category + "." + i);
            if (item != null) {
                inventory.setItem(i, item);
            }
        }
    }

    public void delete(String category) {
        if (exists(category)) {
            kitRoomStorage.set("categories." + category, null);
        }

        try {
            kitRoomStorage.save(kitRoomFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to delete category ", e);
        }
    }

    public boolean exists(String category) {
        return kitRoomStorage.contains("categories." + category + ".");
    }
}