package com.clickadv.mixin;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin
{
    @Shadow
    private ServerPlayer player;

    @Redirect(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/Advancement;getChatComponent()Lnet/minecraft/network/chat/Component;"))
    private Component onGetComponent(final Advancement advancement)
    {
        return AdvancementHelper.buildAdvancementChatInfo(advancement);
    }

    @Inject(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerAdvancements;markForVisibilityUpdate(Lnet/minecraft/advancements/Advancement;)V"))
    public void onGrant(final Advancement advancement, final String criterionName, final CallbackInfoReturnable<Boolean> cir)
    {
        if (advancement.getDisplay() != null && !(advancement.getDisplay().shouldAnnounceChat() && player.level.getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)))
        {
            if ((ClickAdvancements.config.getCommonConfig().showAllInLocalChat && advancement.getParent() != null) || advancement
              .getDisplay()
              .shouldAnnounceChat())
            {
                if (advancement.getChatComponent().getString().contains("recipe"))
                {
                    return;
                }

                MutableComponent desc = (MutableComponent) advancement.getDisplay().getDescription();
                MutableComponent header = (MutableComponent) AdvancementHelper.buildAdvancementChatInfo(advancement);

                int lenght = header.getString().length();

                if (desc != null)
                {
                    header.append(Component.literal(" ")).append(desc.setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
                    lenght += desc.getString().length();
                }

                if (lenght > 120)
                {
                    return;
                }

                player.displayClientMessage(AdvancementHelper.buildAdvancementChatInfo(advancement), false);
            }
        }
    }
}
