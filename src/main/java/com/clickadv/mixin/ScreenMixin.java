package com.clickadv.mixin;

import com.clickadv.ClickAdvancements;
import com.clickadv.event.ClientEventHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin
{
    @Inject(method = "handleComponentClicked", at = @At("HEAD"), cancellable = true)
    private void onSendText(final Style style, final CallbackInfoReturnable<Boolean> cir)
    {
        if (style == null)
        {
            return;
        }

        final ClickEvent event = style.getClickEvent();
        try
        {
            if (event != null && ClientEventHandler.onMessage(event.getValue()))
        {
            cir.setReturnValue(false);
        }
        }
        catch (Exception e)
        {
            ClickAdvancements.LOGGER.warn("Error during trying to jump to advancement, might be caused by conflicting mods changing the advancement screen", e);
            cir.setReturnValue(false);
        }
    }
}
