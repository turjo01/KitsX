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

package dev.darkxx.kitsx.utils.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MenuConfig {

    private final FileConfiguration config;

    public MenuConfig(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public static void of(Plugin plugin) {
        new MenuConfig(plugin, "menus/kits-menu.yml");
        new MenuConfig(plugin, "menus/kiteditor-menu.yml");
        new MenuConfig(plugin, "menus/enderchest-editor-menu.yml");
        new MenuConfig(plugin, "menus/autorekit-menu.yml");
        new MenuConfig(plugin, "menus/premade-kit-menu.yml");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}