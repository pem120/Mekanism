package mekanism.common.lib.inventory.personalstorage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import mekanism.api.DataHandlerUtils;
import mekanism.api.NBTConstants;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.common.lib.MekanismSavedData;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.SecurityUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
@ParametersAreNotNullByDefault
public class PersonalStorageManager {
    private static final Map<UUID, PersonalStorageData> STORAGE_BY_PLAYER_UUID = new HashMap<>();

    private static PersonalStorageData forOwner(UUID playerUUID) {
        return STORAGE_BY_PLAYER_UUID.computeIfAbsent(playerUUID, uuid->MekanismSavedData.createSavedData(PersonalStorageData::new, "personal_storage" + File.separator + uuid));
    }

    /**
     * Only call on the server. Gets or creates an inventory for the supplied stack
     *
     * @param stack Personal storage ItemStack (type not checked) - will be modified if it didn't have an inventory id
     * @return the existing or new inventory
     */
    public static PersonalStorageItemInventory getInventoryFor(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        if (owner == null) {
            throw new IllegalStateException("Stack has no owner!");
        }
        UUID invId = getInventoryId(stack);
        PersonalStorageItemInventory storageItemInventory = forOwner(owner).getOrAddInventory(invId);
        ListTag legacyData = ItemDataUtils.getList(stack, NBTConstants.ITEMS);
        if (!legacyData.isEmpty()) {
            DataHandlerUtils.readContainers(storageItemInventory.getInventorySlots(null), legacyData);
            ItemDataUtils.removeData(stack, NBTConstants.ITEMS);
        }
        return storageItemInventory;
    }

    /**
     * Only call on the server
     * <p>
     * Version of {@link #getInventoryFor(ItemStack)} which will NOT create an inventory if none exists already.
     * The stack will only be modified if it contained a legacy inventory
     *
     * @param stack Personal storage ItemStack
     * @return the existing or converted inventory
     */
    @Nullable
    public static PersonalStorageItemInventory getInventoryIfPresent(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        UUID invId = getInventoryIdNullable(stack);
        boolean hasLegacyData = ItemDataUtils.hasData(stack, NBTConstants.ITEMS, Tag.TAG_LIST);
        return owner != null && (invId != null || hasLegacyData) ? getInventoryFor(stack) : null;
    }

    public static void deleteInventory(ItemStack stack) {
        UUID owner = SecurityUtils.get().getOwnerUUID(stack);
        UUID invId = getInventoryIdNullable(stack);
        if (owner != null && invId != null) {
            forOwner(owner).removeInventory(invId);
        }
    }

    @NotNull
    private static UUID getInventoryId(ItemStack stack) {
        UUID invId = getInventoryIdNullable(stack);
        if (invId == null) {
            invId = UUID.randomUUID();
            ItemDataUtils.setUUID(stack, NBTConstants.PERSONAL_STORAGE_ID, invId);
        }
        return invId;
    }

    @Nullable
    private static UUID getInventoryIdNullable(ItemStack stack) {
        return ItemDataUtils.getUniqueID(stack, NBTConstants.PERSONAL_STORAGE_ID);
    }

    public static void reset() {
        STORAGE_BY_PLAYER_UUID.clear();
    }

}
