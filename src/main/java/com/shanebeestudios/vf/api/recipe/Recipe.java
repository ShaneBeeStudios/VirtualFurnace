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
    final Material result;

    Recipe(@NotNull NamespacedKey key, @NotNull Material result) {
        this.key = key;
        this.result = result;
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
     * @deprecated Use {@link #getResultType()} instead
     * <p>Will be working on using {@link ItemStack ItemStacks} for recipes in the future</p>
     */
    @Deprecated
    public Material getResult() {
        return this.result;
    }

    /**
     * Get the result {@link Material} of this recipe
     *
     * @return Result Material of this recipe
     */
    public Material getResultType() {
        return this.result;
    }

}
