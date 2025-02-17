/*
 * This file is part of KitsX
 *
 * KitsX
 * Copyright (c) 2024 XyrisPlugins
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
import dev.darkxx.kitsx.api.KitsAPI;
import dev.darkxx.kitsx.api.events.KitLoadEvent;
import dev.darkxx.kitsx.api.events.KitSaveEvent;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KitUtil implements KitsAPI {

    private static ConfigManager configManager;
    private final Logger logger = Logger.getLogger(KitUtil.class.getName());
    private final Map<String, Long> lastBroadcastTime = new HashMap<>();

    public KitUtil(ConfigManager configManager) {
        KitUtil.configManager = configManager;
    }

    public static void of(JavaPlugin plugin) {
        configManager = ConfigManager.get(plugin);
        configManager.create("data/kits.yml");
    }

    @Override
    public void save(@NotNull Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        ItemStack[] inventoryContents = new ItemStack[36];
        for (int i = 0; i < 36; ++i) {
            inventoryContents[i] = player.getOpenInventory().getTopInventory().getItem(i);
        }

        ItemStack[] armorContents = new ItemStack[4];
        for (int i = 36; i < 40; ++i) {
            armorContents[i - 36] = player.getOpenInventory().getTopInventory().getItem(i);
        }

        ItemStack offhandItem = player.getOpenInventory().getTopInventory().getItem(40);

        KitSaveEvent event = new KitSaveEvent(player, kitName, inventoryContents, armorContents, offhandItem);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (exists(player, kitName)) {
                    delete(player, kitName);
                }
                Map<String, Object> kitData = new HashMap<>();

                for (int i = 0; i < 36; ++i) {
                    kitData.put(playerName + "." + kitName + ".inventory." + i, inventoryContents[i]);
                }
                for (int i = 0; i < 4; ++i) {
                    kitData.put(playerName + "." + kitName + ".armor." + i, armorContents[i]);
                }

                kitData.put(playerName + "." + kitName + ".offhand", offhandItem);

                for (Map.Entry<String, Object> entry : kitData.entrySet()) {
                    configManager.set("data/kits.yml", entry.getKey(), entry.getValue());
                }

                try {
                    configManager.saveConfig("data/kits.yml");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            String kitSaved = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.kit_saved"))
                                    .replace("%kit%", kitName);
                            player.sendMessage(ColorizeText.hex(kitSaved));
                        }
                    }.runTask(KitsX.getInstance());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Failed to save kit: " + kitName, e);
                }
            }
        }.runTaskAsynchronously(KitsX.getInstance());
    }

    @Override
    @SuppressWarnings("deprecation")
    public void load(@NotNull Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        KitLoadEvent event = new KitLoadEvent(player, kitName);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if (configManager.contains("data/kits.yml", playerName + "." + kitName)) {
                for (int i = 0; i < 36; ++i) {
                    ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".inventory." + i);
                    player.getInventory().setItem(i, item);
                }

                for (int i = 0; i < 4; ++i) {
                    ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".armor." + i);
                    player.getInventory().setItem(36 + i, item);
                }

                ItemStack offhandItem = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".offhand");
                player.getInventory().setItemInOffHand(offhandItem);

                long currentTime = System.currentTimeMillis();
                long lastTime = lastBroadcastTime.getOrDefault(playerName, 0L);
                long delayMillis = KitsX.getInstance().getConfig().getInt("broadcast.kit_load_message_delay", 10) * 50L;

                if (KitsX.getInstance().getConfig().getBoolean("broadcast.kit_load", true) && (currentTime - lastTime > delayMillis)) {
                    String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.kit_load_message");
                    if (bcastLoaded != null) {
                        bcastLoaded = bcastLoaded.replace("%player%", player.getName()).replace("%kit%", kitName);
                        Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                    }

                    lastBroadcastTime.put(playerName, currentTime);
                }

                String kitLoaded = KitsX.getInstance().getConfig().getString("messages.kit_loaded");
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
    public void set(@NotNull Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getUniqueId().toString();
        if (configManager.contains("data/kits.yml", playerName + "." + kitName)) {
            for (int i = 0; i < 36; ++i) {
                ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".inventory." + i);
                inventory.setItem(i, item);
            }

            for (int i = 0; i < 4; ++i) {
                ItemStack item = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".armor." + i);
                inventory.setItem(36 + i, item);
            }

            ItemStack offhandItem = configManager.getConfig("data/kits.yml").getItemStack(playerName + "." + kitName + ".offhand");
            inventory.setItem(40, offhandItem);
        }
    }

    @Override
    public void importInventory(@NotNull Player player, GuiBuilder inventory) {
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
    public void delete(@NotNull Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        configManager.set("data/kits.yml", playerName + "." + kitName, null);

        try {
            configManager.saveConfig("data/kits.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete kit: " + kitName, e);
        }
    }

    @Override
    public boolean exists(@NotNull Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        return configManager.contains("data/kits.yml", playerName + "." + kitName);
    }

    @Override
    public void saveAll() {
        try {
            configManager.saveConfig("data/kits.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save kits file", e);
        }
    }
}
