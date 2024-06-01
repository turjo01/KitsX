/*
 * This file is part of KitsX
 *
 * KitsX
 * Copyright (c) 2024 XyrisPlugins Team
 *
 * KitsX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KitsX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PremadeKitUtil implements PremadeKitAPI {

    private static ConfigManager configManager;
    private final Logger logger = Logger.getLogger(PremadeKitUtil.class.getName());

    public PremadeKitUtil(ConfigManager configManager) {
        PremadeKitUtil.configManager = configManager;
    }

    public static void of(KitsX plugin) {
        configManager = ConfigManager.get(plugin);
        configManager.create("data/premadekit.yml");
    }

    @Override
    public void save(Player player) {
        try {
            Inventory inventory = player.getInventory();
            List<ItemStack> inventoryList = Arrays.asList(inventory.getContents());
            List<ItemStack> armorList = Arrays.asList(player.getInventory().getArmorContents());

            ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").createSection("premadekit");
            premadeKitSection.set("inventory", inventoryList);
            premadeKitSection.set("armor", armorList);
            premadeKitSection.set("offhand", player.getInventory().getItemInOffHand());

            configManager.saveConfig("data/premadekit.yml");

            String saved = KitsX.getInstance().getConfig().getString("messages.premade-kit-saved");
            if (saved != null) {
                player.sendMessage(ColorizeText.hex(saved));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save premade kits file", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(GuiBuilder inventory) {
        ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("premadekit");
        if (premadeKitSection != null) {
            List<ItemStack> inventoryItems = (List<ItemStack>) premadeKitSection.get("inventory");
            List<ItemStack> armorItems = (List<ItemStack>) premadeKitSection.get("armor");
            ItemStack offhandItem = premadeKitSection.getItemStack("offhand");

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
    public void load(Player player) {
        ConfigurationSection premadeKitSection = configManager.getConfig("data/premadekit.yml").getConfigurationSection("premadekit");
        if (premadeKitSection != null) {
            player.getInventory().clear();
            player.getInventory().setContents(((List<ItemStack>) Objects.requireNonNull(premadeKitSection.get("inventory"))).toArray(new ItemStack[0]));
            player.getInventory().setArmorContents(((List<ItemStack>) Objects.requireNonNull(premadeKitSection.get("armor"))).toArray(new ItemStack[0]));
            ItemStack offhandItem = premadeKitSection.getItemStack("offhand");
            if (offhandItem != null) {
                player.getInventory().setItemInOffHand(offhandItem);
            }

            if (KitsX.getInstance().getConfig().getBoolean("broadcast.premadekit-load", true)) {
                String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.premadekit-load-message");
                if (bcastLoaded != null) {
                    bcastLoaded = bcastLoaded.replace("%player%", player.getName());
                    Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                }
            }

            String loaded = KitsX.getInstance().getConfig().getString("messages.premade-kit-loaded");
            if (loaded != null) {
                player.sendMessage(ColorizeText.hex(loaded));
            }

        } else {
            String empty = KitsX.getInstance().getConfig().getString("messages.premade-kit-empty");
            if (empty != null) {
                player.sendMessage(ColorizeText.hex(empty));
            }
        }
    }

    @Override
    public void saveAll() {
        try {
            configManager.saveConfig("data/premadekit.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save premadekits file", e);
        }
    }
}
