package com.clickadv.config;

import com.clickadv.ClickAdvancements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration
{
    /**
     * Loaded everywhere, not synced
     */
    private final CommonConfiguration commonConfig = new CommonConfiguration();

    /**
     * Loaded clientside, not synced
     */
    // private final ClientConfiguration clientConfig;

    /**
     * Builds configuration tree.
     */
    public Configuration()
    {
    }

    public void load()
    {
        final Path configPath = FabricLoader.getInstance().getConfigDir().normalize().resolve(ClickAdvancements.MODID + ".json");
        final File config = configPath.toFile();

        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!config.exists())
        {
            ClickAdvancements.LOGGER.warn("Config for dynamic view not found, recreating default");
            try
            {
                final BufferedWriter writer = Files.newBufferedWriter(configPath);
                gson.toJson(commonConfig.serialize(), JsonObject.class, writer);
                writer.close();
            }
            catch (IOException e)
            {
                ClickAdvancements.LOGGER.error("Could not write config to:" + configPath, e);
            }
        }
        else
        {
            try
            {
                commonConfig.deserialize(gson.fromJson(Files.newBufferedReader(configPath), JsonObject.class));
            }
            catch (IOException e)
            {
                ClickAdvancements.LOGGER.error("Could not read config from:" + configPath, e);
            }
        }
    }

    public CommonConfiguration getCommonConfig()
    {
        return commonConfig;
    }
}
