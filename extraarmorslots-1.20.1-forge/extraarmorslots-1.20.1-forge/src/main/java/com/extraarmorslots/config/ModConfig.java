package com.extraarmorslots.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue EXTRA_SLOT_COUNT;
    public static final ForgeConfigSpec.BooleanValue DROP_ON_DEATH;

    static {
        BUILDER.push("Extra Armor Slots Settings");

        EXTRA_SLOT_COUNT = BUILDER
                .comment("Number of extra functional armor slots (1 to 27).")
                .defineInRange("extraSlotCount", 4, 1, 27);

        DROP_ON_DEATH = BUILDER
                .comment("Whether items in extra armor slots drop on death when keepInventory is false.")
                .define("dropOnDeath", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
