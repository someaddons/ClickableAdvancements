package com.clickadv.event;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jline.utils.Log;

/**
 * Forge event bus handler, ingame events are fired here
 */
public class EventHandler
{
    @SubscribeEvent
    public static void onADv(final AdvancementEvent.AdvancementEarnEvent event)
    {
        if (event.getAdvancement().getDisplay() == null)
        {
            return;
        }

        if (!(event.getEntity() instanceof ServerPlayer))
        {
            return;
        }

        if (event.getEntity().getClass() != ServerPlayer.class && ((ServerPlayer) event.getEntity()).connection == null)
        {
            ClickAdvancements.LOGGER.error(
              "Trying to award advancement to a fake player which does not have a connection either, this is a bug in another mod and should not happen. printing trace: Entity:"
                + event.getEntity(),
              new Exception());
            return;
        }

        // Inverted condition of #PlayerAdvancements to exclude when we already sent it to all
        if (!(event.getAdvancement().getDisplay().shouldAnnounceChat() && event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)))
        {
            if (
              (ClickAdvancements.config.getCommonConfig().showAllInLocalChat && event.getAdvancement().getParent() != null && event.getAdvancement().getDisplay().shouldShowToast())
                || event.getAdvancement()
                .getDisplay()
                .shouldAnnounceChat())
            {
                if (event.getAdvancement().getChatComponent().getString().contains("recipe"))
                {
                    return;
                }

                MutableComponent desc = (MutableComponent) event.getAdvancement().getDisplay().getDescription();
                MutableComponent header = (MutableComponent) AdvancementHelper.buildAdvancementChatInfo(event.getAdvancement());

                int lenght = header.getString().length();

                if (desc != null)
                {
                    header.append(Component.literal(" ")).append(desc.setStyle(Style.EMPTY.applyFormat(ChatFormatting.WHITE)));
                    lenght += desc.getString().length();
                }

                if (lenght > 120)
                {
                    return;
                }

                ((ServerPlayer) event.getEntity()).sendSystemMessage(header, false);
            }
        }
    }
}
