package com.extraarmorslots.capability;

import com.extraarmorslots.ExtraArmorSlotsMod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtraArmorProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = new ResourceLocation(ExtraArmorSlotsMod.MOD_ID, "extra_armor");
    public static final Capability<ExtraArmorHandler> EXTRA_ARMOR_CAP =
            CapabilityManager.get(new CapabilityToken<>() {});

    private ExtraArmorHandler handler = null;
    private final LazyOptional<ExtraArmorHandler> optional = LazyOptional.of(this::createHandler);

    private ExtraArmorHandler createHandler() {
        if (this.handler == null) {
            this.handler = new ExtraArmorHandler();
        }
        return this.handler;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == EXTRA_ARMOR_CAP) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return createHandler().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createHandler().deserializeNBT(nbt);
    }

    public void invalidate() {
        optional.invalidate();
    }
}
