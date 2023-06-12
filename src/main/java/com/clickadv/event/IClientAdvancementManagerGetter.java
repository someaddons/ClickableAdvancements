package com.clickadv.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;

import java.util.Map;

public interface IClientAdvancementManagerGetter
{

    Map<Advancement, AdvancementProgress> getAdvancementProgressMap();
}
