package tk.shanebee.fakefurnace.task;

import org.bukkit.scheduler.BukkitRunnable;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.FurnaceManager;
import tk.shanebee.fakefurnace.machine.Furnace;

/**
 * Timer for ticking furnaces
 */
public class FurnaceTick extends BukkitRunnable {

    private final FurnaceManager furnaceManager;
    private int tick;

    public FurnaceTick(FakeFurnace plugin) {
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
