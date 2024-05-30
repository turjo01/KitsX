package dev.darkxx.kitsx.utils.wg;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.utils.worldguard.WorldGuardUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class BlacklistedRegion {

    public static boolean isInBlacklistedRegion(Player player) {
        List<String> blacklistedRegions = KitsX.getInstance().getConfig().getStringList("blacklisted-regions");

        for (String region : blacklistedRegions) {
            if (WorldGuardUtil.isinRegion(player, region)) {
                return true;
            }
        }

        return false;
    }
}