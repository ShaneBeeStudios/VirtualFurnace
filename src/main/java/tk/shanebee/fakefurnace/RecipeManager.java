package tk.shanebee.fakefurnace;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import tk.shanebee.fakefurnace.recipe.Fuel;
import tk.shanebee.fakefurnace.recipe.FurnaceRecipe;

import java.util.HashMap;
import java.util.Map;

/**
 * Recipe manager for furnaces
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public class RecipeManager {

    private final Map<NamespacedKey, Fuel> fuelMap;
    private final Map<NamespacedKey, FurnaceRecipe> furnaceRecipeMap;

    RecipeManager(FakeFurnace plugin) {
        this.fuelMap = new HashMap<>();
        this.furnaceRecipeMap = new HashMap<>();
    }

    /**
     * Register a new {@link Fuel}
     *
     * @param fuel new Fuel to register
     * @return true if fuel was registered
     */
    public boolean registerFuel(Fuel fuel) {
        if (this.fuelMap.containsKey(fuel.getKey())) return false;
        this.fuelMap.put(fuel.getKey(), fuel);
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
     * Get a map of all {@link Fuel}s
     *
     * @return Map of Fuels
     */
    public Map<NamespacedKey, Fuel> getFuels() {
        return this.fuelMap;
    }

    /**
     * Get a map of all {@link FurnaceRecipe}s
     *
     * @return Map of FurnaceRecipes
     */
    public Map<NamespacedKey, FurnaceRecipe> getFurnaceRecipes() {
        return this.furnaceRecipeMap;
    }

    /**
     * Get a {@link Fuel} by material
     *
     * @param material Material of Fuel to grab
     * @return Fuel from recipe
     */
    public Fuel getFuelByMaterial(Material material) {
        for (Fuel fuel : this.fuelMap.values()) {
            if (fuel.matchFuel(material)) {
                return fuel;
            }
        }
        return null;
    }

    /**
     * Get a {@link Fuel} by key
     *
     * @param key Key of Fuel
     * @return Fuel from key
     */
    public Fuel getFuelByKey(NamespacedKey key) {
        return this.fuelMap.get(key);
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

}
