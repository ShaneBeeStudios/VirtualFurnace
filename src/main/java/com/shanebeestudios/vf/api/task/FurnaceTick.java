package com.shanebeestudios.vf.api.task;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import org.bukkit.scheduler.BukkitRunnable;
import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.machine.Furnace;

/**
 * Timer for ticking furnaces
 */
public class FurnaceTick extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private int tick;

    public FurnaceTick(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.tick = 0;
        this.runTaskTimerAsynchronously(virtualFurnaceAPI.getJavaPlugin(), 1, 1);
    }

    @Override
    public void run() {
        try {
            this.furnaceManager.getAllFurnaces().forEach(Furnace::tick);
        } catch (Exception ignore) {}
        tick++;
        if (tick >= 6000) {
            this.furnaceManager.saveAll();
            this.tick = 0;
        }
    }

}
