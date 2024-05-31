package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.PremadeKitAPI;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PremadeKitUtil implements PremadeKitAPI {

    private static final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());

    private static File premadeKitsFile;
    private static FileConfiguration premadeKitsStorage;

    public PremadeKitUtil(File premadeKitsFile, FileConfiguration premadeKitsStorage) {
        PremadeKitUtil.premadeKitsFile = premadeKitsFile;
        PremadeKitUtil.premadeKitsStorage = premadeKitsStorage;
    }

    public static void of(JavaPlugin plugin) {
        premadeKitsFile = new File(plugin.getDataFolder(), "data/premadekit.yml");
        if (!premadeKitsFile.exists()) {
            plugin.saveResource("data/premadekit.yml", false);
        }
        premadeKitsStorage = YamlConfiguration.loadConfiguration(premadeKitsFile);
    }

    @Override
    public void save(Player player) {
        try {
            Inventory inventory = player.getInventory();
            List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
            List<ItemStack> armorList = Arrays.asList(player.getInventory().getArmorContents());
            premadeKitsStorage.set("premadekit.inventory", inventoryList);
            premadeKitsStorage.set("premadekit.armor", armorList);
            premadeKitsStorage.set("premadekit.offhand", player.getInventory().getItemInOffHand());
            premadeKitsStorage.save(premadeKitsFile);
            String saved = KitsX.getInstance().getConfig().getString("messages.premade-kit-saved");
            assert saved != null;
            player.sendMessage(ColorizeText.hex(saved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save premade kits file", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(GuiBuilder inventory) {
        List<ItemStack> inventoryItems = (List<ItemStack>) premadeKitsStorage.getList("premadekit.inventory");
        List<ItemStack> armorItems = (List<ItemStack>) premadeKitsStorage.getList("premadekit.armor");
        ItemStack offhandItem = premadeKitsStorage.getItemStack("premadekit.offhand");
        for (int i = 0; i < inventoryItems.size(); i++) {
            ItemStack item = inventoryItems.get(i);
            if (item != null) {
                inventory.setItem(i, item);
            }
        }
        for (int i = 0; i < armorItems.size(); i++) {
            ItemStack item = armorItems.get(i);
            if (item != null) {
                inventory.setItem(i + 36, item);
            }
        }
        if (offhandItem != null) {
            inventory.setItem(40, offhandItem);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(Player player) {
        if (premadeKitsStorage.contains("premadekit")) {
            player.getInventory().clear();
            player.getInventory().setContents(((List<ItemStack>) premadeKitsStorage.getList("premadekit.inventory")).toArray(new ItemStack[0]));
            player.getInventory().setArmorContents(((List<ItemStack>) premadeKitsStorage.getList("premadekit.armor")).toArray(new ItemStack[0]));
            ItemStack offhandItem = premadeKitsStorage.getItemStack("premadekit.offhand");
            if (offhandItem != null) {
                player.getInventory().setItemInOffHand(offhandItem);
            }

            if (KitsX.getInstance().getConfig().getBoolean("broadcast.premadekit-load", true)) {
                String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.premadekit-load-message");
                assert bcastLoaded != null;
                bcastLoaded = bcastLoaded.replace("%player%", player.getName());
                Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
            }

            String loaded = KitsX.getInstance().getConfig().getString("messages.premade-kit-loaded");
            assert loaded != null;
            player.sendMessage(ColorizeText.hex(loaded));

        } else {
            String empty = KitsX.getInstance().getConfig().getString("messages.premade-kit-empty");
            assert empty != null;
            player.sendMessage(ColorizeText.hex(empty));
        }
    }

    @Override
    public void saveAll() {
        try {
            premadeKitsStorage.save(premadeKitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save premadekits file", e);
        }
    }
}

