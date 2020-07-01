package com.shanebeestudios.vf.api.machine;

import com.shanebeestudios.vf.api.RecipeManager;
import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.property.BrewerProperties;
import com.shanebeestudios.vf.api.property.PropertyHolder;
import com.shanebeestudios.vf.api.recipe.BrewingFuel;
import com.shanebeestudios.vf.api.recipe.BrewingRecipe;
import com.shanebeestudios.vf.api.tile.Tile;
import com.shanebeestudios.vf.api.util.InventoryCreator;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Virtual brewer object
 */
@SuppressWarnings("unused")
public class Brewer extends Machine implements InventoryHolder, PropertyHolder<BrewerProperties>, ConfigurationSerializable {

    private final RecipeManager recipeManager;
    private final Inventory inventory;

    private ItemStack fuelItem = null;
    private ItemStack ingredient = null;
    private final ItemStack[] bottles;
    private final BrewerProperties properties;

    private int fuelTime = 0;
    private int maxBrews = 0;
    private int brewTime = 0;
    private int brewSpeed = 1;

    public Brewer(String name, BrewerProperties properties) {
        super(UUID.randomUUID(), name);
        this.properties = properties;
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
        this.inventory = InventoryCreator.createBrewerInventory(this, name);
        this.bottles = new ItemStack[3];
        this.updateInventory();
    }

    // Used for deserializer
    private Brewer(UUID uuid, String name, BrewerProperties properties, int brewTime, int maxBrews, int fuelTime, ItemStack fuel, ItemStack ingredient, ItemStack[] bottles) {
        super(uuid, name);
        this.recipeManager = VirtualFurnaceAPI.getInstance().getRecipeManager();
        this.properties = properties;
        this.brewTime = brewTime;
        this.maxBrews = maxBrews;
        this.fuelTime = fuelTime;
        this.fuelItem = fuel;
        this.ingredient = ingredient;
        this.bottles = bottles;
        this.inventory = InventoryCreator.createBrewerInventory(this, name);
        this.updateInventory();
        this.updateBrewSpeed();
    }

    /**
     * Open the inventory of this machine to a player
     *
     * @param player Player to open inventory to
     */
    @Override
    public void openInventory(Player player) {
        updateInventory();
        player.openInventory(inventory);
    }

    private void updateInventory() {
        this.inventory.setItem(0, bottles[0]);
        this.inventory.setItem(1, bottles[1]);
        this.inventory.setItem(2, bottles[2]);
        this.inventory.setItem(3, ingredient);
        this.inventory.setItem(4, fuelItem);
    }

    private void updateInventoryView() {
        ItemStack b0 = this.inventory.getItem(0);
        ItemStack b1 = this.inventory.getItem(1);
        ItemStack b2 = this.inventory.getItem(2);
        ItemStack ing = this.inventory.getItem(3);
        ItemStack f = this.inventory.getItem(4);
        if (!match(this.bottles[0], b0)) {
            this.bottles[0] = b0;
        }
        if (!match(this.bottles[1], b1)) {
            this.bottles[1] = b1;
        }
        if (!match(this.bottles[2], b2)) {
            this.bottles[2] = b2;
        }
        if (!match(ingredient, ing)) {
            this.ingredient = ing;
        }
        if (!match(fuelItem, f)) {
            this.fuelItem = f;
        }
        for (HumanEntity entity : inventory.getViewers()) {
            InventoryView view = entity.getOpenInventory();
            view.setProperty(InventoryView.Property.BREW_TIME, brewTime);
            view.setProperty(InventoryView.Property.FUEL_TIME, (int) Math.round(((double) fuelTime / (double) (maxBrews / 20))));
        }
    }

    private boolean match(ItemStack one, ItemStack two) {
        if (one == null || two == null) return false;
        return one.equals(two);
    }

    public ItemStack getFuelItem() {
        return fuelItem;
    }

