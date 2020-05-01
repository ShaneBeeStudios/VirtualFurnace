package com.shanebeestudios.vf.api.event.machine;

import com.shanebeestudios.vf.api.event.Event;
import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an {@link ItemStack} is removed from a {@link Furnace}
 */
@SuppressWarnings("unused")
public class FurnaceExtractEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Furnace furnace;
    private final Player player;
    private ItemStack itemStack;
    private int experience;

    public FurnaceExtractEvent(@NotNull Furnace furnace, @NotNull Player player, @NotNull ItemStack itemStack, int experience) {
        this.furnace = furnace;
        this.player = player;
        this.itemStack = itemStack;
        this.experience = experience;
    }

    /**
     * Get the furnace from this event
     *
     * @return Furnace from this event
     */
    public Furnace getFurnace() {
        return furnace;
    }

    /**
     * Get the player that extracted the item
     *
     * @return Player that extracted the item
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the itemstack which was extracted
     *
     * @return Itemstack which was extracted
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Set the itemstack to be extracted
     *
     * @param itemStack Itemstack to be extracted
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Get the experience the player will receive
     *
     * @return Experience the player will receive
     */
    public int getExperience() {
        return experience;
    }

    /**
     * Set the experience the player will receive
     *
     * @param experience Experience player will receive
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
