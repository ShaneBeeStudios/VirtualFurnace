package tk.shanebee.fakefurnace.debug;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.machine.Furnace;
import tk.shanebee.fakefurnace.util.Util;

/**
 * Debug class for stress testing furnaces
 */
public class Debug {

    private final FakeFurnace plugin;
    private boolean running;

    public Debug(FakeFurnace plugin) {
        this.plugin = plugin;
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
                    Furnace furnace = plugin.getFurnaceManager().createFurnace("test-furnace");
                    furnace.setFuel(new ItemStack(Material.COAL, 64));
                    furnace.setInput(new ItemStack(Material.CHICKEN, 64));
                }
                Util.log(sender, "Created " + amount + " furnaces in " + (System.currentTimeMillis() - start) + " milliseconds.");
                running = false;
            }
        };
        runnable.runTaskLaterAsynchronously(this.plugin, 0);
    }

}
