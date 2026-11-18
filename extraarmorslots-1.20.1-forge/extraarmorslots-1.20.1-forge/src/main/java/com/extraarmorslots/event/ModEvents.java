package com.extraarmorslots.event;

import com.extraarmorslots.ExtraArmorSlotsMod;
import com.extraarmorslots.capability.ExtraArmorHandler;
import com.extraarmorslots.capability.ExtraArmorProvider;
import com.extraarmorslots.config.ModConfig;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ExtraArmorSlotsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final String MODIFIER_PREFIX = "extraarmorslots:";

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).isPresent()) {
                event.addCapability(ExtraArmorProvider.ID, new ExtraArmorProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath() || event.getOriginal().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(oldCap ->
                    event.getEntity().getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(newCap -> {
                        newCap.copyFrom(oldCap);
                        newCap.setDirty(true);
                    })
            );
            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(cap -> cap.setDirty(true));
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        event.getEntity().getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(cap -> cap.setDirty(true));
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player && ModConfig.DROP_ON_DEATH.get()) {
            if (!player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                player.getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(cap -> {
                    for (int i = 0; i < cap.getSlots(); i++) {
                        ItemStack stack = cap.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            event.getDrops().add(new ItemEntity(
                                    player.level(),
                                    player.getX(),
                                    player.getY() + 0.5,
                                    player.getZ(),
                                    stack.copy()
                            ));
                            cap.setStackInSlot(i, ItemStack.EMPTY);
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) {
            return;
        }

        Player player = event.player;
        player.getCapability(ExtraArmorProvider.EXTRA_ARMOR_CAP).ifPresent(cap -> {
            if (cap.isDirty()) {
                refreshExtraArmorAttributes(player, cap);
                cap.setDirty(false);
            }
        });
    }

    private static void refreshExtraArmorAttributes(Player player, ExtraArmorHandler cap) {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES.getValues()) {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                for (AttributeModifier mod : instance.getModifiers()) {
                    if (mod.getName().startsWith(MODIFIER_PREFIX)) {
                        instance.removeModifier(mod.getId());
                    }
                }
            }
        }

        int activeSlots = Math.max(1, Math.min(ExtraArmorHandler.MAX_SLOTS, ModConfig.EXTRA_SLOT_COUNT.get()));
        for (int slotIndex = 0; slotIndex < activeSlots; slotIndex++) {
            ItemStack stack = cap.getStackInSlot(slotIndex);
            if (stack.isEmpty()) {
                continue;
            }

            EquipmentSlot equipSlot = getArmorEquipmentSlot(stack);
            if (equipSlot == null) {
                continue;
            }

            Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(equipSlot);
            for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
                Attribute attribute = entry.getKey();
                AttributeModifier baseMod = entry.getValue();
                AttributeInstance instance = player.getAttribute(attribute);
                if (instance == null) {
                    continue;
                }

                ResourceLocation attrKey = ForgeRegistries.ATTRIBUTES.getKey(attribute);
                String uniqueKey = MODIFIER_PREFIX + slotIndex + ":" + (attrKey != null ? attrKey : attribute.getDescriptionId()) + ":" + baseMod.getName();
                UUID uniqueUuid = UUID.nameUUIDFromBytes(uniqueKey.getBytes(StandardCharsets.UTF_8));

                AttributeModifier applied = new AttributeModifier(
                        uniqueUuid,
                        uniqueKey,
                        baseMod.getAmount(),
                        baseMod.getOperation()
                );
                if (!instance.hasModifier(applied)) {
                    instance.addTransientModifier(applied);
                }
            }
        }
    }

    private static EquipmentSlot getArmorEquipmentSlot(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getEquipmentSlot();
        }
        if (stack.getItem() instanceof Equipable equipable && equipable.getEquipmentSlot().isArmor()) {
            return equipable.getEquipmentSlot();
        }
        return null;
    }
}
