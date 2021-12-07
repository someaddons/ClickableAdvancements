package com.clickadv.event;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;

import java.util.Map;

public interface IClientAdvancementManagerGetter
{

    Map<Advancement, AdvancementProgress> getAdvancementProgressMap();
}
