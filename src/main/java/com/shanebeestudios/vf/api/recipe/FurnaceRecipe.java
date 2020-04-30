package com.shanebeestudios.vf.api.recipe;

import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.SmokingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Recipes for {@link com.shanebeestudios.vf.api.machine.Furnace Furnaces}
 */
@SuppressWarnings("unused")
public class FurnaceRecipe extends Recipe {

    private static final boolean HAS_SMOKING = Util.isRunningMinecraft(1, 14);
    private static final List<FurnaceRecipe> VANILLA_FURNACE_RECIPES = new ArrayList<>();
    private static final List<FurnaceRecipe> VANILLA_SMOKING_RECIPES = new ArrayList<>();
    private static final List<FurnaceRecipe> VANILLA_BLASTING_RECIPES = new ArrayList<>();

    static {
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof org.bukkit.inventory.FurnaceRecipe) {
                org.bukkit.inventory.FurnaceRecipe r = ((org.bukkit.inventory.FurnaceRecipe) recipe);
                FurnaceRecipe rec = new FurnaceRecipe(Util.getKey("mc_furnace_" + r.getKey().getKey()), r.getInput().getType(), r.getResult().getType(), r.getCookingTime(), r.getExperience());
                VANILLA_FURNACE_RECIPES.add(rec);
            } else if (HAS_SMOKING) {
                if (recipe instanceof SmokingRecipe) {
                    SmokingRecipe r = ((SmokingRecipe) recipe);
                    FurnaceRecipe rec = new FurnaceRecipe(Util.getKey("mc_smoking_" + r.getKey().getKey()), r.getInput().getType(), r.getResult().getType(), r.getCookingTime(), r.getExperience());
                    VANILLA_SMOKING_RECIPES.add(rec);
                } else if (recipe instanceof BlastingRecipe) {
                    BlastingRecipe r = ((BlastingRecipe) recipe);
                    FurnaceRecipe rec = new FurnaceRecipe(Util.getKey("mc_blasting_" + r.getKey().getKey()), r.getInput().getType(), r.getResult().getType(), r.getCookingTime(), r.getExperience());
                    VANILLA_BLASTING_RECIPES.add(rec);
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

    private final Material ingredient;
    private final int cookTime;
    private final float experience;

    /**
     * Create a new recipe for a {@link com.shanebeestudios.vf.api.machine.Furnace}
     * <p>The experience for this will default to 0.0</p>
     *
     * @param key        Key for recipe
     * @param ingredient Ingredient to be put into furnace
     * @param result     The resulting item from this recipe
     * @param cookTime   Time to cook this item (in ticks)
     */
    public FurnaceRecipe(NamespacedKey key, Material ingredient, Material result, int cookTime) {
        this(key, ingredient, result, cookTime, 0.0F);
    }

    /**
     * Create a new recipe for a {@link com.shanebeestudios.vf.api.machine.Furnace}
     *
     * @param key        Key for recipe
     * @param ingredient Ingredient to be put into furnace
     * @param result     The resulting item from this recipe
     * @param cookTime   Time to cook this item (in ticks)
     * @param experience The experience the player will receive for cooking this item
     */
    public FurnaceRecipe(NamespacedKey key, Material ingredient, Material result, int cookTime, float experience) {
        super(key, result);
        this.ingredient = ingredient;
        this.cookTime = cookTime;
        this.experience = experience;
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
     * Get the cook time for this recipe
     *
     * @return Cook time for this recipe
     */
    public int getCookTime() {
        return this.cookTime;
    }

    /**
     * Get the experience this recipe will yield
     *
     * @return Experience this recipe will yield
     */
    public float getExperience() {
        return experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FurnaceRecipe recipe = (FurnaceRecipe) o;
        return cookTime == recipe.cookTime && Float.compare(recipe.experience, experience) == 0 && ingredient == recipe.ingredient;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, cookTime, experience);
    }

    @Override
    public String toString() {
        return "FurnaceRecipe{" +
                "key=" + key +
                ", ingredient=" + ingredient +
                ", result=" + result +
                ", cookTime=" + cookTime +
                ", experience=" + experience +
                '}';
    }

}
