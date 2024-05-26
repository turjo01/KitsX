package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.Main;
import dev.darkxx.kitsx.menus.config.MenuConfig;
import dev.darkxx.kitsx.utils.menu.GuiBuilder;
import dev.darkxx.kitsx.utils.menu.ItemBuilderGUI;
import dev.darkxx.utils.text.ColorizeText;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class KitsMenu extends GuiBuilder {

    private static final Main PLUGIN = Main.getInstance();
    private static final MenuConfig CONFIG = new MenuConfig(PLUGIN, "menus/kits.yml");
    private static final Logger LOGGER = PLUGIN.getLogger();

    public KitsMenu(int size) {
        super(size);
    }

    public static GuiBuilder openKitMenu(Player player, Plugin plugin) {
        int inventorySize = CONFIG.getConfig().getInt("kits-menu.size", 54);
        String kitsTitle = CONFIG.getConfig().getString("kits-menu.title", "Kits");
        String inventoryTitle = ColorizeText.hex(kitsTitle);

        GuiBuilder inventory = new GuiBuilder(inventorySize, inventoryTitle);

        if (CONFIG.getConfig().getBoolean("kits-menu.filter", true)) {
            addFilter(inventory);
        }

        addKitItems(inventory, player);
        addEnderChestItems(inventory, player);
        addKitRoomItem(inventory, player);
        addClearInventoryItem(inventory, player);
        addPremadeKitItem(inventory, player);

        return inventory;
    }

    private static void addFilter(GuiBuilder inventory) {
        String filterMaterial = CONFIG.getConfig().getString("kits-menu.filter-material", "BLACK_STAINED_GLASS_PANE");
        ItemStack filterItem = new ItemBuilderGUI(Material.valueOf(filterMaterial))
                .name(" ")
                .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
                .build();

        inventory.fillEmptySlots(filterItem);
    }

    private static void addKitItems(GuiBuilder inventory, Player player) {
        addItemGroup(inventory, "kits-menu.kits", Material.END_CRYSTAL, player);
    }

    private static void addEnderChestItems(GuiBuilder inventory, Player player) {
        addItemGroup(inventory, "kits-menu.enderchests", Material.ENDER_CHEST, player);
    }

    private static void addItemGroup(GuiBuilder inventory, String configPath, Material defaultMaterial, Player player) {
        for (int i = 1; i <= 7; i++) {
            int slot = i + (configPath.equals("kits-menu.kits") ? 9 : 18);
            int kitNumber = i;

            String itemMaterial = CONFIG.getConfig().getString(configPath + ".material", defaultMaterial.name());
            String itemName = CONFIG.getConfig().getString(configPath + ".name", "").replace("%kit%", String.valueOf(kitNumber));
            List<String> loreList = CONFIG.getConfig().getStringList(configPath + ".lore");
            List<String> finalLore = new ArrayList<>();
            for (String lore : loreList) {
                finalLore.add(ColorizeText.hex(lore.replace("%i%", String.valueOf(kitNumber))));
            }
            List<String> flagList = CONFIG.getConfig().getStringList(configPath + ".flags");
            List<ItemFlag> flags = new ArrayList<>();
            for (String flag : flagList) {
                flags.add(ItemFlag.valueOf(flag));
            }

            ItemStack item = new ItemBuilderGUI(Material.valueOf(itemMaterial))
                    .name(ColorizeText.hex(itemName))
                    .lore(finalLore.toArray(new String[0]))
                    .flags(flags.toArray(new ItemFlag[0]))
                    .build();

            inventory.setItem(slot, item, e -> {
                if (e.isRightClick()) {
                    if (configPath.equals("kits-menu.kits")) {
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
        addItem(inventory, "kits-menu.kitroom", Material.CREEPER_BANNER_PATTERN, player);
    }

    private static void addClearInventoryItem(GuiBuilder inventory, Player player) {
        addItem(inventory, "kits-menu.clearinv", Material.RED_DYE, player);
    }

    private static void addPremadeKitItem(GuiBuilder inventory, Player player) {
        addItem(inventory, "kits-menu.premadekit", Material.NETHERITE_CHESTPLATE, player);
    }

    private static void addItem(GuiBuilder inventory, String configName, Material defaultMaterial, Player player) {
        String itemMaterial = CONFIG.getConfig().getString(configName + ".material", defaultMaterial.name());
        String itemName = CONFIG.getConfig().getString(configName + ".name", "");
        int itemSlot = CONFIG.getConfig().getInt(configName + ".slot");
        List<String> loreList = CONFIG.getConfig().getStringList(configName + ".lore");
        List<String> finalLore = new ArrayList<>();
        for (String lore : loreList) {
            finalLore.add(ColorizeText.hex(lore));
        }
        List<String> flagList = CONFIG.getConfig().getStringList(configName + ".flags");
        List<ItemFlag> flags = new ArrayList<>();
        for (String flag : flagList) {
            try {
                flags.add(ItemFlag.valueOf(flag));
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid item flag " + flag);
            }
        }
        List<Map<?, ?>> enchantmentList = CONFIG.getConfig().getMapList(configName + ".enchantments");
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        for (Map<?, ?> enchantmentMap : enchantmentList) {
            String type = (String) enchantmentMap.get("type");
            int level = (Integer) enchantmentMap.get("level");
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(type.toLowerCase()));
            if (enchantment != null) {
                enchantments.put(enchantment, level);
            } else {
                LOGGER.warning("Invalid enchantment type " + type);
            }
        }

        ItemStack item = new ItemBuilderGUI(Material.valueOf(itemMaterial))
                .name(ColorizeText.hex(itemName))
                .flags(flags.toArray(new ItemFlag[0]))
                .flags(ItemFlag.HIDE_ITEM_SPECIFICS)
                .lore(finalLore.toArray(new String[0]))
                .build();

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }

        inventory.setItem(itemSlot, item, p -> {
            switch (configName) {
                case "kits-menu.kitroom":
                    KitRoomMenu.openKitRoom(player).open(player);
                    break;
                case "kits-menu.clearinv":
                    player.getInventory().clear();
                    break;
                case "kits-menu.premadekit":
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