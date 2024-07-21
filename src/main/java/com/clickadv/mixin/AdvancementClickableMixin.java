package com.clickadv.mixin;

import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdvancementType.class)
public class AdvancementClickableMixin
{
    @Inject(method = "createAnnouncement", at = @At("RETURN"), cancellable = true)
    private void addToAnnouncement(final AdvancementHolder advancementHolder, final ServerPlayer serverPlayer, final CallbackInfoReturnable<MutableComponent> cir)
    {
        cir.setReturnValue(AdvancementHelper.buildAdvancementChatInfo(cir.getReturnValue(), advancementHolder, serverPlayer));
    }
}
