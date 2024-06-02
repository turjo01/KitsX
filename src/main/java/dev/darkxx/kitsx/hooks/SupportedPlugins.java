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

package dev.darkxx.kitsx.hooks;

import dev.darkxx.kitsx.hooks.skript.SkriptHook;
import dev.darkxx.kitsx.hooks.worldguard.WorldGuardHook;

public enum SupportedPlugins {

    WORLD_GUARD("WorldGuard") {
        @Override
        public void hook() {
            WorldGuardHook.get().of();
        }
    },

    SKRIPT("Skript") {
        @Override
        public void hook() {
            SkriptHook.get().of();
        }
    };

    private final String pluginName;

    SupportedPlugins(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getName() {
        return pluginName;
    }

    public abstract void hook();
}
