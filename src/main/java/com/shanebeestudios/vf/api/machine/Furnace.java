package com.shanebeestudios.vf.api.machine;

import com.shanebeestudios.vf.api.RecipeManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.event.machine.FurnaceFuelBurnEvent;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import com.shanebeestudios.vf.api.property.PropertyHolder;
import com.shanebeestudios.vf.api.recipe.Fuel;
import com.shanebeestudios.vf.api.recipe.FurnaceRecipe;
import com.shanebeestudios.vf.api.util.Util;
import org.bukkit.Bukkit;
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

/**
 * Virtual furnace object
 */
@SuppressWarnings("unused")
public class Furnace extends Machine implements PropertyHolder<FurnaceProperties>, InventoryHolder, ConfigurationSerializable {

    private final FurnaceProperties furnaceProperties;
    private final RecipeManager recipeManager;
    private ItemStack fuel;
    private ItemStack input;
    private ItemStack output;
    private int cookTime;
    private int cookTimeTotal;
    private int fuelTime;
    private int fuelTimeTotal;
    private final Inventory inventory;

    /**
     * Create a new furnace object
     * <p><b>NOTE:</b> Creating a furnace object using this method will not tick the furnace.</p>
     * <p>It is recommended to use <b>{@link com.shanebeestudios.vf.api.FurnaceManager#createFurnace(String)}</b></p>
     * <p><b>NOTE:</b> The properties used for this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name Name of the object which will show up in the UI
     */
    public Furnace(String name) {
        this(name, FurnaceProperties.FURNACE);
    }

    /**
     * Create a new furnace object
     * <p><b>NOTE:</b> Creating a furnace object using this method will not tick the furnace.</p>
     * <p>It is recommended to use <b>{@link com.shanebeestudios.vf.api.FurnaceManager#createFurnace(String)}</b></p>
     *
     * @param name       Name of the object which will show up in the UI
     * @param furnaceProperties Property for this furnace
     */
    public Furnace(String name, FurnaceProperties furnaceProperties) {
        super(UUID.randomUUID(), name);
        this.furnaceProperties = furnaceProperties;
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
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

    // Used for deserializer
    private Furnace(String name, UUID uuid, int cookTime, int fuelTime, ItemStack fuel, ItemStack input, ItemStack output, FurnaceProperties furnaceProperties) {
        super(uuid, name);
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
        this.cookTime = cookTime;
        this.fuelTime = fuelTime;
        this.fuel = fuel;
        this.input = input;
        this.output = output;
        this.furnaceProperties = furnaceProperties;

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


    /**
     * Get the properties associated with this furnace
     *
     * @return Properties associated with this furnace
     */
    @Override
    public FurnaceProperties getProperties() {
        return this.furnaceProperties;
    }

    /**
     * Get this furnace's inventory
     *
     * @return Inventory
     */
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Get this furnace's current fuel
     *
     * @return Current fuel
     */
    public ItemStack getFuel() {
        return fuel;
    }

    /**
     * Set this furnace's fuel
     *
     * @param fuel Fuel to set
     */
    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    /**
     * Get this furnace's current input ItemStack
     *
     * @return Current input
     */
    public ItemStack getInput() {
        return input;
    }

    /**
     * Set this furnace's input ItemStack
     *
     * @param input ItemStack to set
     */
    public void setInput(ItemStack input) {
        this.input = input;
    }

    /**
     * Get this furnace's output ItemStack
     *
     * @return Output itemstack
     */
    public ItemStack getOutput() {
        return output;
    }

    /**
     * Open this furnace's inventory to a player
     *
     * @param player Player to open inventory to
     */
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

    /**
     * Tick this furnace
     * <p>This will process the fuel, cook the input item
     * and update the inventory</p>
     */
    @Override
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
        FurnaceFuelBurnEvent event = new FurnaceFuelBurnEvent(this, this.fuel, fuel, fuel.getBurnTime());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        int fuelAmount = this.fuel.getAmount();
        if (fuelAmount > 1) {
            this.fuel.setAmount(fuelAmount - 1);
        } else {
            this.fuel = null;
        }
        int burn = (int) (event.getBurnTime() / furnaceProperties.getFuelMultiplier());
        this.fuelTime = burn;
        this.fuelTimeTotal = burn;
        updateInventory();
    }

    private boolean canCook() {
        if (this.input == null) return false;
        FurnaceRecipe result = this.recipeManager.getByIngredient(this.input.getType());
        if (result == null) return false;
        this.cookTimeTotal = (int) (result.getCookTime() / furnaceProperties.getCookMultiplier());
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
        return "Furnace{" +
                "name='" + getName() + '\'' +
                ", uuid=" + getUniqueID() +
                ", properties=" + furnaceProperties +
                ", fuel=" + fuel +
                ", input=" + input +
                ", output=" + output +
                ", cookTime=" + cookTime +
                ", fuelTime=" + fuelTime +
                '}';
    }

    /**
     * Serialize this object for yaml
     * <p><b>Internal use only!</b></p>
     *
     * @return Returns serialized map of object
     */
    // Serializer for config
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.getName());
        result.put("uuid", this.getUniqueID().toString());
        result.put("properties", this.furnaceProperties);
        result.put("cookTime", this.cookTime);
        result.put("fuelTime", this.fuelTime);
        result.put("fuel", this.fuel);
        result.put("input", this.input);
        result.put("output", this.output);
        return result;
    }

    /**
     * Deserialize this object from yaml
     * <p><b>Internal use only!</b></p>
     *
     * @param args Serialized map of object
     * @return New instance of object
     */
    public static Furnace deserialize(Map<String, Object> args) {
        String name = ((String) args.get("name"));
        UUID uuid = UUID.fromString(((String) args.get("uuid")));
        FurnaceProperties furnaceProperties = (FurnaceProperties) args.get("properties");
        int cookTime = ((Number) args.get("cookTime")).intValue();
        int fuelTime = ((Number) args.get("fuelTime")).intValue();
        ItemStack fuel = ((ItemStack) args.get("fuel"));
        ItemStack input = ((ItemStack) args.get("input"));
        ItemStack output = ((ItemStack) args.get("output"));
        return new Furnace(name, uuid, cookTime, fuelTime, fuel, input, output, furnaceProperties);
    }

}