    public void setFuelItem(ItemStack fuelItem) {
        this.fuelItem = fuelItem;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    public void setIngredient(ItemStack ingredient) {
        this.ingredient = ingredient;
    }

    public Collection<ItemStack> getBottles() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (bottles[i] != null) {
                items.add(bottles[i]);
            }
        }
        return items;
    }

    public ItemStack getBottle(int slot) {
        if (slot > 2 || slot < 0) {
            return null;
        }
        return bottles[slot];
    }

    public boolean setBottle(int slot, ItemStack item) {
        if (slot > 2 || slot < 0) {
            return false;
        }
        bottles[slot] = item;
        return true;
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the properties associated with this property holder
     *
     * @return Properties associated with this property holder
     */
    @Override
    public BrewerProperties getProperties() {
        return properties;
    }

    @Override
    public void tick(Tile<?> tile) {
        super.tick(tile);
        if (fuelTime > 0) {
            if (brewTime > 0) {
                if (ingredient != null) {
                    brewTime -= brewSpeed;
                    if (brewTime <= 0) {
                        processBrew(tile);
                    }
                } else {
                    brewTime = 0;
                }
            } else if (canBrew()) {
                brewTime = 400;
                updateBrewSpeed();
            }
        } else if (canBurn()) {
           processBurn();
        }
        updateInventoryView();
    }

    private boolean canBurn() {
        BrewingFuel fuel = recipeManager.getBrewingFuelByItem(fuelItem);
        return fuel != null;
    }

    private void processBurn() {
        BrewingFuel fuel = recipeManager.getBrewingFuelByItem(fuelItem);
        fuelTime = (int) (fuel.getUses() * properties.getFuelMultiplier());
        maxBrews = fuelTime;
        if (fuelItem.getAmount() > 1) {
            fuelItem.setAmount(fuelItem.getAmount() - 1);
        } else {
            fuelItem = null;
        }
        updateInventory();
    }

    private boolean canBrew() {
        return getRecipe() != null;
    }

    private void processBrew(Tile<?> tile) {
        BrewingRecipe recipe = null;
        for (int i = 2; i >= 0; i--) {
            if (bottles[i] != null) {
                BrewingRecipe r = recipeManager.getBrewingRecipeByIngredient(ingredient, bottles[i]);
                if (r != null) {
                    recipe = r;
                }
            }
        }
        if (recipe == null) return;
        for (int i = 0; i <= 2; i++) {
            if (match(bottles[i], recipe.getInputBottle())) {
                bottles[i] = recipe.getOutputBottle().clone();
            }
        }
        if (fuelTime > 0) {
            fuelTime--;
        }
        ingredient = null;
        brewTime = 0;
        updateInventory();
        if (tile != null) {
            playSound(tile.getBlock().getLocation());
        } else {
            for (HumanEntity ent : inventory.getViewers()) {
                playSound(ent.getLocation());
            }
        }
    }

    private BrewingRecipe getRecipe() {
        if (ingredient == null) return null;
        BrewingRecipe recipe;
        for (int i = 0; i <= 2; i++) {
            if (bottles[i] != null) {
                recipe = recipeManager.getBrewingRecipeByIngredient(ingredient, bottles[i]);
                if (recipe != null) {
                    return recipe;
                }
            }
        }
        return null;
    }

    private void updateBrewSpeed() {
        BrewingRecipe recipe = getRecipe();
        if (recipe != null) {
            brewSpeed = (int) ((400 / recipe.getCookTime()) * properties.getBrewMultiplier());
        }
    }

    private void playSound(Location location) {
        assert location.getWorld() != null;
        location.getWorld().playSound(location, Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("name", this.getName());
        result.put("uuid", this.getUniqueID().toString());
        result.put("properties", this.properties);
        result.put("brewTime", this.brewTime);
        result.put("fuelTime", this.fuelTime);
        result.put("maxBrews", this.maxBrews);
        result.put("fuel", this.fuelItem);
        result.put("ingredient", this.ingredient);
        result.put("bottle-1", this.bottles[0]);
        result.put("bottle-2", this.bottles[1]);
        result.put("bottle-3", this.bottles[2]);
        return result;
    }

    public static Brewer deserialize(Map<String, Object> args) {
        String name = ((String) args.get("name"));
        UUID uuid = UUID.fromString(((String) args.get("uuid")));
        BrewerProperties prop = (BrewerProperties) args.get("properties");
        int brewTime = ((Number) args.get("brewTime")).intValue();
        int fuelTime = ((Number) args.get("fuelTime")).intValue();
        int maxBrews = ((Number) args.get("maxBrews")).intValue();
        ItemStack fuel = (ItemStack) args.get("fuel");
        ItemStack ing = (ItemStack) args.get("ingredient");
        ItemStack[] bottles = new ItemStack[3];
        bottles[0] = (ItemStack) args.get("bottle-1");
        bottles[1] = (ItemStack) args.get("bottle-2");
        bottles[2] = (ItemStack) args.get("bottle-3");
        return new Brewer(uuid, name, prop, brewTime, maxBrews, fuelTime, fuel, ing, bottles);
    }

}
