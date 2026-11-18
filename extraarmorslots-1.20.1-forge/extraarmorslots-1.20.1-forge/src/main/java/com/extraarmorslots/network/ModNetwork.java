package com.extraarmorslots.network;

import com.extraarmorslots.ExtraArmorSlotsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ExtraArmorSlotsMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.messageBuilder(OpenExtraArmorScreenPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(OpenExtraArmorScreenPacket::toBytes)
                .decoder(OpenExtraArmorScreenPacket::new)
                .consumerMainThread(OpenExtraArmorScreenPacket::handle)
                .add();
    }
}
