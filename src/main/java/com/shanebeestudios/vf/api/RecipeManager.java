package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.recipe.BrewingFuel;
import com.shanebeestudios.vf.api.recipe.BrewingRecipe;
import com.shanebeestudios.vf.api.recipe.FurnaceFuel;
import com.shanebeestudios.vf.api.recipe.FurnaceRecipe;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe manager for {@link com.shanebeestudios.vf.api.machine.Furnace Furnaces}
 * <p>You can get an instance of this class from <b>{@link VirtualFurnaceAPI#getRecipeManager()}</b></p>
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class RecipeManager {

    private final Map<NamespacedKey, FurnaceFuel> furnaceFuelMap;
    private final Map<NamespacedKey, BrewingFuel> brewingFuelMap;
    private final Map<NamespacedKey, FurnaceRecipe> furnaceRecipeMap;
    private final Map<NamespacedKey, BrewingRecipe> brewingRecipeMap;

    RecipeManager() {
        this.furnaceFuelMap = new HashMap<>();
        this.brewingFuelMap = new HashMap<>();
        this.furnaceRecipeMap = new HashMap<>();
        this.brewingRecipeMap = new HashMap<>();
    }

    /**
     * Register a new {@link FurnaceFuel}
     *
     * @param furnaceFuel New fuel to register
     * @return true if fuel was registered
     */
    public boolean registerFuel(FurnaceFuel furnaceFuel) {
        if (this.furnaceFuelMap.containsKey(furnaceFuel.getKey())) return false;
        this.furnaceFuelMap.put(furnaceFuel.getKey(), furnaceFuel);
        return true;
    }

    /**
     * Register a new {@link BrewingFuel}
     *
     * @param brewingFuel New fuel to register
     * @return True if fuel was registered
     */
    public boolean registerBrewingFuel(@NotNull BrewingFuel brewingFuel) {
        if (this.brewingFuelMap.containsKey(brewingFuel.getKey())) return false;
        this.brewingFuelMap.put(brewingFuel.getKey(), brewingFuel);
        return true;
    }

    /**
     * Register a new {@link FurnaceRecipe}
     *
     * @param furnaceRecipe new FurnaceRecipe to register
     * @return true if recipe was registered
     */
    public boolean registerFurnaceRecipe(FurnaceRecipe furnaceRecipe) {
        if (this.furnaceRecipeMap.containsKey(furnaceRecipe.getKey())) return false;
        this.furnaceRecipeMap.put(furnaceRecipe.getKey(), furnaceRecipe);
        return true;
    }

    /**
     * Register a new {@link BrewingRecipe}
     *
     * @param brewingRecipe New brewing recipe to register
     * @return True if recipe was registered
     */
    public boolean registerBrewingRecipe(BrewingRecipe brewingRecipe) {
        if (this.brewingRecipeMap.containsKey(brewingRecipe.getKey())) return false;
        this.brewingRecipeMap.put(brewingRecipe.getKey(), brewingRecipe);
        return true;
    }

    /**
     * Get a map of all {@link FurnaceFuel FurnaceFuels}
     *
     * @return Map of Fuels
     */
    public Map<NamespacedKey, FurnaceFuel> getFuels() {
        return this.furnaceFuelMap;
    }

    /**
     * Get a map of all {@link BrewingFuel BrewingFuels}
     *
     * @return Map of all brewing fuels
     */
    public Map<NamespacedKey, BrewingFuel> getBrewingFuels() {
        return brewingFuelMap;
    }

    /**
     * Get a map of all {@link FurnaceRecipe}s
     *
     * @return Map of FurnaceRecipes
     */
    public Map<NamespacedKey, FurnaceRecipe> getFurnaceRecipes() {
        return this.furnaceRecipeMap;
    }

    public Map<NamespacedKey, BrewingRecipe> getBrewingRecipes() {
        return brewingRecipeMap;
    }

    /**
     * Get a {@link FurnaceFuel} by material
     *
     * @param material Material of Fuel to grab
     * @return Fuel from recipe
     */
    public FurnaceFuel getFuelByMaterial(Material material) {
        for (FurnaceFuel furnaceFuel : this.furnaceFuelMap.values()) {
            if (furnaceFuel.matchFuel(material)) {
                return furnaceFuel;
            }
        }
        return null;
    }

    /**
     * Get a {@link BrewingFuel} by an ItemStack
     *
     * @param itemStack ItemStack to grab fuel from
     * @return Fuel from recipe
     */
    public BrewingFuel getBrewingFuelByItem(ItemStack itemStack) {
        for (BrewingFuel fuel : this.brewingFuelMap.values()) {
            if (fuel.matchFuel(itemStack)) {
                return fuel;
            }
        }
        return null;
    }

    /**
     * Get a {@link BrewingFuel} by a Material
     *
     * @param material Material to grab fuel from
     * @return Fuel from recipe
     */
    public BrewingFuel getBrewingFuelByMaterial(Material material) {
        for (BrewingFuel fuel : this.brewingFuelMap.values()) {
            if (fuel.matchFuel(material)) {
                return fuel;
            }
        }
        return null;
    }

    /**
     * Get a {@link FurnaceFuel} by key
     *
     * @param key Key of Fuel
     * @return Fuel from key
     */
    public FurnaceFuel getFuelByKey(NamespacedKey key) {
        return this.furnaceFuelMap.get(key);
    }

    /**
     * Get a {@link BrewingFuel} by key
     *
     * @param key Key of fuel
     * @return Fuel from key
     */
    public BrewingFuel getBrewingFuelByKey(@NotNull NamespacedKey key) {
        return this.brewingFuelMap.get(key);
    }

    /**
     * Get a {@link FurnaceRecipe} by ingredient
     *
     * @param ingredient Ingredient of FurnaceRecipe
     * @return FurnaceRecipe from ingredient
     */
    public FurnaceRecipe getByIngredient(Material ingredient) {
        for (FurnaceRecipe recipe : this.furnaceRecipeMap.values()) {
            if (recipe.getIngredient() == ingredient) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Get a {@link FurnaceRecipe} by key
     *
     * @param key Key of FurnaceRecipe
     * @return FurnaceRecipe from key
     */
    public FurnaceRecipe getFurnaceRecipeByKey(NamespacedKey key) {
        return this.furnaceRecipeMap.get(key);
    }

    /**
     * Get a {@link BrewingRecipe} by ingredient and bottle
     *
     * @param ingredient ItemStack to check
     * @param bottle     Bottle to check
     * @return Recipe from ingredient and bottle
     */
    public BrewingRecipe getBrewingRecipeByIngredient(ItemStack ingredient, ItemStack bottle) {
        for (BrewingRecipe recipe : this.brewingRecipeMap.values()) {
            if (recipe.getIngredient().equals(ingredient) && recipe.getInputBottle().equals(bottle)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Get a {@link BrewingRecipe} by key
     *
     * @param key Key of recipe
     * @return Recipe from key
     */
    public BrewingRecipe getBrewingRecipeByKey(NamespacedKey key) {
        return this.brewingRecipeMap.get(key);
    }

}
