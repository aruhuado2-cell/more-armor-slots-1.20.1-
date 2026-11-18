package com.extraarmorslots;

import com.extraarmorslots.config.ModConfig;
import com.extraarmorslots.menu.ModMenus;
import com.extraarmorslots.network.ModNetwork;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ExtraArmorSlotsMod.MOD_ID)
public class ExtraArmorSlotsMod {
    public static final String MOD_ID = "extraarmorslots";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtraArmorSlotsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModMenus.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.COMMON,
                ModConfig.SPEC
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModNetwork::register);
    }
}
