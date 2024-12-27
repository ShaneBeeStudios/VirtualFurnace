package com.shanebeestudios.vf.api;

import com.shanebeestudios.vf.api.chunk.VirtualChunk;
import com.shanebeestudios.vf.api.event.machine.FurnaceExtractEvent;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.machine.Machine;
import com.shanebeestudios.vf.api.recipe.Fuel;
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
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

class FurnaceListener implements Listener {

    private final FurnaceManager furnaceManager;
    private final RecipeManager recipeManager;
    private final TileManager tileManager;

    FurnaceListener(VirtualFurnaceAPI virtualFurnaceAPI) {
        this.furnaceManager = virtualFurnaceAPI.getFurnaceManager();
        this.recipeManager = virtualFurnaceAPI.getRecipeManager();
        this.tileManager = virtualFurnaceAPI.getTileManager();
    }

    @EventHandler
    private void onClickFurnace(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack hand = event.getItem();
        Player player = event.getPlayer();
        if (hand != null) {
            Machine machine = this.furnaceManager.getMachineFromItemStack(hand);
            if (machine != null) {
                event.setCancelled(true);
                machine.openInventory(player);
                return;
            }
        }
        Block block = event.getClickedBlock();
        if (block == null) return;

        Chunk chunk = block.getChunk();
        VirtualChunk virtualChunk = tileManager.getChunk(chunk);
        if (virtualChunk != null) {
            Tile<?> tile = virtualChunk.getTile(block);
            if (tile != null) {
                event.setCancelled(true);
                if (event.getHand() != EquipmentSlot.OFF_HAND) {
                    tile.activate(player);
                }
            }
        }
    }


    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        HumanEntity clicker = event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof Furnace furnace && clicker instanceof Player player) {
            int slot = event.getRawSlot();
            // Give XP to player when they extract from the furnace
            if (slot == 2) {
                ItemStack output = furnace.getOutput();
                if (output != null) {
                    int exp = (int) furnace.extractExperience();
                    // Call the furnace extract event
                    FurnaceExtractEvent extractEvent = new FurnaceExtractEvent(furnace, player, output, exp);
                    extractEvent.callEvent();

                    ((Player) clicker).giveExp(extractEvent.getExperience());
                    event.setCurrentItem(extractEvent.getItemStack());
                }
            }
            // Enable putting custom fuels in the furnaces
            else if (slot == 1) {
                ItemStack cursor = clicker.getItemOnCursor();

                Fuel fuel = recipeManager.getFuelByMaterial(cursor.getType());
                if (fuel != null && isNotVanillaFuel(cursor)) {
                    ItemStack furnaceFuel = furnace.getFuel();
                    event.setCancelled(true);
                    if (furnaceFuel != null && furnaceFuel.getType() == cursor.getType()) {
                        InventoryView view = event.getView();
                        int fuelAmount = furnaceFuel.getAmount();
                        int cursorAmount = cursor.getAmount();
                        int maxStack = cursor.getType().getMaxStackSize();

                        ItemStack fuelSlot = view.getItem(1);
                        assert fuelSlot != null;
                        if (fuelAmount < maxStack) {
                            int diff = maxStack - fuelAmount;
                            if (cursorAmount < diff) {
                                cursor.setAmount(0);
                                fuelSlot.setAmount(fuelAmount + cursorAmount);
                            } else {
                                cursor.setAmount(cursorAmount - diff);
                                fuelSlot.setAmount(maxStack);
                            }
                            ((Player) clicker).updateInventory();
                        }

                    } else {
                        ItemStack oldCursor = cursor.clone();
                        clicker.setItemOnCursor(furnaceFuel);
                        event.getView().setItem(1, oldCursor);
                    }
                }
            }
        }
    }

    private boolean isNotVanillaFuel(ItemStack itemStack) {
        for (Fuel fuel : Fuel.getVanillaFuels()) {
            if (fuel.getFuelMaterial() == itemStack.getType()) {
                return false;
            }
        }
        return true;
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
