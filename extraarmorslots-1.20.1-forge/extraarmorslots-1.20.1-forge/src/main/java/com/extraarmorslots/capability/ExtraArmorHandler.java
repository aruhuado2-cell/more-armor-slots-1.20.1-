package com.extraarmorslots.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ExtraArmorHandler extends ItemStackHandler {
    public static final int MAX_SLOTS = 27;
    private boolean dirty = true;

    public ExtraArmorHandler() {
        super(MAX_SLOTS);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }
        if (stack.getItem() instanceof ArmorItem) {
            return true;
        }
        if (stack.getItem() instanceof Equipable equipable) {
            return equipable.getEquipmentSlot().isArmor();
        }
        return false;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void copyFrom(ExtraArmorHandler other) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            this.stacks.set(i, other.getStackInSlot(i).copy());
        }
        this.dirty = true;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (this.stacks.size() != MAX_SLOTS) {
            this.setSize(MAX_SLOTS);
        }
        this.dirty = true;
    }
}
