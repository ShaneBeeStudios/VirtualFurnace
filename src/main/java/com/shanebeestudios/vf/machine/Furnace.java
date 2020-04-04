package com.shanebeestudios.vf.machine;

import com.shanebeestudios.vf.RecipeManager;
import com.shanebeestudios.vf.VirtualFurnace;
import com.shanebeestudios.vf.recipe.Fuel;
import com.shanebeestudios.vf.recipe.FurnaceRecipe;
import com.shanebeestudios.vf.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class Furnace implements InventoryHolder, ConfigurationSerializable {

    // cook time = 200
    // fuel time = 1600

    private final String name;
    private final UUID uuid;
    private final RecipeManager recipeManager;
    private int cookTime;
    private int cookTimeTotal;
    private int fuelTime;
    private int fuelTimeTotal;
    private ItemStack fuel;
    private ItemStack input;
    private ItemStack output;
    private final Inventory inventory;

    public Furnace(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.recipeManager = VirtualFurnace.getPlugin().getRecipeManager();
        this.cookTime = 0;
        this.cookTimeTotal = 0;
        this.fuelTime = 0;
        this.fuelTimeTotal = 0;
        this.fuel = null;
        this.input = null;
        this.output = null;
        this.inventory = Bukkit.createInventory(this, InventoryType.FURNACE, Util.getColString(name));
        this.updateInventory();
    }

    private Furnace(String name, UUID uuid, int cookTime, int fuelTime, ItemStack fuel, ItemStack input, ItemStack output) {
        this.name = name;
        this.uuid = uuid;
        this.recipeManager = VirtualFurnace.getPlugin().getRecipeManager();
        this.cookTime = cookTime;
        this.fuelTime = fuelTime;
        this.fuel = fuel;
        this.input = input;
        this.output = output;

        FurnaceRecipe furnaceRecipe = recipeManager.getByIngredient(input != null ? input.getType() : null);
        if (furnaceRecipe != null) {
            this.cookTimeTotal = furnaceRecipe.getCookTime();
        } else {
            this.cookTimeTotal = 0;
        }
        Fuel fuelF = recipeManager.getFuelByMaterial(fuel != null ? fuel.getType() : null);
        if (fuelF != null) {
            this.fuelTimeTotal = fuelF.getBurnTime();
        } else {
            this.fuelTimeTotal = 0;
        }
        this.inventory = Bukkit.createInventory(this, InventoryType.FURNACE, Util.getColString(name));
        this.updateInventory();
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    public ItemStack getInput() {
        return input;
    }

    public void setInput(ItemStack input) {
        this.input = input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public void openInventory(Player player) {
        updateInventory();
        player.openInventory(this.inventory);
    }

    private void updateInventory() {
        this.inventory.setItem(0, this.input);
        this.inventory.setItem(1, this.fuel);
        this.inventory.setItem(2, this.output);
    }

    private void updateInventoryView() {
        ItemStack input = this.inventory.getItem(0);
        if (this.input != input) {
            this.input = input;
        }
        ItemStack fuel = this.inventory.getItem(1);
        if (this.fuel != fuel) {
            this.fuel = fuel;
        }
        ItemStack output = this.inventory.getItem(2);
        if (this.output != output) {
            this.output = output;
        }
        for (HumanEntity entity : this.inventory.getViewers()) {
            InventoryView view = entity.getOpenInventory();
            view.setProperty(InventoryView.Property.COOK_TIME, this.cookTime);
            view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, this.cookTimeTotal);
            view.setProperty(InventoryView.Property.BURN_TIME, this.fuelTime);
            view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, this.fuelTimeTotal);
        }
    }

    /*
    private void updateInventoryFromView() {
        for (HumanEntity entity : this.inventory.getViewers()) {
            InventoryView view = entity.getOpenInventory();
            this.input = view.getItem(0);
            this.fuel = view.getItem(1);
            this.output = view.getItem(2);
        }
    }
     */

    public void tick() {
        if (this.fuelTime > 0) {
            this.fuelTime--;
            if (canCook()) {
                this.cookTime++;
                if (this.cookTime >= this.cookTimeTotal) {
                    this.cookTime = 0;
                    processCook();
                }
            } else {
                this.cookTime = 0;
            }
        } else if (canBurn() && canCook()) {
            processBurn();
        } else if (this.cookTime > 0) {
            if (canCook()) {
                this.cookTime -= 5;
            } else {
                this.cookTime = 0;
            }
        }
        updateInventoryView();
    }

    private boolean canBurn() {
        if (this.fuel == null) return false;
        return this.recipeManager.getFuelByMaterial(this.fuel.getType()) != null;
    }

    private void processBurn() {
        Fuel fuel = this.recipeManager.getFuelByMaterial(this.fuel.getType());
        if (fuel == null) return;
        int fuelAmount = this.fuel.getAmount();
        if (fuelAmount > 1) {
            this.fuel.setAmount(fuelAmount - 1);
        } else {
            this.fuel = null;
        }
        this.fuelTime = fuel.getBurnTime();
        this.fuelTimeTotal = fuel.getBurnTime();
        updateInventory();
    }

    private boolean canCook() {
        if (this.input == null) return false;
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return false;
        this.cookTimeTotal = result.getCookTime();
        return this.output == null || this.output.getType() == result.getResult();
    }

    private void processCook() {
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return;
        if (this.output == null) {
            this.output = new ItemStack(result.getResult());
        } else {
            this.output.setAmount(this.output.getAmount() + 1);
        }
        int inputAmount = this.input.getAmount();
        if (inputAmount > 1) {
            this.input.setAmount(inputAmount - 1);
        } else {
            this.input = null;
        }
        updateInventory();
    }

    @Override
    public String toString() {
        return "FakeFurnace{" +
                "name='" + ChatColor.stripColor(name) + '\'' +
                ", uuid=" + uuid +
                ", cookTime=" + cookTime +
                ", fuelTime=" + fuelTime +
                '}';
    }

    // Serializer for config
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.name);
        result.put("uuid", this.uuid.toString());
        result.put("cookTime", this.cookTime);
        result.put("fuelTime", this.fuelTime);
        result.put("fuel", this.fuel);
        result.put("input", this.input);
        result.put("output", this.output);
        return result;
    }

    public static Furnace deserialize(Map<String, Object> args) {
        String name = ((String) args.get("name"));
        UUID uuid = UUID.fromString(((String) args.get("uuid")));
        int cookTime = ((Number) args.get("cookTime")).intValue();
        int fuelTime = ((Number) args.get("fuelTime")).intValue();
        ItemStack fuel = ((ItemStack) args.get("fuel"));
        ItemStack input = ((ItemStack) args.get("input"));
        ItemStack output = ((ItemStack) args.get("output"));
        return new Furnace(name, uuid, cookTime, fuelTime, fuel, input, output);
    }

}
