package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.machine.Machine;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Base Fuel class for {@link Machine Machines}
 */
@SuppressWarnings("unused")
public abstract class Fuel implements Keyed {

    final NamespacedKey key;
    final ItemStack fuelItem;
    final Tag<Material> tag;

    Fuel(@NotNull NamespacedKey key, ItemStack fuelItem, Tag<Material> tag) {
        this.key = key;
        this.fuelItem = fuelItem;
        this.tag = tag;
    }

    /**
     * Get the key from this fuel
     *
     * @return Key from fuel
     */
    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get the material of this fuel
     *
     * @return Material of fuel (null if non-existent)
     */
    public ItemStack getFuelItem() {
        return fuelItem;
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
        if (this.fuelItem != null && this.fuelItem.getType() == material) {
            return true;
        } else return tag != null && tag.isTagged(material);
    }

    /**
     * Check if a material matches this fuel
     *
     * @param itemStack ItemStack to match
     * @return True if material matches
     */
    public boolean matchFuel(ItemStack itemStack) {
        if (itemStack == null) return false;
        ItemStack clone = itemStack.clone();
        clone.setAmount(1);
        if (this.fuelItem != null && this.fuelItem.equals(clone)) {
            return true;
        } else return tag != null && tag.isTagged(itemStack.getType());
    }

    /**
     * Check if an object matches this fuel
     *
     * @param o Object to check
     * @return True if objects match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fuel fuel = (Fuel) o;
        return Objects.equals(key, fuel.key) &&
                Objects.equals(fuelItem, fuel.fuelItem) &&
                Objects.equals(tag, fuel.tag);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return Hash code value for the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, fuelItem, tag);
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "key=" + key +
                (fuelItem != null ? ", fuelItem=" + fuelItem : "") +
                (tag != null ? ", tag=" + tag : "") +
                '}';
    }

}
