package com.hyperoman.manyshields.item;

import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;

public class BaseShieldItem extends ShieldItem {
    private final float blockPercentage; // Portion of damage when actively using the shield
    private final float passiveBlockPercentage; // Portion of damage blocked passively while held

    public BaseShieldItem(float blockPercentage, float passiveBlockPercentage, Item.Properties properties){
        super(properties);
        this.blockPercentage = blockPercentage;
        this.passiveBlockPercentage = passiveBlockPercentage;
    }
}
