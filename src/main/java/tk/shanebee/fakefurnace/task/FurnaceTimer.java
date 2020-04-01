package tk.shanebee.fakefurnace.task;

import org.bukkit.scheduler.BukkitRunnable;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.FurnaceManager;
import tk.shanebee.fakefurnace.machine.Furnace;

public class FurnaceTimer extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private int tick;

    public FurnaceTimer(FakeFurnace plugin) {
        this.furnaceManager = plugin.getFurnaceManager();
        this.tick = 0;
        this.runTaskTimer(plugin, 1, 1);
    }

    @Override
    public void run() {
        for (Furnace furnace : this.furnaceManager.getFurnaceMap().values()) {
            furnace.tick();
        }
        tick++;
        if (tick >= 6000) {
            this.furnaceManager.saveAll();
            this.tick = 0;
        }
    }

}
