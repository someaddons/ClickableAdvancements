package com.clickadv.advancements;

import net.minecraft.advancement.Advancement;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AdvancementHelper
{
    public static final String COMMAND = "advopen ";

    public static Text buildAdvancementChatInfo(final Advancement advancement)
    {
        final MutableText org = (MutableText) advancement.toHoverableText();
        org.setStyle(org.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, COMMAND + advancement.getId())));
        return org;
    }

    /**
     * Get the advancement ID from chat info
     *
     * @param chatcommand
     * @return
     */
    public static Identifier getAdvancementID(final String chatcommand)
    {
        return Identifier.tryParse(chatcommand.replace(COMMAND, ""));
    }
}
