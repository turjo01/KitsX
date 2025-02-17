package dev.darkxx.kitsx.utils;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.PremadeKitAPI;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PremadeKitUtil implements PremadeKitAPI {

    private static ConfigManager configManager;
    private final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());
    private final Map<String, Long> lastBroadcastTime = new HashMap<>();

    public PremadeKitUtil(ConfigManager configManager) {
        PremadeKitUtil.configManager = configManager;
    }

    public static void of(KitsX plugin) {
        configManager = ConfigManager.get(plugin);
        configManager.create("data/premadekit.yml");
    }

    @Override
    public void save(Player player, String kitName) {
        try {
            Inventory inventory = player.getInventory();
            List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
            List<ItemStack> armorList = Arrays.asList(player.getInventory().getArmorContents());

            ConfigurationSection kitSection = configManager.getConfig("data/premadekit.yml").createSection("kits." + kitName);
            kitSection.set("inventory", inventoryList);
            kitSection.set("armor", armorList);
            kitSection.set("offhand", player.getInventory().getItemInOffHand());

            configManager.saveConfig("data/premadekit.yml");

            String saved = KitsX.getInstance().getConfig().getString("messages.premade_kit_saved");
            if (saved != null) {
                player.sendMessage(ColorizeText.hex(saved.replace("%kit%", kitName)));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save kit: " + kitName, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(GuiBuilder inventory, String kitName) {
        ConfigurationSection kitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("kits." + kitName);
        if (kitSection != null) {
            List<ItemStack> inventoryItems = (List<ItemStack>) kitSection.get("inventory");
            List<ItemStack> armorItems = (List<ItemStack>) kitSection.get("armor");
            ItemStack offhandItem = kitSection.getItemStack("offhand");

            for (int i = 0; i < Objects.requireNonNull(inventoryItems).size(); i++) {
                ItemStack item = inventoryItems.get(i);
                if (item != null) {
                    inventory.setItem(i, item);
                }
            }
            for (int i = 0; i < Objects.requireNonNull(armorItems).size(); i++) {
                ItemStack item = armorItems.get(i);
                if (item != null) {
                    inventory.setItem(i + 36, item);
                }
            }
            if (offhandItem != null) {
                inventory.setItem(40, offhandItem);
            }
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public void load(Player player, String kitName) {
        ConfigurationSection kitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("kits." + kitName);
        if (kitSection != null) {
            player.getInventory().clear();
            player.getInventory().setContents(((List<ItemStack>) Objects.requireNonNull(kitSection.get("inventory"))).toArray(new ItemStack[0]));
            player.getInventory().setArmorContents(((List<ItemStack>) Objects.requireNonNull(kitSection.get("armor"))).toArray(new ItemStack[0]));
            ItemStack offhandItem = kitSection.getItemStack("offhand");
            if (offhandItem != null) {
                player.getInventory().setItemInOffHand(offhandItem);
            }

            long currentTime = System.currentTimeMillis();
            String playerName = player.getUniqueId().toString();
            long lastTime = lastBroadcastTime.getOrDefault(playerName, 0L);
            long delayMillis = KitsX.getInstance().getConfig().getInt("broadcast.premadekit_load_message_delay", 10) * 50L;

            if (KitsX.getInstance().getConfig().getBoolean("broadcast.premadekit_load", true) && (currentTime - lastTime > delayMillis)) {
                String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.premadekit_load_message");
                if (bcastLoaded != null) {
                    bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                    Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                }
                lastBroadcastTime.put(playerName, currentTime);
            }

            String loaded = KitsX.getInstance().getConfig().getString("messages.premade_kit_loaded");
            if (loaded != null) {
                player.sendMessage(ColorizeText.hex(loaded.replace("%kit%", kitName)));
            }

        } else {
            String empty = KitsX.getInstance().getConfig().getString("messages.premade_kit_empty");
            if (empty != null) {
                player.sendMessage(ColorizeText.hex(empty.replace("%kit%", kitName)));
            }
        }
    }

    @Override
    public void saveAll() {
        try {
            configManager.saveConfig("data/premadekit.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save premade kits file", e);
        }
    }
}
