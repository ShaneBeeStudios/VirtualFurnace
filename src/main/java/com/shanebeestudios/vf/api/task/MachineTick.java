package com.shanebeestudios.vf.api.task;

import com.shanebeestudios.vf.api.manager.BrewerManager;
import com.shanebeestudios.vf.api.manager.FurnaceManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Brewer;
import com.shanebeestudios.vf.api.machine.Furnace;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Timer for ticking furnaces
 */
public class MachineTick extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private final BrewerManager brewerManager;
    private final VirtualFurnaceAPI virtualFurnaceAPI;
    private int tick;
    private int id;
    private boolean running;

    public MachineTick(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.virtualFurnaceAPI = virtualFurnaceAPI;
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.brewerManager = virtualFurnaceAPI.getBrewerManager();
        this.tick = 0;
    }

    public void start() {
        BukkitTask task = this.runTaskTimerAsynchronously(virtualFurnaceAPI.getJavaPlugin(), 15, 1);
        id = task.getTaskId();
    }

    @Override
    public void run() {
        running = true;
        try {
            for (Furnace furnace : furnaceManager.getAllMachines()) {
                if (!running) {
                    return;
                }
                furnace.tick();
            }
            for (Brewer brewer : brewerManager.getAllMachines()) {
                if (!running) {
                    return;
                }
                brewer.tick();
            }
        } catch (Exception ignore) {
        }
        tick++;
        if (tick >= 6000) {
            this.furnaceManager.saveAll();
            this.brewerManager.saveAll();
            this.tick = 0;
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        this.running = false;
        Bukkit.getScheduler().cancelTask(id);
    }

}
