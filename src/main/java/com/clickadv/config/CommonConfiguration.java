package com.clickadv.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public boolean showAllInLocalChat = true;

    public CommonConfiguration()
    {

    }

    @Override
    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", " By default vanilla only displays advancements in chat which get broadcasted to all online players."
                                     + "Enabling this setting allows the non-broadcasted advancements to show up in your personal chat(other players dont see it)"
                                     + "Some of these may be lacking text when the advancement itself does not have any, e.g. Categories are advancements too. default: true");
        entry.addProperty("showAllInLocalChat", showAllInLocalChat);
        root.add("showAllInLocalChat", entry);

        return root;
    }

    @Override
    public void deserialize(JsonObject data)
    {
        showAllInLocalChat = data.get("showAllInLocalChat").getAsJsonObject().get("showAllInLocalChat").getAsBoolean();
    }
}
