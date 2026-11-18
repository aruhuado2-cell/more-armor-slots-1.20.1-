package com.extraarmorslots.menu;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ArmorOnlySlot extends SlotItemHandler {
    public ArmorOnlySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            return true;
        }
        if (stack.getItem() instanceof Equipable equipable) {
            return equipable.getEquipmentSlot().isArmor();
        }
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
