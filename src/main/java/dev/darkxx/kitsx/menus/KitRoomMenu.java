package dev.darkxx.kitsx.menus;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.kitsx.api.events.KitRoomOpenEvent;
import dev.darkxx.kitsx.utils.config.MenuConfig;
import dev.darkxx.utils.menu.xmenu.GuiBuilder;
import dev.darkxx.utils.menu.xmenu.ItemBuilderGUI;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitRoomMenu extends GuiBuilder {

    private static final MenuConfig CONFIG = new MenuConfig(KitsX.getInstance(), "menus/kitroom_menu.yml");
    private static final Map<Player, String> currentCategoryMap = new HashMap<>();
    public static final Map<String, Long> lastBroadcastTime = new HashMap<>();

    public KitRoomMenu() {
        super(54);
    }

    public static @NotNull GuiBuilder openKitRoom(Player player) {
        GuiBuilder inventory = new GuiBuilder(54, CONFIG.getConfig().getString("kit_room.title", "Virtual Kit Room"));

        inventory.addOpenHandler(event -> {
            long currentTime = System.currentTimeMillis();
            String playerName = player.getUniqueId().toString();
            long lastTime = lastBroadcastTime.getOrDefault(playerName, 0L);
            long delayMillis = KitsX.getInstance().getConfig().getInt("broadcast.kitroom_open_message_delay", 10) * 50L;

            if (KitsX.getInstance().getConfig().getBoolean("broadcast.kitroom_open", true) && (currentTime - lastTime > delayMillis)) {
                String bcastLoaded = KitsX.getInstance().getConfig().getString("broadcast.kitroom_open_message");
                if (bcastLoaded != null) {
                    bcastLoaded = bcastLoaded.replace("%player%", player.getName());
                    Bukkit.broadcastMessage(ColorizeText.hex(bcastLoaded));
                    lastBroadcastTime.put(playerName, currentTime);
                    Bukkit.getServer().getPluginManager().callEvent(new KitRoomOpenEvent(player));
                   // KitsX.getKitRoomUtil().load(inventory, "CRYSTAL_PVP");
                }
            }
        });

        ConfigurationSection itemsSection = CONFIG.getConfig().getConfigurationSection("kit_room.items");
        KitsX.getKitRoomUtil().load(inventory, "CRYSTAL_PVP");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    Material material = Material.valueOf(itemSection.getString("material", "STONE"));
                    String name = itemSection.getString("name", "Item");
                    List<String> lore = itemSection.getStringList("lore");
                    List<Integer> slots = itemSection.getIntegerList("slots");

                    ItemBuilderGUI itemBuilder = new ItemBuilderGUI(material)
                            .name(ColorizeText.hex(name))
                            .lore(lore.toArray(new String[0]))
                            .flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);

                    if (itemSection.getBoolean("enchanted", false)) {
                        itemBuilder.enchant(Enchantment.MENDING, 1);
                    }

                    ItemStack item = itemBuilder.build();

                    if (key.equalsIgnoreCase("filter")) {
                        for (int slot : slots) {
                            inventory.setItem(slot, item, event -> event.setCancelled(true));
                        }
                    } else {
                        int slot = itemSection.getInt("slot", 0);
                        inventory.setItem(slot, item, event -> {
                            if (key.equalsIgnoreCase("back")) {
                                Player p = (Player) event.getWhoClicked();
                                p.closeInventory();
                                KitsMenu.openKitMenu(p).open(p);
                            } else if (key.equalsIgnoreCase("refill")) {
                                Player p = (Player) event.getWhoClicked();
                                String currentCategory = getCurrentCategory(p);
                                KitsX.getKitRoomUtil().load(inventory, currentCategory);
                            } else {
                                Player p = (Player) event.getWhoClicked();
                                String category = itemSection.getString("category", "");
                                if (!category.isEmpty()) {
                                    setCurrentCategory(p, category);
                                    KitsX.getKitRoomUtil().load(inventory, category);
                                }
                            }
                        });
                    }
                }
            }
        }

        inventory.addClickHandler(event -> {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot <= 44) {
                event.setCancelled(false);
            }
        });

        return inventory;
    }

    public static void setCurrentCategory(Player player, String category) {
        currentCategoryMap.put(player, category);
    }

    public static String getCurrentCategory(Player player) {
        return currentCategoryMap.getOrDefault(player, "null");
    }
}