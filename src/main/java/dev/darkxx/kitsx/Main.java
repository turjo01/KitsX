package dev.darkxx.kitsx;

import dev.darkxx.kitsx.commands.KitCommand;
import dev.darkxx.kitsx.commands.KitRoomAdminCommand;
import dev.darkxx.kitsx.commands.PremadeKitCommand;
import dev.darkxx.kitsx.menus.config.MenuConfig;
import dev.darkxx.kitsx.utils.EnderChestUtil;
import dev.darkxx.kitsx.utils.KitRoomUtil;
import dev.darkxx.kitsx.utils.KitUtil;
import dev.darkxx.kitsx.utils.PremadeKitUtil;
import dev.darkxx.kitsx.utils.menu.GuiManager;
import dev.darkxx.kitsx.utils.wg.BlacklistedRegion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.darkxx.kitsx.commands.load.Kit1;
import dev.darkxx.kitsx.commands.load.Kit2;
import dev.darkxx.kitsx.commands.load.Kit3;
import dev.darkxx.kitsx.commands.load.Kit4;
import dev.darkxx.kitsx.commands.load.Kit5;
import dev.darkxx.kitsx.commands.load.Kit6;
import dev.darkxx.kitsx.commands.load.Kit7;

import java.io.File;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static KitUtil kitUtil;
    private static PremadeKitUtil premadeKitUtil;
    private static EnderChestUtil enderChestUtil;
    private static KitRoomUtil kitRoomUtil;
    private MenuConfig menuConfig;
    private static BlacklistedRegion blacklistedRegion;

    @Override
    public void onEnable() {
        instance = this;
        GuiManager.register(this);

        saveDefaultConfig();
        this.load();

        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kitroomadmin").setExecutor(new KitRoomAdminCommand());
        getCommand("kitroomadmin").setTabCompleter(new KitRoomAdminCommand());
        getCommand("premadekit").setExecutor(new PremadeKitCommand());
        getCommand("premadekit").setTabCompleter(new PremadeKitCommand());

        getCommand("kit1").setExecutor(new Kit1());
        getCommand("kit2").setExecutor(new Kit2());
        getCommand("kit3").setExecutor(new Kit3());
        getCommand("kit4").setExecutor(new Kit4());
        getCommand("kit5").setExecutor(new Kit5());
        getCommand("kit6").setExecutor(new Kit6());
        getCommand("kit7").setExecutor(new Kit7());
    }

    public static Main getInstance() {
        return instance;
    }

    public static KitUtil getKitUtil() {
        return kitUtil;
    }

    public static PremadeKitUtil getPremadeKitUtil() {
        return premadeKitUtil;
    }

    public static EnderChestUtil getEnderChestUtil() {
        return enderChestUtil;
    }

    public static KitRoomUtil getKitRoomUtil() {
        return kitRoomUtil;
    }

    public static BlacklistedRegion getBlacklistedRegion() {
        return blacklistedRegion;
    }

    private void load() {
        File kitsFile = new File(getDataFolder(), "data/kits.yml");
        if (!kitsFile.exists()) {
            saveResource("data/kits.yml", false);
        }
        FileConfiguration kitsStorage = YamlConfiguration.loadConfiguration(kitsFile);
        kitUtil = new KitUtil(kitsFile, kitsStorage);

        File premadeKitFile = new File(getDataFolder(), "data/premadekit.yml");
        if (!premadeKitFile.exists()) {
            saveResource("data/premadekit.yml", false);
        }
        FileConfiguration premadeKitStorage = YamlConfiguration.loadConfiguration(premadeKitFile);
        premadeKitUtil = new PremadeKitUtil(premadeKitFile, premadeKitStorage);

        File enderchestFile = new File(getDataFolder(), "data/enderchest.yml");
        if (!enderchestFile.exists()) {
            saveResource("data/enderchest.yml", false);
        }
        FileConfiguration enderchestStorage = YamlConfiguration.loadConfiguration(enderchestFile);
        enderChestUtil = new EnderChestUtil(enderchestFile, enderchestStorage);

        File kitRoomFile = new File(getDataFolder(), "data/kitroom.yml");
        if (!kitRoomFile.exists()) {
            saveResource("data/kitroom.yml", false);
        }
        FileConfiguration kitRoomStorage = YamlConfiguration.loadConfiguration(kitRoomFile);
        kitRoomUtil = new KitRoomUtil(kitRoomFile, kitRoomStorage);

        this.menuConfig = new MenuConfig(this, "menus/kits.yml");
        this.menuConfig = new MenuConfig(this, "menus/kit-editor.yml");
        this.menuConfig = new MenuConfig(this, "menus/enderchest-editor.yml");
    }
}
