package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class Recipe {

    private static final Map<NamespacedKey, Fuel> fuelMap = new HashMap<>();
    private static final Map<NamespacedKey, FurnaceRecipe> furnaceRecipeMap = new HashMap<>();

    private Recipe() {
    }

    public static boolean registerFuel(Fuel fuel) {
        if (fuelMap.containsKey(fuel.getKey())) return false;
        fuelMap.put(fuel.getKey(), fuel);
        return true;
    }

    public static boolean registerFurnaceRecipe(FurnaceRecipe furnaceRecipe) {
        if (furnaceRecipeMap.containsKey(furnaceRecipe.getKey())) return false;
        furnaceRecipeMap.put(furnaceRecipe.getKey(), furnaceRecipe);
        return true;
    }

    public static Map<NamespacedKey, Fuel> getFuels() {
        return fuelMap;
    }

    public static Map<NamespacedKey, FurnaceRecipe> getFurnaceRecipes() {
        return furnaceRecipeMap;
    }

    public static Fuel getByMaterial(Material material) {
        for (Fuel fuel : fuelMap.values()) {
            if (fuel.getFuel() == material) {
                return fuel;
            }
        }
        return null;
    }

    public static Fuel getFuelByKey(NamespacedKey key) {
        return fuelMap.get(key);
    }

    public static FurnaceRecipe getFurnaceRecipeByKey(NamespacedKey key) {
        return furnaceRecipeMap.get(key);
    }

    public static FurnaceRecipe getByIngredient(Material ingredient) {
        for (FurnaceRecipe recipe : furnaceRecipeMap.values()) {
            if (recipe.getIngredient() == ingredient) {
                return recipe;
            }
        }
        return null;
    }

}
