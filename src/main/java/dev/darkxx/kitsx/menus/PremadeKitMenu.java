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

package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.utils.config.MenuConfig;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.menu.xmenu.ItemBuilderGUI;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class PremadeKitMenu extends GuiBuilder {

    private static final KitsX PLUGIN = KitsX.getInstance();
    private static final MenuConfig CONFIG = new MenuConfig(PLUGIN, "menus/premadekit-menu.yml");

    public PremadeKitMenu() {
        super(CONFIG.getConfig().getInt("premade-kit.size"));
    }

    public static GuiBuilder createGui() {
        GuiBuilder inventory = new GuiBuilder(CONFIG.getConfig().getInt("premade-kit.size"), ColorizeText.hex(Objects.requireNonNull(CONFIG.getConfig().getString("premade-kit.title"))));

        addItems(inventory, "premade-kit.filter");
        addItems(inventory, "premade-kit.back");

        return inventory;
    }

    private static void addItems(GuiBuilder inventory, String configPath) {
        ConfigurationSection section = CONFIG.getConfig().getConfigurationSection(configPath);
        if (section != null) {
            Material material = Material.matchMaterial(section.getString("material", "STONE"));
            String name = ColorizeText.hex(section.getString("name", ""));
            List<Integer> slots = section.getIntegerList("slots");
            List<String> flags = section.getStringList("flags");

            ItemStack item = new ItemBuilderGUI(material)
                    .name(name)
                    .flags(getItemFlags(flags))
                    .build();

            for (int slot : slots) {
                inventory.setItem(slot, item);
            }
        }
    }

    private static ItemFlag[] getItemFlags(List<String> flags) {
        return flags.stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new);
    }
}