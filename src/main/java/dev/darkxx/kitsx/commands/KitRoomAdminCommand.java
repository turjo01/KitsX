package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.menus.KitRoomAdmin;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitRoomAdminCommand implements CommandExecutor, TabCompleter {

    private static final List<String> CATEGORIES = Arrays.asList("CRYSTAL_PVP", "POTIONS", "BOWS_ARROWS", "MISC");
    private static final List<String> ACTIONS = Arrays.asList("add", "clear");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 2 && player.hasPermission("kitsx.admin")) {
            sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitroomadmin <category> <add/clear>"));
            return false;
        }

        String category = args[0].toUpperCase();
        String action = args[1].toLowerCase();

        if (CATEGORIES.contains(category) && ACTIONS.contains(action)) {
            if ("add".equals(action)) {
                KitRoomAdmin.openAdminKitRoom(player, category);
            } else if ("clear".equals(action)) {
                Main.getKitRoomUtil().delete(category);
                player.sendMessage(ColorizeText.hex("&#7cff6eKit room category " + category + " cleared."));
            }
            return true;
        } else {
            sender.sendMessage(ColorizeText.hex("&#ffa6a6Invalid category or action."));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return CATEGORIES;
        } else if (args.length == 2) {
            return ACTIONS;
        }
        return new ArrayList<>();
    }
}
