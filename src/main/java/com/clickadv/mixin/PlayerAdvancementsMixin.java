package com.clickadv.mixin;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementsMixin
{
    @Shadow
    private ServerPlayerEntity owner;

    @Redirect(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/Advancement;toHoverableText()Lnet/minecraft/text/Text;"))
    private Text onGetComponent(final Advancement advancement)
    {
        return AdvancementHelper.buildAdvancementChatInfo(advancement);
    }

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    public void onGrant(final Advancement advancement, final String criterionName, final CallbackInfoReturnable<Boolean> cir)
    {
        if (advancement.getDisplay() != null && !(advancement.getDisplay().shouldAnnounceToChat() && owner.world.getGameRules().getBoolean(GameRules.ANNOUNCE_ADVANCEMENTS)))
        {
            if ((ClickAdvancements.config.getCommonConfig().showAllInLocalChat && advancement.getParent() != null) || advancement
              .getDisplay()
              .shouldAnnounceToChat())
            {
                if (advancement.toHoverableText().getString().contains("recipe"))
                {
                    return;
                }

                MutableText desc = (MutableText) advancement.getDisplay().getDescription();
                MutableText header = (MutableText) AdvancementHelper.buildAdvancementChatInfo(advancement);

                int lenght = header.getString().length();

                if (desc != null)
                {
                    header.append(Text.literal(" ")).append(desc.setStyle(Style.EMPTY.withFormatting(Formatting.WHITE)));
                    lenght += desc.getString().length();
                }

                if (lenght > 120)
                {
                    return;
                }

                owner.sendMessage(AdvancementHelper.buildAdvancementChatInfo(advancement), false);
            }
        }
    }
}
