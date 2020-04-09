package com.shanebeestudios.vf.api.task;

import com.shanebeestudios.vf.api.TileManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.chunk.VirtualChunk;
import org.bukkit.scheduler.BukkitRunnable;

public class TileTick extends BukkitRunnable {

    private final TileManager tileManager;
    private int tick;

    public TileTick(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.tileManager = virtualFurnaceAPI.getTileManager();
        this.tick = 0;
        this.runTaskTimerAsynchronously(virtualFurnaceAPI.getJavaPlugin(), 1, 1);
    }

    @Override
    public void run() {
        try {
            this.tileManager.getLoadedChunks().forEach(VirtualChunk::tick);
        } catch (Exception ignore) {}
        tick++;
        if (tick >= 6000) {
            this.tileManager.saveAllTiles();
            this.tick = 0;
        }
    }

}
