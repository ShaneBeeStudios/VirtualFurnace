package tk.shanebee.fakefurnace.recipe;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public class FurnaceRecipe implements Keyed {

    private final NamespacedKey key;
    private final Material ingredient;
    private final Material result;
    private final int cookTime;

    public FurnaceRecipe(NamespacedKey key, Material ingredient, Material result, int cookTime) {
        this.key = key;
        this.ingredient = ingredient;
        this.result = result;
        this.cookTime = cookTime;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return this.key;
    }

    public Material getIngredient() {
        return this.ingredient;
    }

    public Material getResult() {
        return this.result;
    }

    public int getCookTime() {
        return this.cookTime;
    }

}
