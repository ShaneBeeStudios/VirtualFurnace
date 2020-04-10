package com.shanebeestudios.vf.api.task;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.scheduler.BukkitTask;

/**
 * Timer for ticking furnaces
 */
public class FurnaceTick extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private final VirtualFurnaceAPI virtualFurnaceAPI;
    private int tick;
    private BukkitTask task;
    private int id;
    private boolean running;

    public FurnaceTick(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.virtualFurnaceAPI = virtualFurnaceAPI;
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.tick = 0;
    }

    public void start() {
        task = this.runTaskTimerAsynchronously(virtualFurnaceAPI.getJavaPlugin(), 15, 1);
        id = task.getTaskId();
    }

    @Override
    public void run() {
        running = true;
        try {
            this.furnaceManager.getAllFurnaces().forEach(Furnace::tick);
            for (Furnace furnace : furnaceManager.getAllFurnaces()) {
                if (!running) {
                    return;
                }
                furnace.tick();
            }
        } catch (Exception ignore) {}
        tick++;
        if (tick >= 6000) {
            this.furnaceManager.saveAll();
            this.tick = 0;
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        this.running = false;
        Bukkit.getScheduler().cancelTask(id);
        Util.log("Cancelling FurnaceTick with id: " + id);
    }

}
