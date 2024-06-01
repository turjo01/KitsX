package dev.darkxx.kitsx.commands;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.hooks.WorldGuardHook;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.command.XyrisCommand;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class KitLoadCommand extends XyrisCommand<KitsX> {

    public KitLoadCommand(KitsX plugin, String name, int kit) {
        super(plugin, "kitsx", name);
        setAliases("k" + kit);
        setUsage("");
        registerCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (WorldGuardHook.get().isEnabled()) {
            if (BlacklistedRegion.isInBlacklistedRegion(player)) {
                String cannotUseHere = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.blacklisted-region"));
                player.sendMessage(ColorizeText.hex(cannotUseHere));
                return true;
            }
        }

        int kits = getPlugin().getConfig().getInt("kits", 7);

        String cmdName = command.getName();

        for (int i = 1; i <= kits; i++) {
            if (cmdName.equalsIgnoreCase("kit" + i)) {
                if (player.hasPermission("kitsx." + cmdName)) {
                    KitsX.getKitUtil().load(player, "Kit " + i);
                } else {
                    player.sendMessage(ColorizeText.hex("&#ffa6a6You don't have permission to use Kit " + i + "."));
                }
                return true;
            }
        }
        return false;
    }
}