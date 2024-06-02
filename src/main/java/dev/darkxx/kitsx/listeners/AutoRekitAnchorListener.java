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

package dev.darkxx.kitsx.listeners;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.utils.event.crystal.PlayerAnchorDeathEvent;
import dev.darkxx.utils.scheduler.Schedulers;
import dev.darkxx.utils.server.Servers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRekitAnchorListener implements Listener {

    @EventHandler
    public void onAnchorDeath(PlayerAnchorDeathEvent e) {
        Schedulers.sync().execute(task -> {
            Player killer = e.attacker().getPlayer();

            assert killer != null;
            if (KitsX.getAutoRekitUtil().isEnabled(killer)) {
                AutoRekitListener.load(killer);
            }
        }, 5);
    }

    public void register(JavaPlugin plugin) {
        HandlerList.unregisterAll(this);
        Servers.server().getPluginManager().registerEvents(this, plugin);
    }
}