package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.hooks.WorldGuardHook;
import dev.darkxx.kitsx.menus.KitsMenu;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.command.XyrisCommand;
import dev.darkxx.utils.text.color.ColorizeText;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KitCommand extends XyrisCommand<KitsX> {

    public KitCommand(KitsX plugin) {
        super(plugin, "kitsx", "kit");
        setAliases("k");
        setUsage("");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player player) {

            if (WorldGuardHook.get().isEnabled()) {
                if (BlacklistedRegion.isInBlacklistedRegion(player)) {
                    String cannotUseHere = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.blacklisted-region"));
                    player.sendMessage(ColorizeText.hex(cannotUseHere));
                    return true;
                }
            }

            KitsMenu.openKitMenu(player).open(player);
            return true;
        }
        return false;
    }
}