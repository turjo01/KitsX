package dev.darkxx.kitsx.commands.load;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Kit7 implements CommandExecutor {

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

        Main.getKitUtil().load(player, "Kit 7");

        return true;
    }
}