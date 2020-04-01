package tk.shanebee.fakefurnace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.fakefurnace.recipe.Fuel;
import tk.shanebee.fakefurnace.recipe.FurnaceRecipe;
import tk.shanebee.fakefurnace.recipe.Recipe;

@SuppressWarnings("unused")
public class FakeFurnace extends JavaPlugin {

    private static FakeFurnace instance;

    @Override
    public void onEnable() {
        instance = this;
        log("Loading recipes...");
        registerRecipes();
        log("Recipes loaded &asuccessfully!");
        log("Loading fuels...");
        registerFuels();
        log("Fuels loaded &asuccessfully!");
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void registerRecipes() {
        FurnaceRecipe chicken = new FurnaceRecipe(getKey("chicken"), Material.CHICKEN, Material.COOKED_CHICKEN, 200);
        FurnaceRecipe beef = new FurnaceRecipe(getKey("beef"), Material.BEEF, Material.COOKED_BEEF, 100);

        Recipe.registerFurnaceRecipe(chicken);
        Recipe.registerFurnaceRecipe(beef);
    }

    private void registerFuels() {
        Fuel coal = new Fuel(getKey("coal"), Material.COAL, 1600);
        Fuel charcoal = new Fuel(getKey("charcoal"), Material.CHARCOAL, 1000);

        Recipe.registerFuel(coal);
        Recipe.registerFuel(charcoal);
    }

    public static void registerFurnaceRecipe(FurnaceRecipe recipe) {
        Recipe.registerFurnaceRecipe(recipe);
    }

    public static void registerFuel(Fuel fuel) {
        Recipe.registerFuel(fuel);
    }

    public static FakeFurnace getPlugin() {
        return instance;
    }

    private NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }

    private void log(String message) {
        String prefix = "&7[&bFake&3Furnace&7] ";
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

}
