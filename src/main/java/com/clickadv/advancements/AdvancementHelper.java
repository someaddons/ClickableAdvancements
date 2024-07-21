package com.clickadv.advancements;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class AdvancementHelper
{
    public static final String COMMAND = "advopen ";

    public static MutableComponent buildAdvancementChatInfo(
      @NotNull MutableComponent filledComponent,
      @NotNull final AdvancementHolder advancementHolder,
      final ServerPlayer serverPlayer)
    {
        filledComponent.setStyle(filledComponent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, COMMAND + advancementHolder.id())));
        return filledComponent;
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
