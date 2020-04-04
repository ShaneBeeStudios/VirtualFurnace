package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.task.FurnaceTick;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class VirtualFurnaceAPI {

    static {
        ConfigurationSerialization.registerClass(Furnace.class, "furnace");
    }

    private static VirtualFurnaceAPI instance;
    private final JavaPlugin plugin;
    private final RecipeManager recipeManager;
    private final FurnaceManager furnaceManager;
    private final FurnaceTick furnaceTick;

    public VirtualFurnaceAPI(JavaPlugin javaPlugin) {
        instance = this;
        this.plugin = javaPlugin;
        this.recipeManager = new RecipeManager();
        this.furnaceManager = new FurnaceManager(this);
        this.furnaceTick = new FurnaceTick(this);
        Bukkit.getPluginManager().registerEvents(new FurnaceListener(this), javaPlugin);
    }

    public static VirtualFurnaceAPI getInstance() {
        return instance;
    }

    public JavaPlugin getJavaPlugin() {
        return plugin;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public FurnaceManager getFurnaceManager() {
        return furnaceManager;
    }

    public FurnaceTick getFurnaceTick() {
        return furnaceTick;
    }

}
