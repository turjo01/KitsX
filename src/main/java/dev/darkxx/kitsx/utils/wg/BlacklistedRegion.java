package dev.darkxx.kitsx.utils.wg;

import dev.darkxx.utils.worldguard.WorldGuardUtils;
import dev.darkxx.kitsx.Main;
import org.bukkit.entity.Player;

import java.util.List;

public class BlacklistedRegion {

    public static boolean isInBlacklistedRegion(Player player) {
        List<String> blacklistedRegions = Main.getInstance().getConfig().getStringList("blacklisted-regions");

        for (String region : blacklistedRegions) {
            if (WorldGuardUtils.isinRegion(player, region)) {
                return true;
            }
        }

        return false;
    }
}