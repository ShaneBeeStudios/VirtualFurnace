package com.shanebeestudios.vf.api.event.machine;

import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully cooked in a {@link Furnace}
 */
@SuppressWarnings("unused")
public class FurnaceCookEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();

    private final Furnace furnace;
    private final ItemStack source;
    private ItemStack result;
    private boolean cancelled;

    public FurnaceCookEvent(@NotNull Furnace furnace, @NotNull ItemStack source, @NotNull ItemStack result) {
        super(true);
        this.furnace = furnace;
        this.source = source;
        this.result = result;
        this.cancelled = false;
    }

    /**
     * Get the Furnace for this event
     *
     * @return Furnace for this event
     */
    public Furnace getFurnace() {
        return furnace;
    }

    /**
     * Get the source item, the item that was cooking.
     *
     * @return Source item
     */
    public ItemStack getSource() {
        return source;
    }

    /**
     * Get there result that was cooked
     *
     * @return Cooked item
     */
    public ItemStack getResult() {
        return result;
    }

    /**
     * Set the result that was cooked
     *
     * @param result ItemStack to set as result
     */
    public void setResult(ItemStack result) {
        this.result = result;
    }

    /**
     * Gets the cancellation state of this event.
     * <p>A cancelled event will not be executed in the server,
     * but will still pass to other plugins</p>
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     * <p>A cancelled event will not be executed in the server,
     * but will still pass to other plugins</p>
     *
     * @param cancel Whether to cancel the event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
