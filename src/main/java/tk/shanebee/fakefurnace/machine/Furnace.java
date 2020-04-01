package tk.shanebee.fakefurnace.machine;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import tk.shanebee.fakefurnace.FakeFurnace;
import tk.shanebee.fakefurnace.recipe.Fuel;
import tk.shanebee.fakefurnace.recipe.FurnaceRecipe;
import tk.shanebee.fakefurnace.RecipeManager;

import java.util.UUID;

public class Furnace implements InventoryHolder {

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
        this.name = ChatColor.stripColor(name);
        this.uuid = UUID.randomUUID();
        this.recipeManager = FakeFurnace.getPlugin().getRecipeManager();
        this.cookTime = 0;
        this.cookTimeTotal = 0;
        this.fuelTime = 0;
        this.fuelTimeTotal = 0;
        this.fuel = null;
        this.input = null;
        this.output = null;
        this.inventory = Bukkit.createInventory(this, InventoryType.FURNACE, name);
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
        for (HumanEntity entity : this.inventory.getViewers()) {
            InventoryView view = entity.getOpenInventory();
            this.input = view.getItem(0);
            this.fuel = view.getItem(1);
            this.output = view.getItem(2);
            view.setProperty(InventoryView.Property.COOK_TIME, this.cookTime);
            view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, this.cookTimeTotal);
            view.setProperty(InventoryView.Property.BURN_TIME, this.fuelTime);
            view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, this.fuelTimeTotal);
        }
    }

    private void updateInventoryFromView() {
        for (HumanEntity entity : this.inventory.getViewers()) {
            InventoryView view = entity.getOpenInventory();
            this.input = view.getItem(0);
            this.fuel = view.getItem(1);
            this.output = view.getItem(2);
        }
    }

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
        } else {
            if (canBurn() && canCook()) {
                processBurn();
            } else {
                if (canCook() && this.cookTime > 0) {
                    this.cookTime -= 10;
                }
            }
        }
        updateInventoryView();
    }

    private boolean canBurn() {
        if (this.fuel == null) return false;
        //return Fuel.getByFuel(this.fuel.getType()) != null;
        return this.recipeManager.getByMaterial(this.fuel.getType()) != null;
    }

    private void processBurn() {
        Fuel fuel = this.recipeManager.getByMaterial(this.fuel.getType());
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

    public void placeItem(Player player, int slot, ItemStack item) {
        // 0=input 1=fuel 2=output
        switch (slot) {
            case 0:
                if (this.input == null) {
                    this.input = item;
                } else if (item.getType() == this.input.getType()) {
                    this.input.setAmount(this.input.getAmount() + item.getAmount());
                } else {
                    return;
                }
                break;
            case 1:
                if (this.fuel == null) {
                    this.fuel = item;
                    Bukkit.broadcastMessage("Setting fuel");
                } else if (item.getType() == this.fuel.getType()) {
                    this.fuel.setAmount(this.fuel.getAmount() + item.getAmount());
                    Bukkit.broadcastMessage("Increasing fuel");
                }
                break;
        }
        updateInventory();
    }

    @Override
    public String toString() {
        return "FakeFurnace{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", cookTime=" + cookTime +
                ", fuelTime=" + fuelTime +
                '}';
    }

}
