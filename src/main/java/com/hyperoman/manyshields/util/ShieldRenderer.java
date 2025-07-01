package com.hyperoman.manyshields.util;

import com.hyperoman.manyshields.ManyShields;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
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
    private ShieldModel shieldModel;
    private ShieldModel shieldPatternModel;
    private EntityModelSet entityModelSet;

    public static ShieldRenderer instance;

    public ShieldRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.entityModelSet = entityModelSet;
    }

    @SubscribeEvent
    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event){
        instance = new ShieldRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        event.registerReloadListener(instance);
    }

    @Override
    public void onResourceManagerReload(net.minecraft.server.packs.resources.ResourceManager resourceManager) {
        // берём те же слои, что ваниль
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType,
                             PoseStack matrixStack, net.minecraft.client.renderer.MultiBufferSource buffers,
                             int packedLight, int packedOverlay) {
        // --- 1) Если это именно наш щит, рисуем как у ванили ---
        if (stack.getItem() == ManyShields.WOODEN_SHIELD.get()) {
            // достаём цвета/паттерны из DataComponents
            var patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            DyeColor base = stack.get(DataComponents.BASE_COLOR);
            boolean hasPattern = !patterns.layers().isEmpty() || base != null;

            matrixStack.pushPose();
            matrixStack.scale(1.0F, -1.0F, -1.0F);

            // выбираем нужный материал: с паттернами или без
            Material mat = hasPattern ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
            VertexConsumer consumer = mat.sprite()
                    .wrap(ItemRenderer.getFoilBufferDirect(
                            buffers,
                            this.shieldModel.renderType(mat.atlasLocation()),
                            true,
                            stack.hasFoil()
                    ));

            // рендерим держак
            this.shieldModel.handle().render(matrixStack, consumer, packedLight, packedOverlay);

            // рендерим сам щит: либо просто пластину, либо пластину + цветные паттерны
            if (hasPattern) {
                BannerRenderer.renderPatterns(
                        matrixStack,
                        buffers,
                        packedLight,
                        packedOverlay,
                        this.shieldModel.plate(),
                        mat,
                        false,
                        Objects.requireNonNullElse(base, DyeColor.WHITE),
                        patterns,
                        stack.hasFoil()
                );
            } else {
                this.shieldModel.plate().render(matrixStack, consumer, packedLight, packedOverlay);
            }
            matrixStack.popPose();
            return;
        }

        // --- 2) Для всего остального — дефолтный рендер ванили ---
        super.renderByItem(stack, transformType, matrixStack, buffers, packedLight, packedOverlay);
    }
}
