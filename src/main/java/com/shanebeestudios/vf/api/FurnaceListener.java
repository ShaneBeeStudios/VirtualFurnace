package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.chunk.VirtualChunk;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.tile.Tile;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

class FurnaceListener implements Listener {

    private final FurnaceManager furnaceManager;
    private final TileManager tileManager;

    FurnaceListener(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.tileManager = virtualFurnaceAPI.getTileManager();
    }

    @EventHandler
    private void onClickFurnace(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();
        if (hand != null) {
            Furnace furnace = this.furnaceManager.getFurnaceFromItemStack(hand);
            if (furnace != null) {
                event.setCancelled(true);
                furnace.openInventory(event.getPlayer());
                return;
            }
        }
        Block block = event.getClickedBlock();
        if (block == null) return;

        if (event.getHand() != EquipmentSlot.HAND) return; // TODO temp for debugging
        Chunk chunk = block.getChunk();
        VirtualChunk virtualChunk = tileManager.getChunk(chunk);
        if (virtualChunk != null) {
            Tile<?> tile = virtualChunk.getTile(block);
            if (tile != null) {
                tile.activate(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        HumanEntity clicker = event.getWhoClicked();
        if (holder instanceof Furnace && clicker instanceof Player) {
            int slot = event.getRawSlot();
            if (slot == 2) {
                handleOutput(((Player) clicker), ((Furnace) holder));
            } else if (slot == 1) {
                handleFuel();
            }
        }
    }

    private void handleOutput(Player player, Furnace furnace) {
        ItemStack output = furnace.getOutput();
        if (output == null) return;

        float exp = furnace.extractExperience();
        player.giveExp((int) exp);
    }

    private void handleFuel() {
        // TODO this will be used for custom fuels
    }

    @EventHandler
    private void onChunkLoad(ChunkLoadEvent event) {
        handleChunk(event.getChunk(), true);
    }

    @EventHandler
    private void onChunkUnload(ChunkUnloadEvent event) {
        handleChunk(event.getChunk(), false);
    }

    private void handleChunk(Chunk chunk, boolean load) {
        VirtualChunk virtualChunk = tileManager.getChunk(chunk);
        if (virtualChunk != null) {
            if (load) {
                if (virtualChunk.isLoaded()) return;
                tileManager.loadChunk(virtualChunk);
            } else {
                if (virtualChunk.isForceLoaded()) return;
                tileManager.unloadChunk(virtualChunk);
            }
        }
    }

}
