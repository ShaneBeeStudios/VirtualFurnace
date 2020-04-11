package com.shanebeestudios.vf.debug;

import com.shanebeestudios.vf.VirtualFurnace;
import com.shanebeestudios.vf.api.TileManager;
import com.shanebeestudios.vf.api.chunk.VirtualChunk;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.tile.FurnaceTile;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Debug class for stress testing furnaces
 */
public class Debug {

    private final VirtualFurnace plugin;
    private final TileManager tileManager;
    private boolean running;

    public Debug(VirtualFurnace plugin) {
        this.plugin = plugin;
        this.tileManager = plugin.getVirtualFurnaceAPI().getTileManager();
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void loadDebugFurnaces(int amount, CommandSender sender) {
        this.running = true;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (int i = 0; i < amount; i++) {
                    plugin.getFurnaceManager().createFurnace("test-furnace", furnace -> {
                        furnace.setFuel(new ItemStack(Material.COAL, 64));
                        furnace.setInput(new ItemStack(Material.CHICKEN, 64));
                    });
                }
                Util.log(sender, "Created " + amount + " furnaces in " + (System.currentTimeMillis() - start) + " milliseconds.");
                running = false;
            }
        };
        runnable.runTaskLaterAsynchronously(this.plugin, 0);
    }

    public void loadDebugTiles(int debug) {
        World world = Bukkit.getWorlds().get(0);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = 0; x < debug; x++) {
                    for (int z = 0; z < debug; z++) {
                        Furnace furnace = new Furnace("test");
                        furnace.setFuel(new ItemStack(Material.COAL, 64));
                        furnace.setInput(new ItemStack(Material.CHICKEN, 64));
                        FurnaceTile furnaceTile = tileManager.createFurnaceTile(x << 4, 1, z << 4, world, furnace);
                        VirtualChunk virtualChunk = tileManager.getChunk(furnaceTile.getBlock().getChunk());
                        tileManager.loadChunk(virtualChunk);
                        virtualChunk.addPluginChunkTicket(plugin);
                    }
                }

                Util.log("Chunks: &b" + tileManager.getChunks().size() + "&7 Loaded: &b" + tileManager.getLoadedChunks().size());
            }
        };
        runnable.runTaskLaterAsynchronously(this.plugin, 0);
    }

}
