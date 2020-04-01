package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Recipes for furnaces
 */
public class FurnaceRecipe implements Keyed {

    private final NamespacedKey key;
    private final Material ingredient;
    private final Material result;
    private final int cookTime;

    /**
     * Create a new recipe for a furnace
     *
     * @param key        Key for recipe
     * @param ingredient Ingredient to be put into furnace
     * @param result     Result after cooking
     * @param cookTime   Time to cook this item (in ticks)
     */
    public FurnaceRecipe(NamespacedKey key, Material ingredient, Material result, int cookTime) {
        this.key = key;
        this.ingredient = ingredient;
        this.result = result;
        this.cookTime = cookTime;
    }

    /**
     * Get the key from this recipe
     *
     * @return Key from recipe
     */
    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    /**
     * Get the ingredient of this recipe
     *
     * @return Ingredient of this recipe
     */
    public Material getIngredient() {
        return this.ingredient;
    }

    /**
     * Get the result of this recipe
     *
     * @return Result of this recipe
     */
    public Material getResult() {
        return this.result;
    }

    /**
     * Get the cook time for this recipe
     *
     * @return Cook time for this recipe
     */
    public int getCookTime() {
        return this.cookTime;
    }

}
