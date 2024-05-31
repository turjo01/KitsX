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

public class PremadeKitCommand extends XyrisCommand<KitsX> {

    public PremadeKitCommand(KitsX plugin) {
        super(plugin, "kitsx", "premadekit");
        addTabbComplete(0, "kitsx.admin", (String[]) null, "save");
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

        if (args.length == 0) {
            KitsX.getPremadeKitUtil().load(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
            if (player.hasPermission("kitsx.admin")) {
                KitsX.getPremadeKitUtil().save(player);
            } else {
                String noPerm = Objects.requireNonNull(KitsX.getInstance().getConfig().getString("messages.no-permission"));
                player.sendMessage(ColorizeText.hex(noPerm));
            }
            return true;
        }
        return true;
    }
}