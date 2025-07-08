package com.hyperoman.manyshields.item;

import com.hyperoman.manyshields.Config;
import com.hyperoman.manyshields.ManyShields;
import com.hyperoman.manyshields.util.ShieldRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class ModItems extends Item.Properties{

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ManyShields.MODID);

    public ModItems defaultDurability(int pDefaultDamage) {
        return this.component() == 0 ? (ModItems) this.durability(pDefaultDamage) : this;
    }

    private int component() {
        return 0;
    }

    public ModItems durability(int pMaxDamage) {
        this.component(DataComponents.MAX_DAMAGE, pMaxDamage);
        this.component(DataComponents.MAX_STACK_SIZE, 1);
        this.component(DataComponents.DAMAGE, 0);
        return this;
    }

    // Shields
    public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.register("wooden_shield",
            () -> new ShieldItem(new ModItems().defaultDurability(79)){
                @Override
                public int getMaxDamage(ItemStack stack){
                    return Config.WOODEN_SHIELD_DURABILITY.get();
                }

                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    consumer.accept(new IClientItemExtensions() {

                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return ShieldRenderer.instance;
                        }
                    });
                }
            });

    public static final DeferredItem<Item> GOLD_SHIELD = ITEMS.register("gold_shield",
            () -> new ShieldItem(new ModItems().defaultDurability(44)){
                @Override
                public int getMaxDamage(ItemStack stack){
                    return Config.GOLD_SHIELD_DURABILITY.get();
                }

                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    consumer.accept(new IClientItemExtensions() {

                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return ShieldRenderer.instance;
                        }
                    });
                }
            });

    public static final DeferredItem<Item> DIAMOND_SHIELD = ITEMS.register("diamond_shield",
            () -> new ShieldItem(new ModItems().defaultDurability(2098)){
                @Override
                public int getMaxDamage(ItemStack stack){
                    return Config.DIAMOND_SHIELD_DURABILITY.get();
                }

                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    consumer.accept(new IClientItemExtensions() {

                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return ShieldRenderer.instance;
                        }
                    });
                }
            });

    public static final DeferredItem<Item> NETHERITE_SHIELD = ITEMS.register("netherite_shield",
            () -> new ShieldItem(new ModItems().defaultDurability(2731)){
                @Override
                public int getMaxDamage(ItemStack stack){
                    return Config.NETHERITE_SHIELD_DURABILITY.get();
                }

                @Override
                public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                    consumer.accept(new IClientItemExtensions() {

                        @Override
                        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                            return ShieldRenderer.instance;
                        }
                    });
                }
            });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
