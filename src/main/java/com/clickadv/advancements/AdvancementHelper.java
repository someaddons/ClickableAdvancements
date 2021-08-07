package com.clickadv.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;

public class AdvancementHelper
{
    public static final String COMMAND = "/advopen ";

    public static ITextComponent buildAdvancementChatInfo(final Advancement advancement)
    {
        final TranslationTextComponent org = (TranslationTextComponent) advancement.getChatComponent();
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
