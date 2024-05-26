package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.kitsx.utils.menu.ItemBuilderGUI;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KitEditorMenu extends GuiBuilder {

    public KitEditorMenu() {
        super(54);
    }

    public static void openKitEditor(Player player, String kitName) {
        GuiBuilder inventory = new GuiBuilder(54, "Editing " + kitName);

        for (int i = 1; i <= 4; i++) {
            int slot = i + 40;
            ItemStack filter = new ItemBuilderGUI(Material.BLACK_STAINED_GLASS_PANE)
                    .name(" ")
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build();
            inventory.setItem(slot, filter);
            inventory.setItem(45, filter);
            inventory.setItem(47, filter);
            inventory.setItem(48, filter);
            inventory.setItem(51, filter);
            inventory.setItem(53, filter);
        }

        Main.getKitUtil().set(player, kitName, inventory);

        ItemStack save = new ItemBuilderGUI(Material.LIME_DYE)
                .name(ColorizeText.mm("<green>Save"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(46, save, p -> {
            Main.getKitUtil().save(player, kitName);
            KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
        });

        ItemStack reset = new ItemBuilderGUI(Material.RED_DYE)
                .name(ColorizeText.mm("<red>Reset"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(48, reset, p -> {
            for (int i1 = 0; i1 <= 40; i1++) {
                inventory.setItem(i1, new ItemStack(Material.AIR));
            }
        });

        ItemStack importInventory = new ItemBuilderGUI(Material.CHEST)
                .name(ColorizeText.mm("<green>Import Inventory"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(50, importInventory, p -> {
            Main.getKitUtil().importInventory(player, inventory);
        });

        ItemStack premadeKit = new ItemBuilderGUI(Material.NETHERITE_CHESTPLATE)
                .name(ColorizeText.mm("<blue>Premade Kit"))
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
        inventory.setItem(52, premadeKit, p -> {
            Main.getKitUtil().importInventory(player, inventory);
        });

        ItemStack back = new ItemBuilderGUI(Material.RED_STAINED_GLASS_PANE)
                .name(ColorizeText.mm("<#ffa6a6>Back"))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();
        inventory.setItem(49, back, p -> {
            KitsMenu.openKitMenu(player, Main.getInstance()).open(player);
        });

        inventory.addClickHandler(event -> {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 40) {
                event.setCancelled(false);
            }
        });

        inventory.open(player);
    }
}