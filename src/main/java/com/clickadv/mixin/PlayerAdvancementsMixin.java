package com.clickadv.mixin;

import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.PlayerAdvancements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin
{
    @Redirect(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/Advancement;getChatComponent()Lnet/minecraft/network/chat/Component;"))
    private Component onGetComponent(final Advancement advancement)
    {
        return AdvancementHelper.buildAdvancementChatInfo(advancement);
    }
}
