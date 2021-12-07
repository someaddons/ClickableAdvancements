package com.clickadv.mixin;

import com.clickadv.event.ClientEventHandler;
import com.clickadv.event.IAdvancementsScreenGetter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin extends Screen implements IAdvancementsScreenGetter
{
    @Shadow
    @Nullable
    private AdvancementTab selectedTab;

    protected AdvancementsScreenMixin(final Text title)
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
