package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitUtil {

    private static final Logger logger = Logger.getLogger(KitUtil.class.getName());
    private final File kitsFile;
    private final FileConfiguration kitsStorage;

    public KitUtil(File kitsFile, FileConfiguration kitsStorage) {
        this.kitsFile = kitsFile;
        this.kitsStorage = kitsStorage;
    }

    public void save(Player player, String kitName) {
        String playerName = player.getName();
        if (exists(player, kitName)) {
            delete(player, kitName);
        }

        for (int i = 0; i < 36; ++i) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
                kitsStorage.set(playerName + "." + kitName + ".inventory." + i, item);
        }

        for (int i = 36; i < 40; ++i) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
                kitsStorage.set(playerName + "." + kitName + ".armor." + i, item);
        }

        ItemStack offhandItem = player.getOpenInventory().getTopInventory().getItem(40);
            kitsStorage.set(playerName + "." + kitName + ".offhand", offhandItem);

        try {
            kitsStorage.save(kitsFile);
            String kitSaved = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.kit-saved")).replace("%kit%", kitName);
            player.sendMessage(ColorizeText.hex(kitSaved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save kit ", e);
        }
    }

    public void load(Player player, String kitName) {
        String playerName = player.getName();
        if (kitsStorage.contains(playerName + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".inventory." + i);
                    player.getInventory().setItem(i, item);
            }

            for (int i = 36; i < 40; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".armor." + i);
                    player.getInventory().setItem(i, item);
            }

            ItemStack offhandItem = kitsStorage.getItemStack(playerName + "." + kitName + ".offhand");
                player.getInventory().setItemInOffHand(offhandItem);

            if (Main.getInstance().getConfig().getBoolean("broadcast.kit-load", true)) {
                String bcastLoaded = Main.getInstance().getConfig().getString("broadcast.kit-load-message");
                if (bcastLoaded != null) {
                    bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                    Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                }
            }

            String kitLoaded = Main.getInstance().getConfig().getString("messages.kit-loaded");
            if (kitLoaded != null) {
                kitLoaded = kitLoaded.replace("%kit%", kitName);
                player.sendMessage(ColorizeText.hex(kitLoaded));
            }

            Main.getEnderChestUtil().load(player, kitName);
        } else {
            player.sendMessage(ColorizeText.hex("&#ffa6a6" + kitName + " is empty."));
        }
    }

    public void set(Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getName();
        if (kitsStorage.contains(playerName + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".inventory." + i);
                    inventory.setItem(i, item);
            }

            for (int i = 36; i < 40; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".armor." + i);
                    inventory.setItem(i, item);
            }

            ItemStack offhandItem = kitsStorage.getItemStack(playerName + "." + kitName + ".offhand");
                inventory.setItem(40, offhandItem);
        }
    }

    public void importInventory(Player player, GuiBuilder inventory) {
        ItemStack[] playerItems = player.getInventory().getContents();

        for (int i = 0; i < 36; i++) {
            inventory.setItem(i, playerItems[i]);
        }

        inventory.setItem(39, player.getInventory().getHelmet());
        inventory.setItem(38, player.getInventory().getChestplate());
        inventory.setItem(37, player.getInventory().getLeggings());
        inventory.setItem(36, player.getInventory().getBoots());

        inventory.setItem(40, player.getInventory().getItemInOffHand());
    }

    public void delete(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        kitsStorage.set(playerName + "." + kitName, null);

        try {
            kitsStorage.save(kitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to delete kit ", e);
        }
    }

    public boolean exists(Player player, String kitName) {
        String playerName = player.getName();
        return kitsStorage.contains(playerName + "." + kitName);
    }

    public void saveAll() {
        try {
            kitsStorage.save(kitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save kits file", e);
        }
    }
}
