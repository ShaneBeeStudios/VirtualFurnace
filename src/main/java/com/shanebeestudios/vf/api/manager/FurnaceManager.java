package com.shanebeestudios.vf.api.manager;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Furnace;
import com.shanebeestudios.vf.api.property.FurnaceProperties;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Manager for {@link Furnace Furnaces}
 * <p>You can get an instance of this class from <b>{@link VirtualFurnaceAPI#getFurnaceManager()}</b></p>
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class FurnaceManager extends Manager<Furnace> {

    public FurnaceManager(VirtualFurnaceAPI API) {
        super(API, "furnace");
    }

    /**
     * Create a new furnace
     * <p>This will create a new furnace, add it to the tick list, and save to file</p>
     * <p><b>NOTE:</b> The default <b>{@link FurnaceProperties}</b> associated with this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name Name of new furnace (This shows up in the inventory view)
     * @return Instance of this new furnace
     */
    public Furnace createFurnace(@NotNull String name) {
        return createFurnace(name, FurnaceProperties.FURNACE, null);
    }

    /**
     * Create a new furnace
     * <p>This will create a new furnace, add it to the tick list, and save to file</p>
     *
     * @param name              Name of new furnace (This shows up in the inventory view)
     * @param furnaceProperties Properties to apply to this furnace
     * @return Instance of this new furnace
     */
    public Furnace createFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties) {
        return createFurnace(name, furnaceProperties, null);
    }

    /**
     * Create a new furnace
     * <p>This will create a new furnace, add it to the tick list, and save to file</p>
     * <p><b>NOTE:</b> The default <b>{@link FurnaceProperties}</b> associated with this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name     Name of new furnace (This shows up in the inventory view)
     * @param function Function to run before furnace is created
     * @return Instance of this new furnace
     */
    public Furnace createFurnace(@NotNull String name, @NotNull Consumer<Furnace> function) {
        return createFurnace(name, FurnaceProperties.FURNACE, function);
    }

    /**
     * Create a new furnace
     * <p>This will create a new furnace, add it to the tick list, and save to file</p>
     *
     * @param name              Name of new furnace (This shows up in the inventory view)
     * @param furnaceProperties Properties to apply to this furnace
     * @param function          Function to run before furnace is created
     * @return Instance of this new furnace
     */
    public Furnace createFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties, @Nullable Consumer<Furnace> function) {
        Furnace furnace = new Furnace(name, furnaceProperties);
        if (function != null) {
            function.accept(furnace);
        }
        this.machineMap.put(furnace.getUniqueID(), furnace);
        saveMachine(furnace, true);
        return furnace;
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link FurnaceProperties}</b> associated with this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name     Name of furnace (this will show up in the furnace UI)
     * @param material Material of the new ItemStack
     * @param glowing  Whether the item should glow (enchanted)
     * @return New ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull Material material, boolean glowing) {
        return createItemWithFurnace(name, new ItemStack(material), glowing);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     *
     * @param name              Name of furnace (this will show up in the furnace UI)
     * @param furnaceProperties Properties associated with this furnace item
     * @param material          Material of the new ItemStack
     * @param glowing           Whether the item should glow (enchanted)
     * @return New ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties, @NotNull Material material, boolean glowing) {
        return createItemWithFurnace(name, furnaceProperties, new ItemStack(material), glowing);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link FurnaceProperties}</b> associated with this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name     Name of furnace (this will show up in the furnace UI)
     * @param material Material of the new ItemStack
     * @param glowing  Whether the item should glow (enchanted)
     * @param function Function to run before furnace is created
     * @return New ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull Material material, boolean glowing, @Nullable Consumer<Furnace> function) {
        return createItemWithFurnace(name, new ItemStack(material), glowing, function);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     *
     * @param name              Name of furnace (this will show up in the furnace UI)
     * @param furnaceProperties Properties associated with this furnace item
     * @param material          Material of the new ItemStack
     * @param glowing           Whether the item should glow (enchanted)
     * @param function          Function to run before furnace is created
     * @return New ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties, @NotNull Material material, boolean glowing, @Nullable Consumer<Furnace> function) {
        return createItemWithFurnace(name, furnaceProperties, new ItemStack(material), glowing, function);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     *
     * @param name      Name of furnace (this will show up in the furnace UI)
     * @param itemStack ItemStack to be copied and have a furnace attached
     * @param glowing   Whether the item should glow (enchanted)
     * @return Clone of the input ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull ItemStack itemStack, boolean glowing) {
        return createItemWithFurnace(name, itemStack, glowing, null);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     *
     * @param name              Name of furnace (this will show up in the furnace UI)
     * @param furnaceProperties Properties associated with this furnace item
     * @param itemStack         ItemStack to be copied and have a furnace attached
     * @param glowing           Whether the item should glow (enchanted)
     * @return Clone of the input ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties, @NotNull ItemStack itemStack, boolean glowing) {
        return createItemWithFurnace(name, furnaceProperties, itemStack, glowing, null);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link FurnaceProperties}</b> associated with this furnace will be <b>{@link FurnaceProperties#FURNACE}</b></p>
     *
     * @param name      Name of furnace (this will show up in the furnace UI)
     * @param itemStack ItemStack to be copied and have a furnace attached
     * @param glowing   Whether the item should glow (enchanted)
     * @param function  Function to run before furnace is created
     * @return Clone of the input ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull ItemStack itemStack, boolean glowing, @Nullable Consumer<Furnace> function) {
        return createItemWithFurnace(name, FurnaceProperties.FURNACE, itemStack, glowing, function);
    }

    /**
     * Create a {@link Furnace} that is attached to an {@link ItemStack}
     *
     * @param name              Name of furnace (this will show up in the furnace UI)
     * @param furnaceProperties Properties associated with this furnace item
     * @param itemStack         ItemStack to be copied and have a furnace attached
     * @param glowing           Whether the item should glow (enchanted)
     * @param function          Function to run before furnace is created
     * @return Clone of the input ItemStack with a furnace attached
     */
    public ItemStack createItemWithFurnace(@NotNull String name, @NotNull FurnaceProperties furnaceProperties, @NotNull ItemStack itemStack, boolean glowing, @Nullable Consumer<Furnace> function) {
        ItemStack item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (glowing) {
            if (item.getType() == Material.BOW) {
                meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
            } else {
                meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            }
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        Furnace furnace;
        if (function == null) {
            furnace = createFurnace(name, furnaceProperties);
        } else {
            furnace = createFurnace(name, furnaceProperties, function);
        }
        meta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, furnace.getUniqueID().toString());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Get a {@link Furnace} from an {@link ItemStack}
     *
     * @param itemStack ItemStack to grab furnace from
     * @return Furnace if the ItemStack has one assigned to it else null
     */
    public Furnace getFurnaceFromItemStack(@NotNull ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if (meta.getPersistentDataContainer().has(this.key, PersistentDataType.STRING)) {
            String u = meta.getPersistentDataContainer().get(this.key, PersistentDataType.STRING);
            if (u == null) return null;
            return getByID(UUID.fromString(u));
        }
        return null;
    }

}
