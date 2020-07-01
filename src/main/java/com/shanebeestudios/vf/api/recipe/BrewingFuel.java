package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.machine.Brewer;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Fuel for {@link Brewer}
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class BrewingFuel extends Fuel {

    public static final BrewingFuel BLAZE_POWDER = get("blaze_powder", Material.BLAZE_POWDER, 20);

    private static BrewingFuel get(String name, Material fuel, int uses) {
        return new BrewingFuel(Util.getKey("mc_brewfuel_" + name), new ItemStack(fuel), uses);
    }

    private final int uses;

    /**
     * Create a new fuel for brewers
     * <p>ItemStacks will only accept an item amount of 1.
     * Values higher than this will just be set to 1.</p>
     *
     * @param key      Key for this fuel
     * @param fuelItem ItemStack to burn
     * @param uses     How many items this fuel will brew
     */
    public BrewingFuel(@NotNull NamespacedKey key, @NotNull ItemStack fuelItem, int uses) {
        super(key, fuelItem, null);
        super.fuelItem.setAmount(1);
        this.uses = uses;
    }

    /**
     * Create a new fuel for brewers
     *
     * @param key      Key for this fuel
     * @param fuelItem Material to burn
     * @param uses     How many items this fuel will brew
     */
    public BrewingFuel(@NotNull NamespacedKey key, @NotNull Material fuelItem, int uses) {
        super(key, new ItemStack(fuelItem, 1), null);
        this.uses = uses;
    }

    /**
     * Create a new fuel for brewers
     *
     * @param key  Key for this fuel
     * @param tag  Tag to use as fuel
     * @param uses How many items this fuel will brew
     */
    public BrewingFuel(@NotNull NamespacedKey key, @NotNull Tag<Material> tag, int uses) {
        super(key, null, tag);
        this.uses = uses;
    }

    /**
     * Get the amount of uses for this fuel
     * <p>The amount of items this fuel will brew.</p>
     *
     * @return Amount of uses for this fuel
     */
    public int getUses() {
        return uses;
    }

}
