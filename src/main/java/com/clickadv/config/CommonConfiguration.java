package com.clickadv.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration
{
    public final ForgeConfigSpec                      ForgeConfigSpecBuilder;
    public final ForgeConfigSpec.ConfigValue<Boolean> showAllInLocalChat;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Config category");

        builder.comment("By default vanilla only displays advancements in chat which get broadcasted to all online players,"
                          + "this setting turns on showing all advancements achieved in chat instead. That way you can find"
                          + "the advancements you got easier/see better which you get. Some of these may have no actual text though because"
                          + " the advancement itself doesnt have any, e.g. Categories are advancements too. default = true");
        showAllInLocalChat = builder.define("showAllInLocalChat", true);

        // Escapes the current category level
        builder.pop();
        ForgeConfigSpecBuilder = builder.build();
    }
}
