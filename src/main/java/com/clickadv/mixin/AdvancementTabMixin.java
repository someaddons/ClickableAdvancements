package com.clickadv.mixin;

import com.clickadv.event.IAdvancementTabGetter;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementTab.class)
public class AdvancementTabMixin implements IAdvancementTabGetter
{

    @Shadow
    private int maxPanX;

    @Shadow
    private int minPanY;

    @Shadow
    private int minPanX;

    @Shadow
    private int maxPanY;

    @Override
    public int maxX()
    {
        return maxPanX;
    }

    @Override
    public int minX()
    {
        return minPanX;
    }

    @Override
    public int minY()
    {
        return minPanY;
    }

    @Override
    public int maxY()
    {
        return maxPanY;
    }
}
