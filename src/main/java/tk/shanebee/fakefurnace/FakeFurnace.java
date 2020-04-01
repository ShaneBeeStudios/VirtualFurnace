package tk.shanebee.fakefurnace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.fakefurnace.machine.Furnace;
import tk.shanebee.fakefurnace.recipe.Fuel;
import tk.shanebee.fakefurnace.recipe.FurnaceRecipe;

/**
 * Main class for FakeFurnace
 */
@SuppressWarnings("unused")
public class FakeFurnace extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(Furnace.class, "furnace");
    }

    private static FakeFurnace instance;
    private RecipeManager recipeManager;
    private FurnaceManager furnaceManager;

    @Override
    public void onEnable() {
        instance = this;
        this.recipeManager = new RecipeManager(this);
        this.furnaceManager = new FurnaceManager(this);
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

        this.recipeManager.registerFurnaceRecipe(chicken);
        this.recipeManager.registerFurnaceRecipe(beef);
    }

    private void registerFuels() {
        Fuel coal = new Fuel(getKey("coal"), Material.COAL, 1600);
        Fuel charcoal = new Fuel(getKey("charcoal"), Material.CHARCOAL, 1000);

        this.recipeManager.registerFuel(coal);
        this.recipeManager.registerFuel(charcoal);
    }

    /**
     * Register a new {@link FurnaceRecipe}
     *
     * @param recipe new FurnaceRecipe to register
     */
    public static void registerFurnaceRecipe(FurnaceRecipe recipe) {
        instance.recipeManager.registerFurnaceRecipe(recipe);
    }

    /**
     * Register a new {@link Fuel}
     *
     * @param fuel new Fuel to register
     */
    public static void registerFuel(Fuel fuel) {
        instance.recipeManager.registerFuel(fuel);
    }

    /**
     * Get an instance of this plugin
     *
     * @return Instance of this plugin
     */
    public static FakeFurnace getPlugin() {
        return instance;
    }

    /**
     * Get an instance of the recipe manager
     *
     * @return Instance of recipe manager
     */
    public RecipeManager getRecipeManager() {
        return this.recipeManager;
    }

    /**
     * Get an instance of the furnace manager
     *
     * @return Instance of furnace manager
     */
    public FurnaceManager getFurnaceManager() {
        return furnaceManager;
    }

    private NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }

    private void log(String message) {
        String prefix = "&7[&bFake&3Furnace&7] ";
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

}
