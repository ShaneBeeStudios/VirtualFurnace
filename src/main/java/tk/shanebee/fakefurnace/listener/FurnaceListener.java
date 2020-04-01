package tk.shanebee.fakefurnace.listener;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.machine.Furnace;

import java.util.UUID;

public class FurnaceListener implements Listener {

    private FakeFurnace plugin;
    private NamespacedKey key;

    public FurnaceListener(FakeFurnace plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "furnaceID");
    }

    @EventHandler
    private void onClickFurnace(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack hand = event.getItem();
        if (hand == null) return;

        ItemMeta meta = hand.getItemMeta();
        assert meta != null;
        if (meta.getDisplayName().contains("Portable")) {
            event.setCancelled(true);
            if (meta.getPersistentDataContainer().has(this.key, PersistentDataType.STRING)) {
                String u = meta.getPersistentDataContainer().get(this.key, PersistentDataType.STRING);
                UUID uuid = UUID.fromString(u);
                Furnace furnace = this.plugin.getFurnaceManager().getByID(uuid);
                if (furnace != null) {
                    furnace.openInventory(event.getPlayer());
                }
            }
        }
    }
}
