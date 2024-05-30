package dev.darkxx.kitsx.hooks.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import dev.darkxx.kitsx.api.events.KitLoadEvent;
import dev.darkxx.kitsx.api.events.KitRoomOpenEvent;
import dev.darkxx.kitsx.api.events.KitSaveEvent;
import dev.darkxx.kitsx.hooks.skript.conditions.CondEqualsKit;
import org.bukkit.entity.Player;

public class SkriptHook {

    public void of() {
        // Kit Load Event
        Skript.registerEvent("Kit Load", SimpleEvent.class, KitLoadEvent.class, "[kit] load");
        EventValues.registerEventValue(KitLoadEvent.class, Player.class, new Getter<Player, KitLoadEvent>() {
            @Override
            public Player get(KitLoadEvent e) {
                return e.getPlayer();
            }
        }, 0);
        EventValues.registerEventValue(KitLoadEvent.class, String.class, new Getter<String, KitLoadEvent>() {
            @Override
            public String get(KitLoadEvent e) {
                return e.getKitName();
            }
        }, 0);

        // Kit Save Event
        Skript.registerEvent("Kit Save", SimpleEvent.class, KitSaveEvent.class, "[kit] save");
        EventValues.registerEventValue(KitSaveEvent.class, Player.class, new Getter<Player, KitSaveEvent>() {
            @Override
            public Player get(KitSaveEvent e) {
                return e.getPlayer();
            }
        }, 0);

        // KitRoom Open Event
        Skript.registerEvent("KitRoom Open Event", SimpleEvent.class, KitRoomOpenEvent.class, "[kitroom] open");
        EventValues.registerEventValue(KitRoomOpenEvent.class, Player.class, new Getter<Player, KitRoomOpenEvent>() {
            @Override
            public Player get(KitRoomOpenEvent e) {
                return e.getPlayer();
            }
        }, 0);

        // Conditions
        Skript.registerCondition(CondEqualsKit.class,
                "[kit] (is|equals|matches) %string%",
                "[kit] (is(n't| not)|is not|doesn't equal|isn't equal to|doesn't match|isn't matching) %string%"
        );
    }

    public static SkriptHook get() {
        return new SkriptHook();
    }
}
