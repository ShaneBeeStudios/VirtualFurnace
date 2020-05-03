package com.shanebeestudios.vf.api.util;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Creator class to create custom {@link Inventory Inventories}
 */
@SuppressWarnings("unused")
public class InventoryCreator {

    private static final Class<?> CREATOR_CLASS = ReflectionUtils.getOBCClass("inventory.util.CraftCustomInventoryConverter");
    private static final Object DEFAULT_CONVERTER;

    static {
        Object DEFAULT_CONVERTER1;
        try {
            assert CREATOR_CLASS != null;
            Constructor<?> constructor = CREATOR_CLASS.getConstructor();
            DEFAULT_CONVERTER1 = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            DEFAULT_CONVERTER1 = null;
        }
        DEFAULT_CONVERTER = DEFAULT_CONVERTER1;
    }

    /**
     * Create a new {@link org.bukkit.inventory.BrewerInventory}
     *
     * @param owner Owner of this new inventory
     * @param title Title of this new inventory
     * @return New instance of an inventory
     */
    public static Inventory createBrewerInventory(@NotNull InventoryHolder owner, @NotNull String title) {
        return createInventory(owner, InventoryType.BREWING, title);
    }

    /**
     * Create a new {@link org.bukkit.inventory.FurnaceInventory}
     *
     * @param owner Owner of this new inventory
     * @param title Title of this new inventory
     * @return New instance of an inventory
     */
    public static Inventory createFurnaceInventory(@NotNull InventoryHolder owner, @NotNull String title) {
        return createInventory(owner, InventoryType.FURNACE, title);
    }

    /**
     * Create a new {@link Inventory}
     *
     * @param owner Owner of this new inventory
     * @param type  Type of this new inventory
     * @param title Title of this new inventory
     * @return New instance of an inventory
     */
    public static Inventory createInventory(@NotNull InventoryHolder owner, @NotNull InventoryType type, @NotNull String title) {
        try {
            assert CREATOR_CLASS != null;
            Method method = CREATOR_CLASS.getMethod("createInventory", InventoryHolder.class, InventoryType.class, String.class);
            Object inv = method.invoke(DEFAULT_CONVERTER, owner, type, Util.getColString(title));
            return ((Inventory) inv);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
