package dev.darkxx.kitsx.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KitSaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final String kitName;
    @Getter
    private final ItemStack[] inventoryContents;
    @Getter
    private final ItemStack[] armorContents;
    @Getter
    private final ItemStack offhandItem;
    private boolean cancelled;

    public KitSaveEvent(Player player, String kitName, ItemStack[] inventoryContents, ItemStack[] armorContents, ItemStack offhandItem) {
        this.player = player;
        this.kitName = kitName;
        this.inventoryContents = inventoryContents;
        this.armorContents = armorContents;
        this.offhandItem = offhandItem;
        this.cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
