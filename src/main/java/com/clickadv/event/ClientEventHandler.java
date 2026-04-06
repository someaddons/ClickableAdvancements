package com.clickadv.event;

import com.clickadv.ClickAdvancements;
import com.clickadv.advancements.AdvancementHelper;
import com.cupboard.util.ResourceLocation;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.advancements.AdvancementWidget;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.multiplayer.ClientAdvancements;

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
        public float getPercent()
        {
            return 105f;
        }
    };

    public static boolean onMessage(final String message)
    {
        if (message.contains(AdvancementHelper.COMMAND) && Minecraft.getInstance().player != null)
        {

            final ClientAdvancements manager = Minecraft.getInstance().player.connection.getAdvancements();
            final ResourceLocation id = AdvancementHelper.getAdvancementID(message);

            if (id == null)
            {
                return true;
            }

            final AdvancementHolder advancementHolder = manager.get(id);
            if (advancementHolder == null)
            {
                return true;
            }

            final Advancement advancement = advancementHolder.value();
            AdvancementHolder tab = manager.get(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath().split("/")[0] + "/root"));
            if (tab == null)
            {
                tab = manager.get(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "root"));

                if (tab == null)
                {
                    Advancement current = advancement;

                    if (current == null)
                    {
                        ClickAdvancements.LOGGER.info("Could not find advancement for id:" + id);
                        return true;
                    }

                    for (int i = 0; i < 20; i++)
                    {
                        if (current.parent().isPresent() && manager.get(current.parent().get()) != null)
                        {
                            tab = manager.get(current.parent().get());
                            current = manager.get(current.parent().get()).value();
                        }
                        else
                        {
                            break;
                        }
                    }

                    if (tab == null)
                    {
                        tab = advancementHolder;
                    }
                }
            }

            final AdvancementsScreen screen = new AdvancementsScreen(manager);
            Minecraft.getInstance().setScreen(screen);
            manager.setSelectedTab(tab, true);
            if (Minecraft.getInstance().screen instanceof AdvancementsScreen)
            {
                final AdvancementsScreen actualScreen = (AdvancementsScreen) Minecraft.getInstance().screen;
                if (!(actualScreen.selectedTab instanceof IAdvancementTabSetter advancementTabSetter) || advancement == null)
                {
                    return true;
                }

                final AdvancementWidget entry = actualScreen.getAdvancementWidget(manager.getTree().get(advancementHolder.id()));
                advancementTabSetter.setFocusWidget(entry);
            }

            if (Minecraft.getInstance().screen instanceof ClientAdvancements.Listener)
            {
                listener = (ClientAdvancements.Listener) Minecraft.getInstance().screen;
                flashingEntry = manager.getTree().get(advancementHolder.id());
                counter = 0;
                progressInfo = ((IClientAdvancementManagerGetter) manager).getAdvancementProgressMap().get(advancementHolder);
            }

            return true;
        }
        return false;
    }

    static ClientAdvancements.Listener listener      = null;
    static AdvancementNode             flashingEntry = null;
    static AdvancementProgress         progressInfo  = null;
    static               int                         counter       = 0;
    private static final AdvancementProgress         noProgress    = new AdvancementProgress();

    public static void onTick(final Screen screen)
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
