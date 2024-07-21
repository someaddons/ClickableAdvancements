package com.clickadv.mixin;

import com.clickadv.ClickAdvancements;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin
{
    @Shadow
    private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerAdvancements;markForVisibilityUpdate(Lnet/minecraft/advancements/AdvancementHolder;)V"))
    public void onGrant(final AdvancementHolder advancementHolder, final String string, final CallbackInfoReturnable<Boolean> cir)
    {
        final Advancement advancement = advancementHolder.value();
        if (advancement.display().isPresent() && !(advancement.display().get().shouldAnnounceChat() && player.level()
                                                                                                         .getGameRules()
                                                                                                         .getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)))
        {
            if (!(player instanceof ServerPlayer))
            {
                return;
            }

            if (player.getClass() != ServerPlayer.class && ((ServerPlayer) player).connection == null)
            {
                ClickAdvancements.LOGGER.error(
                  "Trying to award advancement to a fake player which does not have a connection either, this is a bug in another mod and should not happen. printing trace: Entity:"
                    + player,
                  new Exception());
                return;
            }

            if ((ClickAdvancements.config.getCommonConfig().showAllInLocalChat
                   && advancement.parent().isPresent()
                   && advancement.display().isPresent()
                   && advancement.display().get().shouldShowToast())
                  || (advancement.display().isPresent() && advancement.display().get().shouldAnnounceChat()))
            {

                MutableComponent chatComponent = advancement.display().get().getType().createAnnouncement(advancementHolder, player);
                if (chatComponent == null || chatComponent.getString().contains("recipe"))
                {
                    return;
                }

                MutableComponent desc = (MutableComponent) advancement.display().get().getDescription();

                int lenght = chatComponent.getString().length();

                if (desc != null)
                {
                    chatComponent.append(Component.literal(" ")).append(desc.setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
                    lenght += desc.getString().length();
                }

                if (lenght > 120)
                {
                    chatComponent = advancement.display().get().getType().createAnnouncement(advancementHolder, player);
                    if (chatComponent.getString().length() > 120)
                    {
                        return;
                    }
                }

                player.displayClientMessage(chatComponent, false);
            }
        }
    }
}
