package com.hyperoman.manyshields;

import com.hyperoman.manyshields.item.ModItems;
import com.hyperoman.manyshields.util.ModRecipeSerializer;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ManyShields.MODID)
public class ManyShields {

    public static final String MODID = "manyshields";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ManyShields(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC, "bettershields-server.toml");
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        ModItems.register(modEventBus);
        ModRecipeSerializer.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
