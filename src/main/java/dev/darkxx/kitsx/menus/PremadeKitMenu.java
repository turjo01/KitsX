// TODO: make this menu configurable via the config

package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.kitsx.utils.menu.ItemBuilderGUI;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class PremadeKitMenu extends GuiBuilder {

    public PremadeKitMenu() {
        super(54);
    }

    public static GuiBuilder createGui(Player player) {
        GuiBuilder inventory = new GuiBuilder(54, "Premade Kit");

        Main.getPremadeKitUtil().set(inventory);

        for (int slot = 41; slot <= 53; slot++) {
            ItemStack filter = new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)
                    .name(" ")
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build();
            inventory.setItem(slot, filter);
        }

        ItemStack back = new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)
                .name(ColorizeText.mm("<#ffa6a6>Back"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(53, back, p -> {
            KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
        });

        return inventory;
    }
}