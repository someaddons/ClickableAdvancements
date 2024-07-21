package com.clickadv;

import com.clickadv.config.CommonConfiguration;
import com.clickadv.event.ClientEventHandler;
import com.cupboard.config.CupboardConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.clickadv.ClickAdvancements.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class ClickAdvancements
{
    public static final String                              MODID  = "clickadv";
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();

    public ClickAdvancements(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::clientSetup);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event)
    {
        // Side safe client event handler
        NeoForge.EVENT_BUS.register(ClientEventHandler.class);
    }
}
