package com.shanebeestudios.vf.task;

import org.bukkit.scheduler.BukkitRunnable;
import com.shanebeestudios.vf.VirtualFurnace;
import com.shanebeestudios.vf.FurnaceManager;
import com.shanebeestudios.vf.machine.Furnace;

/**
 * Timer for ticking furnaces
 */
public class FurnaceTick extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private int tick;

    public FurnaceTick(VirtualFurnace plugin) {
        this.furnaceManager = plugin.getFurnaceManager();
        this.tick = 0;
        this.runTaskTimerAsynchronously(plugin, 1, 1);
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
