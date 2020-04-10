package com.shanebeestudios.vf.command;

import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.VirtualFurnace;
import com.shanebeestudios.vf.api.TileManager;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import com.shanebeestudios.vf.debug.Debug;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import com.shanebeestudios.vf.api.machine.Furnace;

import java.util.UUID;

public class FurnaceCommand implements CommandExecutor {

    private final FurnaceManager furnaceManager;
    private final TileManager tileManager;
    private final Debug debug;

    public FurnaceCommand(VirtualFurnace plugin) {
        this.furnaceManager = plugin.getFurnaceManager();
        this.tileManager = plugin.getVirtualFurnaceAPI().getTileManager();
        this.debug = new Debug(plugin);
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

                                ItemStack smokerItem = new ItemStack(Material.SMOKER);
                                String smokerName = "&2Portable Smoker";
                                ItemMeta smokerMeta = smokerItem.getItemMeta();
                                assert smokerMeta != null;
                                smokerMeta.setDisplayName(Util.getColString(smokerName));
                                smokerItem.setItemMeta(smokerMeta);
                                player.getInventory().addItem(this.furnaceManager.createItemWithFurnace(smokerName, FurnaceProperties.SMOKER,smokerItem, true));

                                ItemStack fastItem = new ItemStack(Material.BLAST_FURNACE);
                                String fastName = "&6Fast Food";
                                ItemMeta fastMeta = fastItem.getItemMeta();
                                assert fastMeta != null;
                                fastMeta.setDisplayName(Util.getColString(fastName));
                                fastItem.setItemMeta(fastMeta);
                                FurnaceProperties properties = new FurnaceProperties("fast").cookMultiplier(10.0);
                                ItemStack i = this.furnaceManager.createItemWithFurnace(fastName, properties, fastItem, true);
                                player.getInventory().addItem(i);
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
                            player.sendMessage("Portable furnace ID: " + furnace.getUniqueID().toString());
                        } else {
                            player.sendMessage("This item has no ID!");
                        }
                        break;
                    case "tile":
                        Location loc = player.getTargetBlockExact(20).getLocation();
                        tileManager.createFurnaceTile(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), player.getWorld(), "test-smoker", FurnaceProperties.SMOKER);
                        player.sendMessage("Tile created");
                        break;
                    case "debug":
                        if (args.length == 1) {
                            sender.sendMessage("How much ya wanna debug?");
                            return true;
                        }
                        int amount = Integer.parseInt(args[1]);
                        if (debug.isRunning()) {
                            player.sendMessage("Debugger currently running... please wait!");
                            return true;
                        }
                        //sender.sendMessage("Debugging " + amount + " new furnaces!");
                        //debug.loadDebugFurnaces(amount, player);
                        debug.loadDebugTiles(amount);
                        break;
                    case "check":
                        int furnaces = furnaceManager.getAllFurnaces().size();
                        int tiles = tileManager.getAllTiles().size();
                        int chunks = tileManager.getChunks().size();
                        int loadedChunks = tileManager.getLoadedChunks().size();
                        Util.log("&dCheck:");
                        Util.log(" - Chunks: &b" + loadedChunks + "/" + chunks);
                        Util.log(" - Tiles: &a" + tiles);
                        Util.log(" - Furnaces: &c" + furnaces);

                }
            }

        }
        return true;
    }

}
