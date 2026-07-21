package it.globalx.bungee.module.implementations.scheduledmessages.tasks;

import it.globalx.bungee.GlobalXBungee;
import it.globalx.bungee.module.implementations.scheduledmessages.ScheduledMessagesModule;
import it.globalx.bungee.module.implementations.scheduledmessages.messages.ScheduledMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ScheduledMessagesTasks implements Runnable {
    private final ScheduledMessagesModule instance;
    private final HashMap<ScheduledMessage, Integer> tickMap = new HashMap<>();
    private final ScheduledTask task;

    public ScheduledMessagesTasks(ScheduledMessagesModule instance) {
        this.instance = instance;
        this.task = ProxyServer.getInstance().getScheduler().schedule(GlobalXBungee.getInstance(), this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        Set<ScheduledMessage> scheduledMessages = instance.getScheduledMessageManager().getScheduledMessages();
        if (scheduledMessages.size() == 0) {
            task.cancel();
            return;
        }

        scheduledMessages
                .forEach(scheduledMessage -> {
                    int diff = scheduledMessage.timeoutSeconds() - tickMap.getOrDefault(scheduledMessage, 0);

                    if (diff <= 0) {
                        scheduledMessage.execute();
                        if (scheduledMessage.repeat()) {
                            tickMap.put(scheduledMessage, 0);
                            return;
                        }

                        tickMap.remove(scheduledMessage);
                        instance.getScheduledMessageManager().removeScheduledMessage(scheduledMessage);
                        return;
                    }
                    tickMap.put(scheduledMessage, tickMap.getOrDefault(scheduledMessage, 0) + 1);
                });
    }
}
