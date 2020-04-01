package tk.shanebee.fakefurnace.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.FurnaceManager;
import tk.shanebee.fakefurnace.machine.Furnace;
import tk.shanebee.fakefurnace.util.Util;

import java.util.UUID;

public class FurnaceCommand implements CommandExecutor {

    private final FurnaceManager furnaceManager;

    public FurnaceCommand(FakeFurnace plugin) {
        this.furnaceManager = plugin.getFurnaceManager();
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
                                ItemStack itemStack = new ItemStack(Material.FURNACE);
                                String name = "&3Portable Furnace";
                                ItemMeta meta = itemStack.getItemMeta();
                                assert meta != null;
                                meta.setDisplayName(Util.getColString(name));
                                itemStack.setItemMeta(meta);
                                player.getInventory().addItem(this.furnaceManager.createItemWithFurnace(name, itemStack, true));
                                break;
                            case "clone":
                                ItemStack item = player.getInventory().getItemInMainHand();
                                player.getInventory().addItem(item.clone());
                                break;
                        }
                        break;
                    case "id":
                        ItemStack item = player.getInventory().getItemInMainHand();
                        Furnace furnace = this.furnaceManager.getFurnaceFromItemStack(item);
                        if (furnace != null) {
                            player.sendMessage("Portable furnace ID: " + furnace.getUuid().toString());
                        } else {
                            player.sendMessage("This item has no ID!");
                        }
                }
            }

        }
        return true;
    }

}
