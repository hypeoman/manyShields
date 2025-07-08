package com.hyperoman.manyshields;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Integer> WOODEN_SHIELD_DURABILITY;
    public static final ModConfigSpec.ConfigValue<Integer> GOLD_SHIELD_DURABILITY;
    public static final ModConfigSpec.ConfigValue<Integer> DIAMOND_SHIELD_DURABILITY;
    public static final ModConfigSpec.ConfigValue<Integer> NETHERITE_SHIELD_DURABILITY;

    static {
        WOODEN_SHIELD_DURABILITY = BUILDER.comment("Durability of Wooden Shield. Default Durability is 79")
                .define("woodenShieldDurability", 79);
                
        GOLD_SHIELD_DURABILITY = BUILDER.comment("Durability of Gold Shield. Default Durability is 44")
                .define("goldShieldDurability", 44);

        DIAMOND_SHIELD_DURABILITY = BUILDER.comment("Durability of Diamond Shield. Default Durability is 2098")
                .define("diamondShieldDurability", 2098);
        
        NETHERITE_SHIELD_DURABILITY = BUILDER.comment("Durability of Netherite Shield. Default Durability is 2731")
                .define("netheriteShieldDurability", 2731);
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
