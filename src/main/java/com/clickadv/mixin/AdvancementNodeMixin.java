package com.clickadv.mixin;

import com.clickadv.ClickAdvancements;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.minecraft.advancements.AdvancementNode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

/**
 * Sorts tab displays, or keeps insertion order
 */
@Mixin(AdvancementNode.class)
public class AdvancementNodeMixin
{
    @Shadow
    @Final
    private Set<AdvancementNode> children = !ClickAdvancements.config.getCommonConfig().sortDisplayAlphabetically
        ? Collections.newSetFromMap(new Reference2ObjectLinkedOpenHashMap<>())
        : Collections.newSetFromMap(new TreeMap<>(Comparator.comparing(advancementNode -> {
            final String name;
            if (advancementNode.holder().value().display().isPresent())
            {
                name = advancementNode.holder().value().display().get().getTitle().getString();
            }
            else
            {
                name = advancementNode.toString();
            }
            return name;
        })));
}
