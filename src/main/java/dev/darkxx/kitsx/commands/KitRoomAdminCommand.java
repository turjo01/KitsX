package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.menus.KitRoomAdmin;
import dev.darkxx.utils.command.XyrisCommand;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class KitRoomAdminCommand extends XyrisCommand<KitsX> {

    public KitRoomAdminCommand(KitsX plugin) {
        super(plugin, "kitsx", "kitroomadmin");
        addTabbComplete(0, "add");
        addTabbComplete(0, "clear");
        addTabbComplete(1, "CRYSTAL_PVP");
        addTabbComplete(1, "POTIONS");
        addTabbComplete(1, "BOWS_ARROWS");
        addTabbComplete(1, "MISC");
        setPermission("kitsx.admin");
        setUsage("");
        registerCommand();
    }

    private static final List<String> CATEGORIES = Arrays.asList("CRYSTAL_PVP", "POTIONS", "BOWS_ARROWS", "MISC");
    private static final List<String> ACTIONS = Arrays.asList("add", "clear");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 2 && player.hasPermission("kitsx.admin")) {
            sender.sendMessage(ColorizeText.hex("&#ffa6a6Usage: /kitroomadmin <add/clear> <category>"));
            return false;
        }

        String action = args[0].toLowerCase();
        String category = args[1].toUpperCase();

        if (CATEGORIES.contains(category) && ACTIONS.contains(action)) {
            if ("add".equals(action)) {
                KitRoomAdmin.openAdminKitRoom(player, category);
            } else if ("clear".equals(action)) {
                KitsX.getKitRoomUtil().delete(category);
                player.sendMessage(ColorizeText.hex("&#7cff6eKit room category " + category + " cleared."));
            }
            return true;
        } else {
            sender.sendMessage(ColorizeText.hex("&#ffa6a6Invalid category or action."));
            return false;
        }
    }
}