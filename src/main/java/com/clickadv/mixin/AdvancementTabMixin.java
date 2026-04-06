package com.clickadv.mixin;

import com.clickadv.event.IAdvancementTabSetter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementTab.class)
public abstract class AdvancementTabMixin implements IAdvancementTabSetter
{
    @Shadow
    private boolean centered;

    @Shadow
    public abstract void scroll(final double x, final double y);

    @Shadow
    private double scrollX;
    @Shadow
    private double scrollY;

    @Unique
    private AdvancementWidget focusWidget = null;

    @Inject(method = "drawContents", at = @At("HEAD"))
    private void initPosition(final GuiGraphics p_282728_, final int p_282962_, final int p_281511_, final CallbackInfo ci)
    {
        if (!centered && focusWidget != null)
        {
            centered = true;
            scroll((-focusWidget.getX() + 50) - scrollX, (-focusWidget.getY() + 50) - scrollY);
        }
    }

    @Override
    public void setFocusWidget(AdvancementWidget widget)
    {
        focusWidget = widget;
    }
}
