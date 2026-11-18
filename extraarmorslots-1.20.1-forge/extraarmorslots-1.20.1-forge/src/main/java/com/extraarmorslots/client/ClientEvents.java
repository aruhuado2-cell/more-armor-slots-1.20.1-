package com.extraarmorslots.client;

import com.extraarmorslots.ExtraArmorSlotsMod;
import com.extraarmorslots.menu.ModMenus;
import com.extraarmorslots.network.ModNetwork;
import com.extraarmorslots.network.OpenExtraArmorScreenPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class ClientEvents {
    public static final KeyMapping OPEN_GUI_KEY = new KeyMapping(
            "key.extraarmorslots.open_gui",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.extraarmorslots"
    );

    @Mod.EventBusSubscriber(modid = ExtraArmorSlotsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> MenuScreens.register(ModMenus.EXTRA_ARMOR_MENU.get(), ExtraArmorScreen::new));
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(OPEN_GUI_KEY);
        }
    }

    @Mod.EventBusSubscriber(modid = ExtraArmorSlotsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeBusEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.screen == null) {
                    while (OPEN_GUI_KEY.consumeClick()) {
                        ModNetwork.CHANNEL.sendToServer(new OpenExtraArmorScreenPacket());
                    }
                }
            }
        }
    }
}
