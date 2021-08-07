package com.clickadv.mixin;

import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerAdvancements.class)
public class PlayerAdvancementsMixin
{
    @Redirect(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/Advancement;getChatComponent()Lnet/minecraft/util/text/ITextComponent;"))
    private ITextComponent onGetComponent(final Advancement advancement)
    {
        return AdvancementHelper.buildAdvancementChatInfo(advancement);
    }
}
