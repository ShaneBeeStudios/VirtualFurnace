package tk.shanebee.fakefurnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import tk.shanebee.fakefurnace.command.FurnaceCommand;
import tk.shanebee.fakefurnace.listener.FurnaceListener;
import tk.shanebee.fakefurnace.machine.Furnace;
import tk.shanebee.fakefurnace.recipe.Fuel;
import tk.shanebee.fakefurnace.recipe.FurnaceRecipe;
import tk.shanebee.fakefurnace.task.FurnaceTick;
import tk.shanebee.fakefurnace.util.Util;

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
    private FurnaceTick furnaceTick;

    @Override
    public void onEnable() {
        instance = this;
        this.recipeManager = new RecipeManager(this);
        Util.log("Loading recipes...");
        registerRecipes();
        Util.log("Recipes loaded &asuccessfully!");
        Util.log("Loading fuels...");
        registerFuels();
        Util.log("Fuels loaded &asuccessfully!");

        Util.log("Loading furnaces...");
        this.furnaceManager = new FurnaceManager(this);
        Util.log("Loaded &b" + this.furnaceManager.getAllFurnaces().size() + " &7furnaces &asuccessfully!");
        this.furnaceTick = new FurnaceTick(this);

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        instance = null;
        Util.log("Saving &b" + this.furnaceManager.getAllFurnaces().size() + " &7furnaces...");
        this.furnaceManager.saveAll();
        Util.log("Furnaces saved &asuccessfully!");
        this.furnaceTick.cancel();
        this.furnaceTick = null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new FurnaceListener(this), this);
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        getCommand("furnace").setExecutor(new FurnaceCommand(this));
    }

    private void registerRecipes() {
        FurnaceRecipe chicken = new FurnaceRecipe(getKey("chicken"), Material.CHICKEN, Material.COOKED_CHICKEN, 200);
        FurnaceRecipe beef = new FurnaceRecipe(getKey("beef"), Material.BEEF, Material.COOKED_BEEF, 100);
        FurnaceRecipe pork = new FurnaceRecipe(getKey("pork"), Material.PORKCHOP, Material.COOKED_PORKCHOP, 20);

        this.recipeManager.registerFurnaceRecipe(chicken);
        this.recipeManager.registerFurnaceRecipe(beef);
        this.recipeManager.registerFurnaceRecipe(pork);
    }

    private void registerFuels() {
        /* KEEP for examples
        Fuel coal = new Fuel(getKey("coal"), Material.COAL, 1600);
        Fuel charcoal = new Fuel(getKey("charcoal"), Material.CHARCOAL, 1000);
        Fuel planks = new Fuel(getKey("planks"), Tag.PLANKS, 100);

        this.recipeManager.registerFuel(coal);
        this.recipeManager.registerFuel(charcoal);
        this.recipeManager.registerFuel(planks);
         */
        for (Fuel fuel : Fuel.getVanillaFuels()) {
            this.recipeManager.registerFuel(fuel);
        }
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

}
