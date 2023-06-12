package com.clickadv.mixin;

import com.clickadv.event.IClientAdvancementManagerGetter;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementManagerMixin implements IClientAdvancementManagerGetter
{
    @Shadow
    @Final
    private Map<Advancement, AdvancementProgress> progress;

    @Override
    public Map<Advancement, AdvancementProgress> getAdvancementProgressMap()
    {
        return progress;
    }
}
