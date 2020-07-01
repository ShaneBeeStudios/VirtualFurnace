package com.shanebeestudios.vf;

import com.shanebeestudios.vf.api.builder.ItemBuilder;
import com.shanebeestudios.vf.api.manager.BrewerManager;
import com.shanebeestudios.vf.api.manager.FurnaceManager;
import com.shanebeestudios.vf.api.RecipeManager;
import com.shanebeestudios.vf.api.manager.TileManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.recipe.BrewingFuel;
import com.shanebeestudios.vf.api.recipe.BrewingRecipe;
import com.shanebeestudios.vf.api.recipe.FurnaceFuel;
import com.shanebeestudios.vf.api.recipe.FurnaceRecipe;
import com.shanebeestudios.vf.api.util.Util;
import com.shanebeestudios.vf.command.FurnaceCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for VirtualFurnace plugin
 */
@SuppressWarnings("unused")
public class VirtualFurnace extends JavaPlugin {

    private boolean enabled = true;
    private static VirtualFurnace instance;
    private VirtualFurnaceAPI virtualFurnaceAPI;
    private RecipeManager recipeManager;
    private FurnaceManager furnaceManager;
    private BrewerManager brewerManager;
    private TileManager tileManager;

    // If ran as a Bukkit plugin, load the plugin
    @Override
    public void onEnable() {
        instance = this;
        long start = System.currentTimeMillis();
        Util.log("&7Setting up &bVirtualFurnaceAPI");
        this.virtualFurnaceAPI = new VirtualFurnaceAPI(this);
        if (!virtualFurnaceAPI.isEnabled()) {
            Util.log("Failed to load!");
            this.enabled = false;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        this.recipeManager = virtualFurnaceAPI.getRecipeManager();
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.brewerManager = virtualFurnaceAPI.getBrewerManager();
        this.tileManager = virtualFurnaceAPI.getTileManager();

        registerCommands();
        registerRecipes();
        registerFuels();
        Util.log("&7Setup &asuccessful&7 in &b" + (System.currentTimeMillis() - start) + " &7milliseconds");
    }

    @Override
    public void onDisable() {
        instance = null;
        /*
        if (!enabled) {
            return;
        }
         */
        this.tileManager = null;
        this.furnaceManager = null;
        this.recipeManager = null;
        this.virtualFurnaceAPI.disableAPI();
        this.virtualFurnaceAPI = null;
        Bukkit.getScheduler().cancelTasks(this);
        /*
        this.furnaceTick.cancel();
        Util.log("Saving &b" + this.furnaceManager.getAllFurnaces().size() + " &7furnaces...");
        this.furnaceManager.saveAll();
        Util.log("Furnaces saved &asuccessfully!");
        Util.log("Saving &b" + this.tileManager.getAllTiles().size() + " &7tiles...");
        this.tileManager.saveAllTiles();
        Util.log("Tiles saved &asuccessfully!");
         */
    }

    @SuppressWarnings("ConstantConditions")
    private void registerCommands() {
        getCommand("furnace").setExecutor(new FurnaceCommand(this));
    }

    private void registerRecipes() {
        for (FurnaceRecipe recipe : FurnaceRecipe.getVanillaFurnaceRecipes()) {
            this.recipeManager.registerFurnaceRecipe(recipe);
        }
        for (BrewingRecipe recipe : BrewingRecipe.getVanillaBrewingRecipes()) {
            this.recipeManager.registerBrewingRecipe(recipe);
        }

        ItemStack ing = new ItemStack(Material.GLOWSTONE_DUST);
        ItemStack in = new ItemBuilder(Material.POTATO, 1).build();
        ItemStack out = new ItemBuilder(Material.BAKED_POTATO).name("&aGlowing Potato").addEnchant(Enchantment.DAMAGE_ALL, 1).hideEnchants().build();

        BrewingRecipe rec = new BrewingRecipe(Util.getKey("potato_glow"), ing, in, out, 400);
        this.recipeManager.registerBrewingRecipe(rec);
    }

    private void registerFuels() {
        for (FurnaceFuel furnaceFuel : FurnaceFuel.getVanillaFurnaceFuels()) {
            this.recipeManager.registerFuel(furnaceFuel);
        }
        this.recipeManager.registerFuel(new FurnaceFuel(Util.getKey("test_diamond"), Material.DIAMOND, 200));
        this.recipeManager.registerFuel(new FurnaceFuel(Util.getKey("test_snowball"), Material.SNOWBALL, 30));
        this.recipeManager.registerBrewingFuel(BrewingFuel.BLAZE_POWDER);
    }

    /**
     * Get an instance of this plugin
     *
     * @return Instance of this plugin
     */
    public static VirtualFurnace getPlugin() {
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

    public BrewerManager getBrewerManager() {
        return brewerManager;
    }

    /**
     * Get an instance of the {@link VirtualFurnaceAPI}
     *
     * @return Instance of VirtualFurnaceAPI
     */
    public VirtualFurnaceAPI getVirtualFurnaceAPI() {
        return virtualFurnaceAPI;
    }

}
