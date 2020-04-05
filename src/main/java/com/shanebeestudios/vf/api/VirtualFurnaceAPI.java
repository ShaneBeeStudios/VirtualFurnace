package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.task.FurnaceTick;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main API for VirtualFurnace
 */
@SuppressWarnings("unused")
public class VirtualFurnaceAPI {

    static {
        ConfigurationSerialization.registerClass(Furnace.class, "furnace");
    }

    private static VirtualFurnaceAPI instance;
    private final JavaPlugin plugin;
    private final RecipeManager recipeManager;
    private final FurnaceManager furnaceManager;
    private final FurnaceTick furnaceTick;

    /**
     * Create a new instance of the VirtualFurnaceAPI
     *
     * @param javaPlugin Your plugin
     */
    public VirtualFurnaceAPI(JavaPlugin javaPlugin) {
        instance = this;
        this.plugin = javaPlugin;
        this.recipeManager = new RecipeManager();
        this.furnaceManager = new FurnaceManager(this);
        this.furnaceTick = new FurnaceTick(this);
        Bukkit.getPluginManager().registerEvents(new FurnaceListener(this), javaPlugin);
    }

    /**
     * Disable the furnace tick
     * <p>This is good to use in your onDisable() method, to prevent tasks still running on server shutdown/reload</p>
     */
    public void disableFurnaceTick() {
        this.furnaceTick.cancel();
    }

    /**
     * Get a static instance of the VirtualFurnaceAPI
     * <p><b>NOTE:</b> You have to create once first or this will return null</p>
     *
     * @return Instance of the VirtualFurnaceAPI
     */
    public static VirtualFurnaceAPI getInstance() {
        return instance;
    }

    /**
     * Get the plugin that was passed into this instance
     *
     * @return Plugin in this instance
     */
    public JavaPlugin getJavaPlugin() {
        return plugin;
    }

    /**
     * Get an instance of the recipe manager
     *
     * @return Instance of recipe manager
     */
    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    /**
     * Get an instance of the furnace manager
     *
     * @return Instance of the furnace manager
     */
    public FurnaceManager getFurnaceManager() {
        return furnaceManager;
    }

    /**
     * Get an instance of the furnace tick class
     *
     * @return Instance of furnace tick
     */
    public FurnaceTick getFurnaceTick() {
        return furnaceTick;
    }

}
