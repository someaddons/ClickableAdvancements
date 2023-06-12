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

@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin extends Screen implements IAdvancementsScreenGetter
{
    @Shadow
    @Nullable
    private AdvancementTab selectedTab;

    protected AdvancementsScreenMixin(final Component title)
    {
        super(title);
    }

    @Override
    public AdvancementTab getCurrentTab()
    {
        return selectedTab;
    }

    @Override
    public void tick()
    {
        ClientEventHandler.onTick(this);
    }
}
