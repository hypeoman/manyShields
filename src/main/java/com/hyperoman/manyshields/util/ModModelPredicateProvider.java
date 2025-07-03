package com.hyperoman.manyshields.util;

import com.hyperoman.manyshields.ManyShields;
import com.hyperoman.manyshields.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.Set;

@EventBusSubscriber(value = Dist.CLIENT, modid = ManyShields.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModModelPredicateProvider {

    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            addShieldPropertyOverrides(ResourceLocation.fromNamespaceAndPath(ManyShields.MODID, "blocking"),
                    (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F,
                    //Shields with Banner Support

                    ModItems.WOODEN_SHIELD.get(),
                    ModItems.GOLD_SHIELD.get(),
                    ModItems.DIAMOND_SHIELD.get(),
                    ModItems.NETHERITE_SHIELD.get()
            );
        });
    }

    private static void addShieldPropertyOverrides(ResourceLocation override, ClampedItemPropertyFunction propertyGetter,
                                                   ItemLike... shields) {
        for (ItemLike shield : shields) {
            ItemProperties.register(shield.asItem(), override, propertyGetter);
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onStitch(Pre event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            for (Material textures : new Material[] {
                    LOCATION_GOLD_SHIELD_BASE,
                    LOCATION_GOLD_SHIELD_BASE_NOPATTERN,
                    LOCATION_DIAMOND_SHIELD_BASE,
                    LOCATION_DIAMOND_SHIELD_BASE_NOPATTERN,
                    LOCATION_NETHERITE_SHIELD_BASE,
                    LOCATION_NETHERITE_SHIELD_BASE_NOPATTERN,
            }) {
                event.addSprite(textures.texture());
            }
        }
    }

    public static class Pre extends TextureAtlasStitchedEvent {
        private final Set<ResourceLocation> sprites;

        @ApiStatus.Internal
        public Pre(TextureAtlas map, Set<ResourceLocation> sprites) {
            super(map);
            this.sprites = sprites;
        }

        public boolean addSprite(ResourceLocation sprite) {
            return this.sprites.add(sprite);
        }
    }


    public static final Material LOCATION_WOODEN_SHIELD_BASE = material("entity/wooden_shield_base");
    public static final Material LOCATION_WOODEN_SHIELD_BASE_NOPATTERN = material("entity/wooden_shield_base_nopattern");
    public static final Material LOCATION_GOLD_SHIELD_BASE = material("entity/gold_shield_base");
    public static final Material LOCATION_GOLD_SHIELD_BASE_NOPATTERN = material("entity/gold_shield_base_nopattern");
    public static final Material LOCATION_DIAMOND_SHIELD_BASE = material("entity/diamond_shield_base");
    public static final Material LOCATION_DIAMOND_SHIELD_BASE_NOPATTERN = material("entity/diamond_shield_base_nopattern");
    public static final Material LOCATION_NETHERITE_SHIELD_BASE = material("entity/netherite_shield_base");
    public static final Material LOCATION_NETHERITE_SHIELD_BASE_NOPATTERN = material("entity/netherite_shield_base_nopattern");

    @SuppressWarnings("deprecation")
    private static Material material(String path) {

        LOGGER.info(new Material(
                TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(ManyShields.MODID, path)).toString());

        return new Material(
                TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(ManyShields.MODID, path));
    }

}
