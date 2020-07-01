package com.shanebeestudios.vf.api.manager;

import com.shanebeestudios.vf.api.VirtualFurnaceAPI;
import com.shanebeestudios.vf.api.machine.Brewer;
import com.shanebeestudios.vf.api.property.BrewerProperties;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
 * Manager for {@link Brewer Brewers}
 * <p>You can get an instance of this class from <b>{@link VirtualFurnaceAPI#getBrewerManager()}</b></p>
 */
@SuppressWarnings("unused")
public class BrewerManager extends Manager<Brewer>{

    public BrewerManager(VirtualFurnaceAPI API) {
        super(API, "brewer");
    }

    /**
     * Get a {@link Brewer} by ID
     *
     * @param uuid ID of brewer to grab
     * @return Brewer from ID (null if a brewer with this ID does not exist)
     */
    public Brewer getByID(@NotNull UUID uuid) {
        return this.machineMap.get(uuid);
    }

    /**
     * Create a new brewer
     * <p>This will create a new brewer, add it to the tick list, and save to file</p>
     * <p><b>NOTE:</b> The default <b>{@link BrewerProperties}</b> associated with this brewer will be <b>{@link BrewerProperties#BREWER}</b></p>
     *
     * @param name Name of new brewer (This shows up in the inventory view)
     * @return Instance of this new brewer
     */
    public Brewer createBrewer(@NotNull String name) {
        return createBrewer(name, BrewerProperties.BREWER, null);
    }

    /**
     * Create a new brewer
     * <p>This will create a new brewer, add it to the tick list, and save to file</p>
     *
     * @param name             Name of new brewer (This shows up in the inventory view)
     * @param brewerProperties Properties to apply to this brewer
     * @return Instance of this new brewer
     */
    public Brewer createBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties) {
        return createBrewer(name, brewerProperties, null);
    }

    /**
     * Create a new brewer
     * <p>This will create a new brewer, add it to the tick list, and save to file</p>
     * <p><b>NOTE:</b> The default <b>{@link BrewerProperties}</b> associated with this brewer will be <b>{@link BrewerProperties#BREWER}</b></p>
     *
     * @param name     Name of new brewer (This shows up in the inventory view)
     * @param function Function to run before brewer is created
     * @return Instance of this new brewer
     */
    public Brewer createBrewer(@NotNull String name, @NotNull Consumer<Brewer> function) {
        return createBrewer(name, BrewerProperties.BREWER, function);
    }

    /**
     * Create a new brewer
     * <p>This will create a new brewer, add it to the tick list, and save to file</p>
     *
     * @param name             Name of new brewer (This shows up in the inventory view)
     * @param brewerProperties Properties to apply to this brewer
     * @param function         Function to run before brewer is created
     * @return Instance of this new brewer
     */
    public Brewer createBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties, @Nullable Consumer<Brewer> function) {
        Brewer brewer = new Brewer(name, brewerProperties);
        if (function != null) {
            function.accept(brewer);
        }
        this.machineMap.put(brewer.getUniqueID(), brewer);
        saveMachine(brewer, true);
        return brewer;
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link BrewerProperties}</b> associated with this brewer will be <b>{@link BrewerProperties#BREWER}</b></p>
     *
     * @param name     Name of brewer (this will show up in the brewer UI)
     * @param material Material of the new ItemStack
     * @param glowing  Whether the item should glow (enchanted)
     * @return New ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull Material material, boolean glowing) {
        return createItemWithBrewer(name, new ItemStack(material), glowing);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     *
     * @param name             Name of brewer (this will show up in the brewer UI)
     * @param brewerProperties Properties associated with this brewer item
     * @param material         Material of the new ItemStack
     * @param glowing          Whether the item should glow (enchanted)
     * @return New ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties, @NotNull Material material, boolean glowing) {
        return createItemWithBrewer(name, brewerProperties, new ItemStack(material), glowing);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link BrewerProperties}</b> associated with this brewer will be <b>{@link BrewerProperties#BREWER}</b></p>
     *
     * @param name     Name of brewer (this will show up in the brewer UI)
     * @param material Material of the new ItemStack
     * @param glowing  Whether the item should glow (enchanted)
     * @param function Function to run before brewer is created
     * @return New ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull Material material, boolean glowing, @Nullable Consumer<Brewer> function) {
        return createItemWithBrewer(name, new ItemStack(material), glowing, function);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     *
     * @param name             Name of brewer (this will show up in the brewer UI)
     * @param brewerProperties Properties associated with this brewer item
     * @param material         Material of the new ItemStack
     * @param glowing          Whether the item should glow (enchanted)
     * @param function         Function to run before brewer is created
     * @return New ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties, @NotNull Material material, boolean glowing, @Nullable Consumer<Brewer> function) {
        return createItemWithBrewer(name, brewerProperties, new ItemStack(material), glowing, function);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     *
     * @param name      Name of brewer (this will show up in the brewer UI)
     * @param itemStack ItemStack to be copied and have a brewer attached
     * @param glowing   Whether the item should glow (enchanted)
     * @return Clone of the input ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull ItemStack itemStack, boolean glowing) {
        return createItemWithBrewer(name, itemStack, glowing, null);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     *
     * @param name             Name of brewer (this will show up in the brewer UI)
     * @param brewerProperties Properties associated with this brewer item
     * @param itemStack        ItemStack to be copied and have a brewer attached
     * @param glowing          Whether the item should glow (enchanted)
     * @return Clone of the input ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties, @NotNull ItemStack itemStack, boolean glowing) {
        return createItemWithBrewer(name, brewerProperties, itemStack, glowing, null);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     * <p><b>NOTE:</b> The default <b>{@link BrewerProperties}</b> associated with this brewer will be <b>{@link BrewerProperties#BREWER}</b></p>
     *
     * @param name      Name of brewer (this will show up in the brewer UI)
     * @param itemStack ItemStack to be copied and have a brewer attached
     * @param glowing   Whether the item should glow (enchanted)
     * @param function  Function to run before brewer is created
     * @return Clone of the input ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull ItemStack itemStack, boolean glowing, @Nullable Consumer<Brewer> function) {
        return createItemWithBrewer(name, BrewerProperties.BREWER, itemStack, glowing, function);
    }

    /**
     * Create a {@link Brewer} that is attached to an {@link ItemStack}
     *
     * @param name             Name of brewer (this will show up in the brewer UI)
     * @param brewerProperties Properties associated with this brewer item
     * @param itemStack        ItemStack to be copied and have a brewer attached
     * @param glowing          Whether the item should glow (enchanted)
     * @param function         Function to run before brewer is created
     * @return Clone of the input ItemStack with a brewer attached
     */
    public ItemStack createItemWithBrewer(@NotNull String name, @NotNull BrewerProperties brewerProperties, @NotNull ItemStack itemStack, boolean glowing, @Nullable Consumer<Brewer> function) {
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
        Brewer brewer;
        if (function == null) {
            brewer = createBrewer(name, brewerProperties);
        } else {
            brewer = createBrewer(name, brewerProperties, function);
        }
        meta.getPersistentDataContainer().set(this.key, PersistentDataType.STRING, brewer.getUniqueID().toString());
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Get a {@link Brewer} from an {@link ItemStack}
     *
     * @param itemStack ItemStack to grab brewer from
     * @return Brewer if the ItemStack has one assigned to it else null
     */
    public Brewer getBrewerFromItemStack(@NotNull ItemStack itemStack) {
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
