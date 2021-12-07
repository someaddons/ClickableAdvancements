package com.clickadv.mixin;

import com.clickadv.event.ClientEventHandler;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin
{
    @Inject(method = "sendMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void on(final String message, final boolean toHud, final CallbackInfo ci)
    {
        if (ClientEventHandler.onMessage(message))
        {
            ci.cancel();
        }
    }
}
