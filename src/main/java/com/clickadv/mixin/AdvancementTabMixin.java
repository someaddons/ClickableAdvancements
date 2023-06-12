package com.clickadv.mixin;

import com.clickadv.event.IAdvancementTabGetter;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AdvancementTab.class)
public class AdvancementTabMixin implements IAdvancementTabGetter
{

    @Shadow
    private int maxX;

    @Shadow
    private int minY;

    @Shadow
    private int minX;

    @Shadow
    private int maxY;

    @Override
    public int maxX()
    {
        return maxX;
    }

    @Override
    public int minX()
    {
        return minX;
    }

    @Override
    public int minY()
    {
        return minY;
    }

    @Override
    public int maxY()
    {
        return maxY;
    }
}
