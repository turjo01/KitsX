package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.KitsAPI;
import dev.darkxx.kitsx.api.events.KitLoadEvent;
import dev.darkxx.kitsx.api.events.KitSaveEvent;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
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

public class KitUtil implements KitsAPI {

    private static final Logger logger = Logger.getLogger(KitUtil.class.getName());
    private static File kitsFile;
    private static FileConfiguration kitsStorage;

    public KitUtil(File kitsFile, FileConfiguration kitsStorage) {
        KitUtil.kitsFile = kitsFile;
        KitUtil.kitsStorage = kitsStorage;
    }

    public static void of(JavaPlugin plugin) {
        kitsFile = new File(plugin.getDataFolder(), "data/kits.yml");
        if (!kitsFile.exists()) {
            plugin.saveResource("data/kits.yml", false);
        }
        kitsStorage = YamlConfiguration.loadConfiguration(kitsFile);
    }

    @Override
    public void save(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        if (exists(player, kitName)) {
            delete(player, kitName);
        }

        ItemStack[] inventoryContents = new ItemStack[36];
        for (int i = 0; i < 36; ++i) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
            inventoryContents[i] = item;
            kitsStorage.set(playerName + "." + kitName + ".inventory." + i, item);
        }

        ItemStack[] armorContents = new ItemStack[4];
        for (int i = 36; i < 40; ++i) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
            armorContents[i - 36] = item;
            kitsStorage.set(playerName + "." + kitName + ".armor." + (i - 36), item);
        }

        ItemStack offhandItem = player.getOpenInventory().getTopInventory().getItem(40);
        kitsStorage.set(playerName + "." + kitName + ".offhand", offhandItem);

        KitSaveEvent event = new KitSaveEvent(player, kitName, inventoryContents, armorContents, offhandItem);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        try {
            kitsStorage.save(kitsFile);
            String kitSaved = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.kit-saved")).replace("%kit%", kitName);
            player.sendMessage(ColorizeText.hex(kitSaved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save kit ); ", e);
        }
    }

    @Override
    public void load(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        KitLoadEvent event = new KitLoadEvent(player, kitName);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if (kitsStorage.contains(playerName + "." + kitName)) {
                for (int i = 0; i < 36; ++i) {
                    ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".inventory." + i);
                    player.getInventory().setItem(i, item);
                }

                for (int i = 0; i < 4; ++i) {
                    ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".armor." + i);
                    player.getInventory().setItem(36 + i, item);
                }

                ItemStack offhandItem = kitsStorage.getItemStack(playerName + "." + kitName + ".offhand");
                player.getInventory().setItemInOffHand(offhandItem);

                if (KitsX.getInstance().getConfig().getBoolean("broadcast.kit-load", true)) {
                    String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.kit-load-message");
                    if (bcastLoaded != null) {
                        bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                        Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                    }
                }

                String kitLoaded = KitsX.getInstance().getConfig().getString("messages.kit-loaded");
                if (kitLoaded != null) {
                    kitLoaded = kitLoaded.replace("%kit%", kitName);
                    player.sendMessage(ColorizeText.hex(kitLoaded));
                }

                KitsX.getEnderChestUtil().load(player, kitName);
            } else {
                player.sendMessage(ColorizeText.hex("&#ffa6a6" + kitName + " is empty."));
            }
        }
    }

    @Override
    public void set(Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getUniqueId().toString();
        if (kitsStorage.contains(playerName + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".inventory." + i);
                inventory.setItem(i, item);
            }

            for (int i = 0; i < 4; ++i) {
                ItemStack item = kitsStorage.getItemStack(playerName + "." + kitName + ".armor." + i);
                inventory.setItem(36 + i, item);
            }

            ItemStack offhandItem = kitsStorage.getItemStack(playerName + "." + kitName + ".offhand");
            inventory.setItem(40, offhandItem);
        }
    }


    @Override
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

    @Override
    public void delete(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        kitsStorage.set(playerName + "." + kitName, null);

        try {
            kitsStorage.save(kitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to delete kit ", e);
        }
    }

    @Override
    public boolean exists(Player player, String kitName) {
        String playerName = player.getName();
        return kitsStorage.contains(playerName + "." + kitName);
    }

    @Override
    public void saveAll() {
        try {
            kitsStorage.save(kitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save kits file", e);
        }
    }
}
