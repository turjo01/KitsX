package dev.darkxx.kitsx.hooks;

import dev.darkxx.kitsx.hooks.skript.SkriptHook;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HooksImpl {

    public static void of(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        ConsoleCommandSender log = Bukkit.getServer().getConsoleSender();

        if (pm.isPluginEnabled("WorldGuard")) {
            log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fFound &#ff2e2eWorldGuard&f, Hooking into it"));
            SkriptHook.get().of();
            log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fHooked into &#ff2e2eWorldGuard&f!"));
        }

        if (pm.isPluginEnabled("Skript")) {
            log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fFound &#ff2e2eSkript&f, Hooking into it"));
            SkriptHook.get().of();
            log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fHooked into &#ff2e2eSkript&f!"));
        }
    }
}