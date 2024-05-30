package dev.darkxx.kitsx;

import dev.darkxx.kitsx.commands.*;
import dev.darkxx.kitsx.commands.admin.KitsAdminCommand;
import dev.darkxx.kitsx.hooks.HooksImpl;
import dev.darkxx.kitsx.listeners.AutoRekitListener;
import dev.darkxx.kitsx.utils.*;
import dev.darkxx.kitsx.menus.config.MenuConfig;
import dev.darkxx.kitsx.utils.menu.GuiManager;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.library.wrapper.PluginWrapper;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class KitsX extends PluginWrapper {

    private static KitsX instance;
    private static KitUtil kitUtil;
    private static PremadeKitUtil premadeKitUtil;
    private static EnderChestUtil enderChestUtil;
    private static KitRoomUtil kitRoomUtil;
    private static AutoRekitUtil autoRekitUtil;


    @Override
    protected void start() {
        instance = this;
        Utils.init(this);
        GuiManager.register(this);
        ConsoleCommandSender log = Bukkit.getServer().getConsoleSender();
        saveDefaultConfig();

        KitUtil.of(this);
        kitUtil = new KitUtil(new File(getDataFolder(), "data/kits.yml"), YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data/kits.yml")));

        PremadeKitUtil.of(this);
        premadeKitUtil = new PremadeKitUtil(new File(getDataFolder(), "data/premadekit.yml"), YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data/premadekit.yml")));

        EnderChestUtil.of(this);
        enderChestUtil = new EnderChestUtil(new File(getDataFolder(), "data/enderchest.yml"), YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data/enderchest.yml")));

        KitRoomUtil.of(this);
        kitRoomUtil = new KitRoomUtil(new File(getDataFolder(), "data/kitroom.yml"), YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data/kitroom.yml")));

        AutoRekitUtil.of(this);
        autoRekitUtil = new AutoRekitUtil(new File(getDataFolder(), "data/autorekit.yml"), YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data/autorekit.yml")));


        MenuConfig.of(this);

        getServer().getPluginManager().registerEvents(new AutoRekitListener(), this);

        new KitCommand(this);
        new KitRoomAdminCommand(this);
        new PremadeKitCommand(this);
        new AutoRekitCommand(this);
        new KitsAdminCommand(this);
        int kits = getConfig().getInt("kits");
        for (int i = 1; i <= kits; i++) {
            new KitLoadCommand(this, "kit" + i, i);
        }

        HooksImpl.get().of(this);

        log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fThe &#ff2e2eplugin &fhas been successfully enabled, created by &#ff2e2eXyrisPlugins &fwith &#ff2e2e❤"));
        log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fJoin our &#ff2e2esupport server &fat &#ff2e2ehttps://xyris.fun/devs/"));
    }

    @Override
    protected void stop() {
        // do nothing :D
    }

    public static KitsX getInstance() {
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

    public static AutoRekitUtil getAutoRekitUtil() {
        return autoRekitUtil;
    }
}