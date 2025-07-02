package com.hyperoman.manyshields.util;

import com.hyperoman.manyshields.ManyShields;
import com.hyperoman.manyshields.item.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

import java.util.Objects;
import java.util.function.Predicate;

@EventBusSubscriber(value = Dist.CLIENT, modid = ManyShields.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ShieldRenderer extends BlockEntityWithoutLevelRenderer {

    public static ShieldRenderer instance;
    private ShieldModel shieldModel;

    public ShieldRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        instance = new ShieldRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(instance);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        BannerPatternLayers bannerpatternlayers = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        DyeColor dyecolor = stack.get(DataComponents.BASE_COLOR);
        boolean flag = !bannerpatternlayers.layers().isEmpty() || dyecolor != null;
        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);
        Material material = flag ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;

        Item shield = stack.getItem();
        if (shield == ModItems.WOODEN_SHIELD.get()) {
            material = flag ? ModModelPredicateProvider.LOCATION_WOODEN_SHIELD_BASE : ModModelPredicateProvider.LOCATION_WOODEN_SHIELD_BASE_NOPATTERN;
        } else if (shield == ModItems.GOLD_SHIELD.get()) {
            material = flag ? ModModelPredicateProvider.LOCATION_GOLD_SHIELD_BASE : ModModelPredicateProvider.LOCATION_GOLD_SHIELD_BASE_NOPATTERN;
        } else if (shield == ModItems.DIAMOND_SHIELD.get()) {
            material = flag ? ModModelPredicateProvider.LOCATION_DIAMOND_SHIELD_BASE : ModModelPredicateProvider.LOCATION_DIAMOND_SHIELD_BASE_NOPATTERN;
        } else if (shield == ModItems.NETHERITE_SHIELD.get()) {
            material = flag ? ModModelPredicateProvider.LOCATION_NETHERITE_SHIELD_BASE : ModModelPredicateProvider.LOCATION_NETHERITE_SHIELD_BASE_NOPATTERN;
        }

        VertexConsumer $$28 = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(multiBufferSource, this.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
        this.shieldModel.handle().render(poseStack, $$28, light, overlay);
        if (flag) {
            BannerRenderer.renderPatterns(poseStack, multiBufferSource, light, overlay, this.shieldModel.plate(), material, false, (DyeColor) Objects.requireNonNullElse(dyecolor, DyeColor.WHITE), bannerpatternlayers, stack.hasFoil());
        } else {
            this.shieldModel.plate().render(poseStack, $$28, light, overlay);
        }
        poseStack.popPose();
    }
}