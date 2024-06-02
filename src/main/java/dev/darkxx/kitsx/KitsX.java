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

package dev.darkxx.kitsx;

import dev.darkxx.kitsx.commands.*;
import dev.darkxx.kitsx.commands.admin.*;
import dev.darkxx.kitsx.hooks.HooksImpl;
import dev.darkxx.kitsx.listeners.*;
import dev.darkxx.kitsx.utils.config.MenuConfig;
import dev.darkxx.kitsx.utils.*;
import dev.darkxx.kitsx.utils.config.ConfigManager;
import dev.darkxx.utils.library.Utils;
import dev.darkxx.utils.library.wrapper.PluginWrapper;
import dev.darkxx.utils.menu.xmenu.GuiManager;
import dev.darkxx.utils.misc.Versions;
import dev.darkxx.utils.server.Servers;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.command.ConsoleCommandSender;

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
        saveDefaultConfig();

        ConsoleCommandSender log = Servers.server().getConsoleSender();
        ConfigManager configManager = ConfigManager.get(this);

        MenuConfig.of(this);

        KitUtil.of(this);
        kitUtil = new KitUtil(configManager);

        PremadeKitUtil.of(this);
        premadeKitUtil = new PremadeKitUtil(configManager);

        EnderChestUtil.of(this);
        enderChestUtil =  new EnderChestUtil(configManager);

        KitRoomUtil.of(this);
        kitRoomUtil = new KitRoomUtil(configManager);

        AutoRekitUtil.of(this);
        autoRekitUtil = new AutoRekitUtil(configManager);

        Servers.server().getPluginManager().registerEvents(new AutoRekitListener(), this);
        if (Versions.isHigherThanOrEqualTo("1.20.1")) {
            AutoRekitAnchorListener autoRekitAnchorListener = new AutoRekitAnchorListener();
            autoRekitAnchorListener.register(this);
        }

        new KitCommand(this);
        new KitRoomAdminCommand(this);
        new PremadeKitCommand(this);
        new AutoRekitCommand(this);
        new KitsAdminCommand(this);
        int kits = getConfig().getInt("kits");
        for (int i = 1; i <= kits; i++) {
            new KitLoadCommand(this, "kit" + i, i);
        }

        HooksImpl.of(this);

        log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fThe &#ff2e2eplugin &fhas been successfully enabled, created by &#ff2e2eXyrisPlugins &fwith &#ff2e2e❤"));
        log.sendMessage(ColorizeText.hex("&c&lKitsX &7› &fJoin our &#ff2e2esupport server &fat &#ff2e2ehttps://xyris.fun/devs/"));
    }

    @Override
    protected void stop() {
        getKitUtil().saveAll();
        getEnderChestUtil().saveAll();
        getPremadeKitUtil().saveAll();
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