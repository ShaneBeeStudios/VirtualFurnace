package com.shanebeestudios.vf.api.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents base class for recipes
 */
public abstract class Recipe implements Keyed {

    final NamespacedKey key;
    final ItemStack itemStack;

    Recipe(@NotNull NamespacedKey key, @NotNull Material result) {
        this.key = key;
        this.itemStack = new ItemStack(result, 1);
    }

    Recipe(@NotNull NamespacedKey key, @NotNull ItemStack itemStack) {
        this.key = key;
        this.itemStack = itemStack;
    }

    /**
     * Get the key from this recipe
     *
     * @return Key from recipe
     */
    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get the result of this recipe
     *
     * @return Result of this recipe
     */
    public Material getResult() {
        return this.itemStack.getType();
    }

    public ItemStack getItemResult() {
        return this.itemStack;
    }

}
