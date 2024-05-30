package dev.darkxx.kitsx.listeners;

import dev.darkxx.kitsx.KitsX;
import dev.darkxx.utils.text.color.ColorizeText;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class AutoRekitListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!KitsX.getAutoRekitUtil().hasAutoRekit(player)) {
            KitsX.getAutoRekitUtil().set(player, false, "Kit 1");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();
        if (attacker != null) {
            if (KitsX.getAutoRekitUtil().isEnabled(attacker)) {
                loadAndSend(attacker);
            }
        }
    }

    private void loadAndSend(Player player) {
        String kit = KitsX.getAutoRekitUtil().getKit(player);
        KitsX.getKitUtil().load(player, kit);
        send(player);
    }

    private void send(Player player) {
        if (!KitsX.getInstance().getConfig().getBoolean("messages.send-auto-rekit-message", true)) {
            return;
        }
        String msg = KitsX.getInstance().getConfig().getString("messages.auto-rekit-message");
        if (msg != null) {
            player.sendMessage(ColorizeText.hex(msg));
        }
    }
}