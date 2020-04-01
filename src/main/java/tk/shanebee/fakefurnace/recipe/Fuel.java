package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Fuel for furnace
 */
public class Fuel implements Keyed {

    private final NamespacedKey key;
    private final Material fuel;
    private final int burnTime;

    /**
     * Create a new fuel for furnaces
     *
     * @param key      Key for recipe
     * @param fuel     Fuel to register
     * @param burnTime Time this fuel will burn for (in ticks)
     */
    public Fuel(NamespacedKey key, Material fuel, int burnTime) {
        this.key = key;
        this.fuel = fuel;
        this.burnTime = burnTime;
    }

    /**
     * Get the key from this fuel
     *
     * @return Key from fuel
     */
    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Get the material of this fuel
     *
     * @return Material of fuel
     */
    public Material getFuel() {
        return this.fuel;
    }

    /**
     * Get the time this fuel will burn for
     *
     * @return Time fuel will burn
     */
    public int getBurnTime() {
        return this.burnTime;
    }

}
