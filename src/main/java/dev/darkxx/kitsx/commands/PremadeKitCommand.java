package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PremadeKitCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (BlacklistedRegion.isInBlacklistedRegion(player)) {
            String cannotUseHere = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.blacklisted-region"));
            player.sendMessage(ColorizeText.hex(cannotUseHere));
            return true;
        }

        if (args.length == 0) {
            Main.getPremadeKitUtil().load(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
            if (player.hasPermission("kitsx.admin")) {
                Main.getPremadeKitUtil().save(player);
            } else {
                String noPerm = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.no-permission"));
                player.sendMessage(ColorizeText.hex(noPerm));
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                if (player.hasPermission("kitsx.admin")) {
                    completions.add("save");
                }
                return completions;
            }
        }
        return null;
    }
}
