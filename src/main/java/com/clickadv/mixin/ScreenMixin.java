package com.clickadv.mixin;

import com.clickadv.event.ClientEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class ScreenMixin
{
    @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
    public void on(final Style style, final CallbackInfoReturnable<Boolean> cir)
    {
        final ClickEvent event = style.getClickEvent();

        if (event != null && ClientEventHandler.onMessage(event.getValue()))
        {
            cir.setReturnValue(false);
        }
    }
}
