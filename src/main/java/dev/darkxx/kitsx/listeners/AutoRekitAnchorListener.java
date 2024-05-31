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