package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipes for furnaces
 */
@SuppressWarnings("unused")
public class FurnaceRecipe implements Keyed {

    private static final boolean HAS_SMOKING = Util.isRunningMinecraft(1, 14);
    private static final List<FurnaceRecipe> VANILLA_FURNACE_RECIPES = new ArrayList<>();
    private static final List<FurnaceRecipe> VANILLA_SMOKING_RECIPES = new ArrayList<>();
    private static final List<FurnaceRecipe> VANILLA_BLASTING_RECIPES = new ArrayList<>();

    static {
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof org.bukkit.inventory.FurnaceRecipe) {
                NamespacedKey key = Util.getKey("mc_furnace_" + ((org.bukkit.inventory.FurnaceRecipe) recipe).getKey().getKey());
                Material ingredient = ((org.bukkit.inventory.FurnaceRecipe) recipe).getInput().getType();
                Material result = recipe.getResult().getType();
                int cookTime = ((org.bukkit.inventory.FurnaceRecipe) recipe).getCookingTime();
                FurnaceRecipe furnaceRecipe = new FurnaceRecipe(key, ingredient, result, cookTime);
                VANILLA_FURNACE_RECIPES.add(furnaceRecipe);
            } else if (HAS_SMOKING) {
                if (recipe instanceof SmokingRecipe) {
                    NamespacedKey key = Util.getKey("mc_smoking_" + ((SmokingRecipe) recipe).getKey().getKey());
                    Material ingredient = ((SmokingRecipe) recipe).getInput().getType();
                    Material result = recipe.getResult().getType();
                    int cookTime = ((SmokingRecipe) recipe).getCookingTime();
                    FurnaceRecipe furnaceRecipe = new FurnaceRecipe(key, ingredient, result, cookTime);
                    VANILLA_SMOKING_RECIPES.add(furnaceRecipe);
                } else if (recipe instanceof BlastingRecipe) {
                    NamespacedKey key = Util.getKey("mc_blasting_" + ((BlastingRecipe) recipe).getKey().getKey());
                    Material ingredient = ((BlastingRecipe) recipe).getInput().getType();
                    Material result = recipe.getResult().getType();
                    int cookTime = ((BlastingRecipe) recipe).getCookingTime();
                    FurnaceRecipe furnaceRecipe = new FurnaceRecipe(key, ingredient, result, cookTime);
                    VANILLA_BLASTING_RECIPES.add(furnaceRecipe);
                }
            }
        });
    }

    /**
     * Get a list of vanilla Minecraft furnace recipes
     *
     * @return List of vanilla furnace recipes
     */
    public static List<FurnaceRecipe> getVanillaFurnaceRecipes() {
        return VANILLA_FURNACE_RECIPES;
    }

    /**
     * Get a list of vanilla Minecraft smoking recipes
     * <p><b>NOTE:</b> These recipes are only available on MC 1.14+</p>
     *
     * @return List of vanilla smoking recipes
     */
    public static List<FurnaceRecipe> getVanillaSmokingRecipes() {
        return VANILLA_SMOKING_RECIPES;
    }

    /**
     * Get a list of vanilla Minecraft blasting recipes
     * <p><b>NOTE:</b> These recipes are only available on MC 1.14+</p>
     *
     * @return List of vanilla blasting recipes
     */
    public static List<FurnaceRecipe> getVanillaBlastingRecipes() {
        return VANILLA_BLASTING_RECIPES;
    }

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

    @Override
    public String toString() {
        return "FurnaceRecipe{" +
                "key=" + key +
                ", ingredient=" + ingredient +
                ", result=" + result +
                ", cookTime=" + cookTime +
                '}';
    }

}
