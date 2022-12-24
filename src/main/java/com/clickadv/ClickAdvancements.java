package com.clickadv;

import com.clickadv.config.Configuration;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
public class ClickAdvancements implements ModInitializer
{
    public static final String        MODID  = "clickadv";
    public static final Logger        LOGGER = LogManager.getLogger();
    public static       Configuration config = new Configuration();
    public static       Random        rand   = new Random();

    @Override
    public void onInitialize()
    {
        config.load();
        LOGGER.info(MODID + " mod initialized");
    }
}
