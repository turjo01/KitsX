package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.kitsx.utils.menu.ItemBuilderGUI;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class EnderChestEditor extends GuiBuilder {
    public EnderChestEditor() {
        super(36);
    }

    public static void openEnderChestEditor(Player player, String kitName) {
        GuiBuilder inventory = new GuiBuilder(36, "Ender Chest - " + kitName);

        for (int i = 1; i <= 9; i++) {
            int slot = i + 26;
            ItemStack filter = new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)
                    .name(" ")
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build();
            inventory.setItem(slot, filter);
        }

        Main.getEnderChestUtil().set(player, kitName, inventory);

        ItemStack save = new ItemBuilderGUI(Material.LIME_DYE)
                .name(ColorizeText.mm("<green>Save"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(30, save, p -> {
            Main.getEnderChestUtil().save(player, kitName);
            KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
        });

        ItemStack reset = new ItemBuilderGUI(Material.RED_DYE)
                .name(ColorizeText.mm("<red>Reset"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(32, reset, p -> {
            for (int i1 = 0; i1 <= 26; i1++) {
                inventory.setItem(i1, new ItemStack(Material.AIR));
            }
        });

        ItemStack back = new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)
                .name(ColorizeText.mm("<#ffa6a6>Back"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(31, back, p -> {
            KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
        });

        inventory.addClickHandler(event -> {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 26) {
                event.setCancelled(false);
            }
        });

        inventory.open(player);
    }
}