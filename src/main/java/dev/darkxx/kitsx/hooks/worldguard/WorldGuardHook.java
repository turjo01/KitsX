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

package dev.darkxx.kitsx.hooks.worldguard;

import dev.darkxx.kitsx.KitsX;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class WorldGuardHook {

    @Contract(value = " -> new", pure = true)
    public static @NotNull WorldGuardHook get() {
        return new WorldGuardHook();
    }

    public void of() {
        Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin != null) {
            FileConfiguration config = KitsX.getInstance().getConfig();
            config.addDefault("blacklisted_regions", new String[]{"swordffa", "netheritepotion"});
            config.options().copyDefaults(true);
            KitsX.getInstance().saveConfig();
        }
    }

    public boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }
}
