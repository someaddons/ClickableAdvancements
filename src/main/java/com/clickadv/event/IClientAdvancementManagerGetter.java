package com.clickadv.event;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;

import java.util.Map;

public interface IClientAdvancementManagerGetter
{
    Map<AdvancementHolder, AdvancementProgress> getAdvancementProgressMap();
}
