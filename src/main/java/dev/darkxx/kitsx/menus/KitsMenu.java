package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.kitsx.utils.menu.ItemBuilderGUI;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KitsMenu extends GuiBuilder {
    private static final Main PLUGIN = Main.getInstance();
    private static final int INVENTORY_SIZE = PLUGIN.getConfig().getInt("menus.kitsMenu.size");

    public KitsMenu(Main main) {
        super(INVENTORY_SIZE);
    }

    public static GuiBuilder openKitMenu(Player player, Plugin plugin) {
        String kitsTitle = PLUGIN.getConfig().getString("menus.kitsMenu.title");
        assert kitsTitle != null;

        GuiBuilder inventory = new GuiBuilder(INVENTORY_SIZE, ColorizeText.hex(kitsTitle));

        if (PLUGIN.getConfig().getBoolean("menus.kitsMenu.filter", true)) {
            addFilterItems(inventory);
        }

        addKitItems(inventory, player);
        addEnderChestItems(inventory, player);
        addKitRoomItem(inventory, player);
        addClearInventoryItem(inventory, player);
        addPremadeKitItem(inventory, player);

        return inventory;
    }

    private static void addFilterItems(GuiBuilder inventory) {
        String filterMaterial = PLUGIN.getConfig().getString("menus.kitsMenu.filter-material");
        ItemStack filterItem = new ItemBuilderGUI(Material.valueOf(filterMaterial))
                .name(" ")
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();

        inventory.fillEmptySlots(filterItem);
    }

    private static void addKitItems(GuiBuilder inventory, Player player) {
        addItemGroup(inventory, "kitsMenu.kits", Material.DIAMOND_SWORD, player);
    }

    private static void addEnderChestItems(GuiBuilder inventory, Player player) {
        addItemGroup(inventory, "kitsMenu.enderchests", Material.ENDER_CHEST, player);
    }

    private static void addItemGroup(GuiBuilder inventory, String configPath, Material defaultMaterial, Player player) {
        for (int i = 1; i <= 7; i++) {
            int slot = i + (configPath.equals("kitsMenu.kits") ? 9 : 18);
            int kitNumber = i;

            String itemMaterial = PLUGIN.getConfig().getString("menus." + configPath + ".material", defaultMaterial.name());
            String itemName = PLUGIN.getConfig().getString("menus." + configPath + ".name", "").replace("%kit%", String.valueOf(kitNumber));
            List<String> loreList = PLUGIN.getConfig().getStringList("menus." + configPath + ".lore");
            List<String> finalLore = new ArrayList<>();
            for (String lore : loreList) {
                finalLore.add(ColorizeText.hex(lore.replace("%i%", String.valueOf(kitNumber))));
            }

            ItemStack item = new ItemBuilderGUI(Material.valueOf(itemMaterial))
                    .name(ColorizeText.hex(itemName))
                    .lore(finalLore.toArray(new String[0]))
                    .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                    .build();

            inventory.setItem(slot, item, e -> {
                if (e.isRightClick()) {
                    if (configPath.equals("kitsMenu.kits")) {
                        KitEditorMenu.openKitEditor(player, "Kit " + kitNumber);
                    } else {
                        EnderChestEditor.openEnderChestEditor(player, "Kit " + kitNumber);
                    }
                } else if (e.isLeftClick()) {
                    Main.getKitUtil().load(player, "Kit " + kitNumber);
                }
            });
        }
    }

    private static void addKitRoomItem(GuiBuilder inventory, Player player) {
        addItemFromConfig(inventory, "kitroom", Material.CHEST, player);
    }

    private static void addClearInventoryItem(GuiBuilder inventory, Player player) {
        addItemFromConfig(inventory, "clearinv", Material.BARRIER, player);
    }

    private static void addPremadeKitItem(GuiBuilder inventory, Player player) {
        addItemFromConfig(inventory, "premadekit", Material.ENCHANTED_BOOK, player);
    }

    private static void addItemFromConfig(GuiBuilder inventory, String configName, Material defaultMaterial, Player player) {
        String itemMaterial = PLUGIN.getConfig().getString("menus.kitsMenu." + configName + ".material", defaultMaterial.name());
        String itemName = PLUGIN.getConfig().getString("menus.kitsMenu." + configName + ".name", "");
        int itemSlot = PLUGIN.getConfig().getInt("menus.kitsMenu." + configName + ".slot");
        List<String> loreList = PLUGIN.getConfig().getStringList("menus.kitsMenu." + configName + ".lore");
        List<String> finalLore = new ArrayList<>();
        for (String lore : loreList) {
            finalLore.add(ColorizeText.hex(lore));
        }

        ItemStack item = new ItemBuilderGUI(Material.valueOf(itemMaterial))
                .name(ColorizeText.hex(itemName))
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS)
                .lore(finalLore.toArray(new String[0]))
                .build();

        inventory.setItem(itemSlot, item, p -> {
            switch (configName) {
                case "kitroom":
                    KitRoomMenu.openKitRoom(player).open(player);
                    break;
                case "clearinv":
                    player.getInventory().clear();
                    break;
                case "premadekit":
                    if (p.isRightClick()) {
                        PremadeKitMenu.createGui(player).open(player);
                    } else if (p.isLeftClick()) {
                        Main.getPremadeKitUtil().load(player);
                    }
                    break;
            }
        });
    }
}
