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

// TODO: make this menu configurable via the config

package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.events.KitRoomOpenEvent;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.menu.xmenu.ItemBuilderGUI;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitRoomMenu extends GuiBuilder {

    public KitRoomMenu() {
        super(54);
    }

    @SuppressWarnings("deprecation")
    public static GuiBuilder openKitRoom(Player player) {
        GuiBuilder inventory = new GuiBuilder(54, "Virtual Kit Room");

        inventory.addOpenHandler(event -> {
            if (KitsX.getInstance().getConfig().getBoolean("broadcast.kitroom-open", true)) {
                String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.kitroom-open-message");
                assert bcastLoaded != null;
                bcastLoaded = bcastLoaded.replace("%player%", player.getName());
                Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));

                Bukkit.getServer().getPluginManager().callEvent(new KitRoomOpenEvent(player));
            }
        });

        for (int i = 1; i <= 9; i++) {
            int slot = i + 44;
            ItemStack filter = new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)
                    .name(" ")
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build();
            inventory.setItem(slot, filter);
        }

        KitsX.getKitRoomUtil().load(inventory, "CRYSTAL_PVP");

        ItemStack crystalpvp = new ItemBuilderGUI(Material.NETHERITE_SWORD)
                .name(ColorizeText.hex("&#ff2e2eCrystal PvP"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.MENDING, 1)
                .build();
        inventory.setItem(47, crystalpvp, p -> KitsX.getKitRoomUtil().load(inventory, "CRYSTAL_PVP"));

        ItemStack potions = new ItemBuilderGUI(Material.BREWING_STAND)
                .name(ColorizeText.hex("&#ff2e2ePotions"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(48, potions, p -> KitsX.getKitRoomUtil().load(inventory, "POTIONS"));

        ItemStack bowsArrows = new ItemBuilderGUI(Material.SPECTRAL_ARROW)
                .name(ColorizeText.hex("&#ff2e2eBows & Arrows"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(50, bowsArrows, p -> KitsX.getKitRoomUtil().load(inventory, "BOWS_ARROWS"));

        ItemStack misc = new ItemBuilderGUI(Material.ENDER_PEARL)
                .name(ColorizeText.hex("&#ff2e2eMisc"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(51, misc, p -> KitsX.getKitRoomUtil().load(inventory, "MISC"));

        ItemStack back = new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)
                .name(ColorizeText.hex("&#ffa6a6Back"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(49, back, p -> KitsMenu.openKitMenu(player).open(player));

        inventory.addClickHandler(event -> {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 44) {
                event.setCancelled(false);
            }
        });

        return inventory;
    }
}