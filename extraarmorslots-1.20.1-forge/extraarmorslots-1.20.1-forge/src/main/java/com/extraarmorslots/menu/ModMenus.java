package com.extraarmorslots.menu;

import com.extraarmorslots.ExtraArmorSlotsMod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ExtraArmorSlotsMod.MOD_ID);

    public static final RegistryObject<MenuType<ExtraArmorMenu>> EXTRA_ARMOR_MENU =
            MENUS.register("extra_armor_menu", () -> IForgeMenuType.create(ExtraArmorMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
