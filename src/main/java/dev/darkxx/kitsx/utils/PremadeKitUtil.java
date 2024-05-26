package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PremadeKitUtil {

    private static final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());

    private final File premadeKitsFile;
    private final FileConfiguration premadeKitsStorage;

    public PremadeKitUtil(File premadeKitsFile, FileConfiguration premadeKitsStorage) {
        this.premadeKitsFile = premadeKitsFile;
        this.premadeKitsStorage = premadeKitsStorage;
    }

    public void save(Player player) {
        try {
            Inventory inventory = player.getInventory();
            List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
            List<ItemStack> armorList = Arrays.asList(player.getInventory().getArmorContents());
            premadeKitsStorage.set("premadekit.inventory", inventoryList);
            premadeKitsStorage.set("premadekit.armor", armorList);
            premadeKitsStorage.set("premadekit.offhand", player.getInventory().getItemInOffHand());
            premadeKitsStorage.save(premadeKitsFile);
            String saved = Main.getInstance().getConfig().getString("messages.premade-kit-saved");
            assert saved != null;
            player.sendMessage(ColorizeText.hex(saved));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save premade kits file", e);
        }
    }

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

            if (Main.getInstance().getConfig().getBoolean("broadcast.premadekit-load", true)) {
                String bcastLoaded = Main.getInstance().getConfig().getString("broadcast.premadekit-load-message");
                assert bcastLoaded != null;
                bcastLoaded = bcastLoaded.replace("%player%", player.getName());
                Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
            }

            String loaded = Main.getInstance().getConfig().getString("messages.premade-kit-loaded");
            assert loaded != null;
            player.sendMessage(ColorizeText.hex(loaded));

        } else {
            String empty = Main.getInstance().getConfig().getString("messages.premade-kit-empty");
            assert empty != null;
            player.sendMessage(ColorizeText.hex(empty));
        }
    }

    public void saveAll() {
        try {
            premadeKitsStorage.save(premadeKitsFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save premadekits file", e);
        }
    }
}

