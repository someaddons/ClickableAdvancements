package com.clickadv.mixin;

import com.clickadv.event.IAdvancementTabSetter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
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
    @Shadow
    private int    maxX;
    @Shadow
    private int    maxY;

    @Shadow
    public abstract AdvancementsScreen getScreen();

    @Unique
    private AdvancementWidget focusWidget = null;

    @Inject(method = "extractContents", at = @At("HEAD"))
    private void initPosition(final GuiGraphicsExtractor graphics, final int windowLeft, final int windowTop, final CallbackInfo ci)
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
