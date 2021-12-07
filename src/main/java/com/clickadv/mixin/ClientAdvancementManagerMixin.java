package com.clickadv.mixin;

import com.clickadv.event.IClientAdvancementManagerGetter;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.network.ClientAdvancementManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ClientAdvancementManager.class)
public class ClientAdvancementManagerMixin implements IClientAdvancementManagerGetter
{
    @Shadow
    @Final
    private Map<Advancement, AdvancementProgress> advancementProgresses;

    @Override
    public Map<Advancement, AdvancementProgress> getAdvancementProgressMap()
    {
        return advancementProgresses;
    }
}
