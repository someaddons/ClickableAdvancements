package com.clickadv.event;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementTab;
import net.minecraft.client.gui.screen.advancement.AdvancementWidget;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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

        @Override
        public float getProgressBarPercentage()
        {
            return 105f;
        }
    };

    public static boolean onMessage(final String message)
    {
        if (message.contains(AdvancementHelper.COMMAND) && MinecraftClient.getInstance().player != null)
        {

            final ClientAdvancementManager manager = MinecraftClient.getInstance().player.networkHandler.getAdvancementHandler();
            final Identifier id = AdvancementHelper.getAdvancementID(message);

            if (id == null)
            {
                return true;
            }

            final Advancement advancement = manager.getManager().get(id);
            Advancement tab = manager.getManager().get(new Identifier(id.getNamespace(), id.getPath().split("/")[0] + "/root"));

            if (tab == null)
            {
                tab = manager.getManager().get(new Identifier(id.getNamespace(), "root"));

                if (tab == null)
                {
                    Advancement current = advancement;

                    if (current == null)
                    {
                        ClickAdvancements.LOGGER.info("Could not find advancement for id:"+ id);
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
            MinecraftClient.getInstance().setScreen(screen);
            manager.selectTab(tab, true);
            if (MinecraftClient.getInstance().currentScreen instanceof AdvancementsScreen)
            {
                final AdvancementsScreen actualScreen = (AdvancementsScreen) MinecraftClient.getInstance().currentScreen;
                final AdvancementTab selectedTab = ((IAdvancementsScreenGetter) actualScreen).getCurrentTab();
                if (selectedTab == null || advancement == null)
                {
                    return true;
                }

                selectedTab.render(new MatrixStack());
                final AdvancementWidget entry = actualScreen.getAdvancementWidget(advancement);

                final int midX = (((IAdvancementTabGetter) selectedTab).maxX() - ((IAdvancementTabGetter) selectedTab).minX()) / 2;
                final int midY = (((IAdvancementTabGetter) selectedTab).maxY() - ((IAdvancementTabGetter) selectedTab).minY()) / 2;

                selectedTab.move(midX - entry.getX(), midY - entry.getY());
            }

            if (MinecraftClient.getInstance().currentScreen instanceof ClientAdvancementManager.Listener)
            {
                listener = (ClientAdvancementManager.Listener) MinecraftClient.getInstance().currentScreen;
                flashingEntry = advancement;
                counter = 0;
                progressInfo = ((IClientAdvancementManagerGetter) manager).getAdvancementProgressMap().get(advancement);
            }

            return true;
        }

        return false;
    }

    static               ClientAdvancementManager.Listener listener      = null;
    static               Advancement                       flashingEntry = null;
    static               AdvancementProgress               progressInfo  = null;
    static               int                               counter       = 0;
    private static final AdvancementProgress               noProgress    = new AdvancementProgress();

    public static void onTick(final Screen screen)
    {
        if (flashingEntry != null && screen == listener)
        {
            if (++counter >= 10)
            {
                if (counter % 20 >= 10)
                {
                    listener.setProgress(flashingEntry, noProgress);
                }
                else
                {
                    listener.setProgress(flashingEntry, doneProgress);
                    if (counter > BLINK_TIME_TICKS)
                    {
                        counter = 0;
                        listener.setProgress(flashingEntry, progressInfo);
                        flashingEntry = null;
                    }
                }
            }
        }
    }
}
