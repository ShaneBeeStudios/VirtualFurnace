package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.chunk.MachineChunk;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.tile.Tile;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
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
        MachineChunk machineChunk = tileManager.getChunk(chunk);
        if (machineChunk != null) {
            Tile<?> tile = machineChunk.getTile(block);
            if (tile != null) {
                tile.activate(event.getPlayer());
            }
        }
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
        MachineChunk machineChunk = tileManager.getChunk(chunk);
        if (machineChunk != null) {
            machineChunk.setLoaded(load);
        }
    }

}
