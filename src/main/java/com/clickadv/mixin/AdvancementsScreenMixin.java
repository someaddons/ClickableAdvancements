package com.clickadv.mixin;

import com.clickadv.event.ClientEventHandler;
import com.clickadv.event.IAdvancementsScreenGetter;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin extends Screen implements IAdvancementsScreenGetter
{
    @Shadow
    @Nullable
    public AdvancementTab selectedTab;

    protected AdvancementsScreenMixin(final Component title)
    {
        super(title);
    }

    @Override
    public AdvancementTab getCurrentTab()
    {
        return selectedTab;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ontick(final CallbackInfo ci)
    {
        ClientEventHandler.onTick(this);
    }
}
