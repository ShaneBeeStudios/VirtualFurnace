package tk.shanebee.fakefurnace.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.FurnaceManager;
import tk.shanebee.fakefurnace.machine.Furnace;

public class FurnaceListener implements Listener {

    private final FurnaceManager furnaceManager;

    public FurnaceListener(FakeFurnace plugin) {
        this.furnaceManager = plugin.getFurnaceManager();
    }

    @EventHandler
    private void onClickFurnace(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();
        if (hand == null) return;

        Furnace furnace = this.furnaceManager.getFurnaceFromItemStack(hand);
        if (furnace != null) {
            event.setCancelled(true);
            furnace.openInventory(event.getPlayer());
        }
    }

}
