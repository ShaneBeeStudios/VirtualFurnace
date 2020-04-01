package tk.shanebee.fakefurnace.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.FurnaceManager;
import tk.shanebee.fakefurnace.machine.Furnace;
import tk.shanebee.fakefurnace.util.Util;

import java.util.UUID;

public class FurnaceCommand implements CommandExecutor {

    private final FakeFurnace plugin;
    private final FurnaceManager furnaceManager;
    private final NamespacedKey key;

    public FurnaceCommand(FakeFurnace plugin) {
        this.plugin = plugin;
        this.furnaceManager = plugin.getFurnaceManager();
        this.key = new NamespacedKey(plugin, "furnace");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length >= 1) {
                switch (args[0]) {
                    case "new":
                    case "create":
                        if (args.length == 2) {
                            String title = ChatColor.translateAlternateColorCodes('&', args[1]);
                            Furnace furnace = this.furnaceManager.createFurnace(title);
                            furnace.openInventory(player);
                        } else {
                            sender.sendMessage("Pick a name for your new inventory!");
                        }
                        break;
                    case "open":
                        if (args.length == 2) {
                            UUID uuid = UUID.fromString(args[1]);
                            Furnace furnace1 = this.furnaceManager.getByID(uuid);
                            if (furnace1 != null) {
                                furnace1.openInventory(player);
                            } else {
                                player.sendMessage("FURNACE NOT FOUND!");
                            }
                        } else {
                            sender.sendMessage("Please enter an ID of a furnace to open");
                        }
                        break;
                    case "item":
                        if (args.length == 1) {
                            sender.sendMessage("Options: <new>/<clone>");
                            return true;
                        }
                        switch (args[1]) {
                            case "new":
                                Furnace furnace2 = this.furnaceManager.createFurnace("&3Portable Furnace");
                                player.getInventory().addItem(getFurnaceItem(furnace2.getUuid()));
                                break;
                            case "clone":
                                ItemStack item = player.getInventory().getItemInMainHand();
                                player.getInventory().addItem(item.clone());
                                break;
                        }
                        break;
                    case "id":
                        ItemStack item = player.getInventory().getItemInMainHand();
                        UUID uuid = getUUIDFromItem(item);
                        if (uuid != null) {
                            player.sendMessage("Portable furnace ID: " + uuid.toString());
                        } else {
                            player.sendMessage("This item has no ID!");
                        }
                }
            }

        }
        return true;
    }

    private ItemStack getFurnaceItem(UUID uuid) {
        ItemStack itemStack = new ItemStack(Material.FURNACE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Util.getColString("&3Portable Furnace"));
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, uuid.toString());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private UUID getUUIDFromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta.getPersistentDataContainer().has(this.key, PersistentDataType.STRING)) {
            String u = meta.getPersistentDataContainer().get(this.key, PersistentDataType.STRING);
            return UUID.fromString(u);
        }
        return null;
    }

}
