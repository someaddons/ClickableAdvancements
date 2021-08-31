package com.clickadv.event;

import com.clickadv.advancements.AdvancementHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.advancements.AdvancementEntryGui;
import net.minecraft.client.gui.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatEvent;
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

    @SubscribeEvent
    public static void on(ClientChatEvent event)
    {
        if (event.getOriginalMessage().contains(AdvancementHelper.COMMAND) && Minecraft.getInstance().player != null)
        {
            event.setMessage("");

            final ClientAdvancementManager manager = Minecraft.getInstance().player.connection.getAdvancements();
            final ResourceLocation id = AdvancementHelper.getAdvancementID(event.getOriginalMessage());

            if (id == null)
            {
                return;
            }

            final Advancement advancement = manager.getAdvancements().get(id);
            Advancement tab = manager.getAdvancements().get(new ResourceLocation(id.getNamespace(), id.getPath().split("/")[0] + "/root"));

            if (tab == null)
            {
                tab = manager.getAdvancements().get(new ResourceLocation(id.getNamespace(), "root"));

                if (tab == null)
                {
                    tab = advancement;
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
                    return;
                }

                actualScreen.selectedTab.drawContents(new MatrixStack());
                final AdvancementEntryGui entry = actualScreen.getAdvancementWidget(advancement);

                final int midX = (actualScreen.selectedTab.maxX - actualScreen.selectedTab.minX) / 2;
                final int midY = (actualScreen.selectedTab.maxY - actualScreen.selectedTab.minY) / 2;

                actualScreen.selectedTab.scroll(midX - entry.getX(), midY - entry.getY());
            }

            if (Minecraft.getInstance().screen instanceof ClientAdvancementManager.IListener)
            {
                listener = (ClientAdvancementManager.IListener) Minecraft.getInstance().screen;
                flashingEntry = advancement;
                counter = 0;
                progressInfo = manager.progress.get(advancement);
            }
        }
    }

    static ClientAdvancementManager.IListener listener      = null;
    static Advancement                        flashingEntry = null;
    static AdvancementProgress                progressInfo  = null;
    static int                                counter       = 0;

    @SubscribeEvent
    public static void OnTick(TickEvent.ClientTickEvent event)
    {
        if (flashingEntry != null && Minecraft.getInstance().screen == listener)
        {
            if (++counter >= 10)
            {
                if (counter % 20 >= 10)
                {
                    listener.onUpdateAdvancementProgress(flashingEntry, null);
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
