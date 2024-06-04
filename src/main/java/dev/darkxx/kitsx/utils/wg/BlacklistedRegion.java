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

package dev.darkxx.kitsx.utils.wg;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.utils.worldguard.WorldGuardUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class BlacklistedRegion {

    public static boolean isInBlacklistedRegion(Player player) {
        List<String> blacklistedRegions = KitsX.getInstance().getConfig().getStringList("blacklisted_regions");

        for (String region : blacklistedRegions) {
            if (WorldGuardUtil.isinRegion(player, region)) {
                return true;
            }
        }

        return false;
    }
}