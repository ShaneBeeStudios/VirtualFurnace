package com.shanebeestudios.vf.api.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
     */
    public Material getResult() {
        return this.result;
    }

}
