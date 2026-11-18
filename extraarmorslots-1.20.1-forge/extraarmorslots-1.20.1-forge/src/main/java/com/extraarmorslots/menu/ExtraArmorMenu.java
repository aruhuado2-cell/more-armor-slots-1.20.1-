package com.extraarmorslots.menu;

import com.extraarmorslots.capability.ExtraArmorHandler;
import com.extraarmorslots.capability.ExtraArmorProvider;
import com.extraarmorslots.config.ModConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ExtraArmorMenu extends AbstractContainerMenu {
    private final int activeSlots;
    private final int rows;

    public ExtraArmorMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, new ItemStackHandler(ExtraArmorHandler.MAX_SLOTS), extraData.readInt());
    }

    public ExtraArmorMenu(int containerId, Inventory playerInv, Player player) {
        this(
                containerId,
                playerInv,
                player.getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP)
                        .<IItemHandler>map(h -> h)
                        .orElseGet(() -> new ItemStackHandler(ExtraArmorHandler.MAX_SLOTS)),
                ModConfig.EXTRA_SLOT_COUNT.get()
        );
    }

    private ExtraArmorMenu(int containerId, Inventory playerInv, IItemHandler armorHandler, int activeSlots) {
        super(ModMenus.EXTRA_ARMOR_MENU.get(), containerId);
        this.activeSlots = Math.max(1, Math.min(ExtraArmorHandler.MAX_SLOTS, activeSlots));
        this.rows = (int) Math.ceil(this.activeSlots / 9.0);

        int startY = 18;

        for (int i = 0; i < this.activeSlots; i++) {
            int row = i / 9;
            int col = i % 9;
            int rowCols = (row == this.rows - 1 && this.activeSlots % 9 != 0) ? (this.activeSlots % 9) : 9;
            int rowStartX = 8 + ((9 - rowCols) * 18) / 2;
            this.addSlot(new ArmorOnlySlot(armorHandler, i, rowStartX + col * 18, startY + row * 18));
        }

        int invStartY = 18 + this.rows * 18 + 14;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, invStartY + row * 18));
            }
        }

        int hotbarY = invStartY + 58;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, hotbarY));
        }
    }

    public int getActiveSlots() {
        return activeSlots;
    }

    public int getRows() {
        return rows;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack copied = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            copied = slotStack.copy();

            int containerSize = this.activeSlots;
            int invEnd = containerSize + 36;

            if (index < containerSize) {
                if (!this.moveItemStackTo(slotStack, containerSize, invEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(slotStack, 0, containerSize, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == copied.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }
        return copied;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.isAlive();
    }
}
