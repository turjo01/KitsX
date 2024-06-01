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
import dev.darkxx.kitsx.api.EnderChestAPI;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnderChestUtil implements EnderChestAPI {

    private static ConfigManager configManager;
    private final Logger logger = Logger.getLogger(EnderChestUtil.class.getName());

    public EnderChestUtil(ConfigManager configManager) {
        EnderChestUtil.configManager = configManager;
    }

    public static void of(JavaPlugin plugin) {
        configManager = ConfigManager.get(plugin);
        configManager.create("data/enderchest.yml");
    }

    @Override
    public void save(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        if (exists(player, kitName)) {
            delete(player, kitName);
        }

        for (int i = 0; i < 27; i++) {
            ItemStack item = player.getOpenInventory().getTopInventory().getItem(i);
            if (item != null) {
                configManager.set("data/enderchest.yml", playerName + "." + kitName + ".enderchest." + i, item);
            }
        }

        try {
            configManager.saveConfig("data/enderchest.yml");
            String enderchestSaved = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.enderchest-saved")).replace("%kit%", kitName);
            player.sendMessage(ColorizeText.hex(enderchestSaved));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save ender chest", e);
        }
    }

    @Override
    public void load(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        player.getEnderChest().clear();

        ConfigurationSection section = configManager.getConfig("data/enderchest.yml").getConfigurationSection(playerName + "." + kitName + ".enderchest");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ItemStack item = section.getItemStack(key);
                if (item != null) {
                    player.getEnderChest().setItem(Integer.parseInt(key), item);
                }
            }
        }
    }

    @Override
    public void set(Player player, String kitName, GuiBuilder inventory) {
        String playerName = player.getUniqueId().toString();

        ConfigurationSection section = configManager.getConfig("data/enderchest.yml").getConfigurationSection(playerName + "." + kitName + ".enderchest");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                ItemStack item = section.getItemStack(key);
                if (item != null) {
                    inventory.setItem(Integer.parseInt(key), item);
                }
            }
        }
    }

    @Override
    public void delete(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();

        configManager.set("data/enderchest.yml", playerName + "." + kitName + ".enderchest", null);

        try {
            configManager.saveConfig("data/enderchest.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save ender chest", e);
        }
    }

    @Override
    public boolean exists(Player player, String kitName) {
        String playerName = player.getUniqueId().toString();
        return configManager.contains("data/enderchest.yml", playerName + "." + kitName + ".enderchest");
    }

    @Override
    public void saveAll() {
        try {
            configManager.saveConfig("data/enderchest.yml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save ender chest", e);
        }
    }
}
