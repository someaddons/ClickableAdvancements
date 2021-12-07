package com.clickadv.config;

import com.clickadv.ClickAdvancements;
import com.google.gson.JsonObject;

public class CommonConfiguration
{
    public boolean showAllInLocalChat = true;

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();
        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "By default vanilla only displays advancements in chat which get broadcasted to all online players,"
                                     + "this setting turns on showing all advancements achieved in chat instead. That way you can find"
                                     + "the advancements you got easier/see better which you get. Some of these may have no actual text though because"
                                     + " the advancement itself doesnt have any, e.g. Categories are advancements too. default = true");
        entry.addProperty("showAllInLocalChat", showAllInLocalChat);
        root.add("showAllInLocalChat", entry);
        return root;
    }

    public void deserialize(JsonObject data)
    {
        if (data == null)
        {
            ClickAdvancements.LOGGER.error("Config file was empty!");
            return;
        }

        try
        {
            showAllInLocalChat = data.get("showAllInLocalChat").getAsJsonObject().get("showAllInLocalChat").getAsBoolean();
        }
        catch (Exception e)
        {
            ClickAdvancements.LOGGER.error("Could not parse config file", e);
        }
    }
}
