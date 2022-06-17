package com.clickadv.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class AdvancementHelper
{
    public static final String COMMAND = "advopen ";

    public static Component buildAdvancementChatInfo(final Advancement advancement)
    {
        final MutableComponent org = (MutableComponent) advancement.getChatComponent();
        org.setStyle(org.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, COMMAND + advancement.getId())));
        return org;
    }

    /**
     * Get the advancement ID from chat info
     *
     * @param chatcommand
     * @return
     */
    public static ResourceLocation getAdvancementID(final String chatcommand)
    {
        return ResourceLocation.tryParse(chatcommand.replace(COMMAND, ""));
    }
}
