package com.clickadv.event;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Forge event bus handler, ingame events are fired here
 */
public class EventHandler
{
    @SubscribeEvent
    public static void onADv(final AdvancementEvent event)
    {
        if (event.getAdvancement().getDisplay() == null)
        {
            return;
        }

        // Inverted condition of #PlayerAdvancements to exclude when we already sent it to all
        if (!(event.getAdvancement().getDisplay().shouldAnnounceChat() && event.getEntity().level.getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)))
        {
            if ((ClickAdvancements.config.getCommonConfig().showAllInLocalChat.get() && event.getAdvancement().getParent() != null) || event.getAdvancement()
              .getDisplay()
              .shouldAnnounceChat())
            {
                ((ServerPlayer) event.getEntity()).sendSystemMessage(AdvancementHelper.buildAdvancementChatInfo(event.getAdvancement()), ChatType.CHAT);
            }
        }
    }
}
