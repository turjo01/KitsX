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

package dev.darkxx.kitsx.commands.admin;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.menus.admin.KitViewMenu;
import dev.darkxx.utils.command.XyrisCommand;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KitsAdminCommand extends XyrisCommand<KitsX> {

    public KitsAdminCommand(KitsX plugin) {
        super(plugin, "kitsx", "kitsadmin");
        addTabbComplete(0, "view");
        addTabbComplete(0, "clear");
        setUsage("");
        setPermission("kitsx.admin");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player executor)) {
            return true;
        }

        if (args.length < 2 || !(args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("clear"))) {
            sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin <view/clear> <player> [kit name]"));
            return true;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);

        if (!targetPlayer.hasPlayedBefore() && !targetPlayer.isOnline()) {
            executor.sendMessage(ColorizeText.hex("&#ffa6a6Player not found."));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "view":
                if (args.length != 2) {
                    sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin view <player>"));
                    return true;
                }
                KitViewMenu.openKitSelectMenu(executor, targetPlayer.getName());
                break;
            case "clear":
                if (args.length < 3) {
                    sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitsadmin clear <player> <kit name>"));
                    return true;
                }
                String kitName = Stream.of(args).skip(2).collect(Collectors.joining(" "));
                KitsX.getKitUtil().delete((Player) targetPlayer, kitName);
                KitsX.getEnderChestUtil().delete((Player) targetPlayer, kitName);
                executor.sendMessage(ColorizeText.hex("&#7cff6e" + kitName + " has been cleared for player " + targetPlayer.getName() + "."));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(CommandSender sender, String alias, String @NotNull [] args) {
        if (args.length == 2 && (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("clear"))) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("clear")) {
            int numKits = KitsX.getInstance().getConfig().getInt("kits");
            return IntStream.rangeClosed(1, numKits)
                    .mapToObj(i -> "Kit " + i)
                    .collect(Collectors.toList());
        }
        return super.tabComplete(sender, alias, args);
    }
}
