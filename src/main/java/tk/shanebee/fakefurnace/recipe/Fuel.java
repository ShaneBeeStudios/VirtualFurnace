package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

/**
 * Fuel for furnace
 */
@SuppressWarnings("unused")
public class Fuel implements Keyed {

    private final NamespacedKey key;
    private final Material fuel;
    private final Tag<Material> tag;
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
        this.tag = null;
        this.burnTime = burnTime;
    }

    /**
     * Create a new fuel for furnaces
     *
     * @param key      Key for recipe
     * @param fuelTag  Tag to use as fuel
     * @param burnTime Time this fuel will burn for (in ticks)
     */
    public Fuel(NamespacedKey key, Tag<Material> fuelTag, int burnTime) {
        this.key = key;
        this.fuel = null;
        this.tag = fuelTag;
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
     * @return Material of fuel (null if non-existent)
     */
    public Material getFuel() {
        return this.fuel;
    }

    /**
     * Get the tag of this fuel
     *
     * @return Tag of fuel (null if non-existent)
     */
    public Tag<Material> getTag() {
        return tag;
    }

    /**
     * Check if a material matches this fuel
     *
     * @param material Material to match
     * @return True if material matches
     */
    public boolean matchFuel(Material material) {
        if (fuel != null && fuel == material) {
            return true;
        } else return tag != null && tag.isTagged(material);
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
