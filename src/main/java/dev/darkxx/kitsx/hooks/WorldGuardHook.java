package dev.darkxx.kitsx.hooks;

import dev.darkxx.kitsx.KitsX;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class WorldGuardHook {

    public void of() {
        Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin != null) {
            FileConfiguration config = KitsX.getInstance().getConfig();
            config.addDefault("blacklisted-regions", new String[]{"swordffa", "netheritepotion"});
            config.options().copyDefaults(true);
            KitsX.getInstance().saveConfig();
        }
    }

    public boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
    }

    public static WorldGuardHook get() {
        return new WorldGuardHook();
    }
}
