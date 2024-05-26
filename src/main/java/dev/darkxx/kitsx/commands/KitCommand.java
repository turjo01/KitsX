package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.menus.KitsMenu;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player player) {

            if (BlacklistedRegion.isInBlacklistedRegion(player)) {
                String cannotUseHere = Objects.requireNonNull(Main.getInstance().getConfig().getString("messages.blacklisted-region"));
                player.sendMessage(ColorizeText.hex(cannotUseHere));
                return true;
            }

                KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
            return true;
        }
        return false;
    }
}