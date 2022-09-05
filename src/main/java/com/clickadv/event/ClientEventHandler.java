package com.clickadv.event;

import com.clickadv.advancements.AdvancementHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler
{
    /**
     * Blink time
     */
    private static final int BLINK_TIME_TICKS = 20 * 8;

    private static final AdvancementProgress doneProgress = new AdvancementProgress()
    {
        @Override
        public boolean isDone()
        {
            return true;
        }

        @OnlyIn(Dist.CLIENT)
        public float getPercent()
        {
            return 105f;
        }
    };

    public static boolean onMessage(String message)
    {
        if (message.contains(AdvancementHelper.COMMAND) && Minecraft.getInstance().player != null)
        {
            final ClientAdvancements manager = Minecraft.getInstance().player.connection.getAdvancements();
            final ResourceLocation id = AdvancementHelper.getAdvancementID(message);

            if (id == null)
            {
                return true;
            }

            final Advancement advancement = manager.getAdvancements().get(id);
            Advancement tab = manager.getAdvancements().get(new ResourceLocation(id.getNamespace(), id.getPath().split("/")[0] + "/root"));

            if (tab == null)
            {
                tab = manager.getAdvancements().get(new ResourceLocation(id.getNamespace(), "root"));

                if (tab == null)
                {
                    Advancement current = advancement;

                    if (advancement == null)
                    {
                        return true;
                    }

                    for (int i = 0; i < 20; i++)
                    {
                        if (current.getParent() != null)
                        {
                            tab = current.getParent();
                            current = current.getParent();
                        }
                        else
                        {
                            break;
                        }
                    }

                    if (tab == null)
                    {
                        tab = advancement;
                    }
                }
            }

            final AdvancementsScreen screen = new AdvancementsScreen(manager);
            Minecraft.getInstance().setScreen(screen);
            manager.setSelectedTab(tab, true);
            if (Minecraft.getInstance().screen instanceof AdvancementsScreen)
            {
                final AdvancementsScreen actualScreen = (AdvancementsScreen) Minecraft.getInstance().screen;
                if (actualScreen.selectedTab == null || advancement == null)
                {
                    return true;
                }

                actualScreen.selectedTab.drawContents(new PoseStack());
                final AdvancementWidget entry = actualScreen.getAdvancementWidget(advancement);

                final int midX = (actualScreen.selectedTab.maxX - actualScreen.selectedTab.minX) / 2;
                final int midY = (actualScreen.selectedTab.maxY - actualScreen.selectedTab.minY) / 2;

                actualScreen.selectedTab.scroll(midX - entry.getX(), midY - entry.getY());
            }

            if (Minecraft.getInstance().screen instanceof ClientAdvancements.Listener)
            {
                listener = (ClientAdvancements.Listener) Minecraft.getInstance().screen;
                flashingEntry = advancement;
                counter = 0;
                progressInfo = manager.progress.get(advancement);
            }

            return true;
        }
        return false;
    }

    static               ClientAdvancements.Listener listener      = null;
    static               Advancement                 flashingEntry = null;
    static               AdvancementProgress         progressInfo  = null;
    static               int                         counter       = 0;
    private static final AdvancementProgress         noProgress    = new AdvancementProgress();

    @SubscribeEvent
    public static void OnTick(TickEvent.ClientTickEvent event)
    {
        if (flashingEntry != null && Minecraft.getInstance().screen == listener)
        {
            if (++counter >= 10)
            {
                if (counter % 20 >= 10)
                {
                    listener.onUpdateAdvancementProgress(flashingEntry, noProgress);
                }
                else
                {
                    listener.onUpdateAdvancementProgress(flashingEntry, doneProgress);
                    if (counter > BLINK_TIME_TICKS)
                    {
                        counter = 0;
                        listener.onUpdateAdvancementProgress(flashingEntry, progressInfo);
                        flashingEntry = null;
                    }
                }
            }
        }
    }
}
