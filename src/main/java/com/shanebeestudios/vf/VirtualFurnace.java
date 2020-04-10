package com.shanebeestudios.vf;

import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.RecipeManager;
import com.shanebeestudios.vf.api.TileManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.recipe.Fuel;
import com.shanebeestudios.vf.api.recipe.FurnaceRecipe;
import com.shanebeestudios.vf.api.task.FurnaceTick;
import com.shanebeestudios.vf.api.util.Util;
import com.shanebeestudios.vf.command.FurnaceCommand;
import org.bukkit.Bukkit;
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
    }

    private void registerFuels() {
        for (Fuel fuel : Fuel.getVanillaFuels()) {
            this.recipeManager.registerFuel(fuel);
        }
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

    /**
     * Get an instance of the {@link VirtualFurnaceAPI}
     *
     * @return Instance of VirtualFurnaceAPI
     */
    public VirtualFurnaceAPI getVirtualFurnaceAPI() {
        return virtualFurnaceAPI;
    }

}
