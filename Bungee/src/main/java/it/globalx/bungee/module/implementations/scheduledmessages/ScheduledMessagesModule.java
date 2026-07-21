package it.globalx.bungee.module.implementations.scheduledmessages;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.scheduledmessages.manager.ScheduledMessageManager;
import it.globalx.bungee.module.implementations.scheduledmessages.tasks.ScheduledMessagesTasks;
import lombok.Getter;

@Getter
public class ScheduledMessagesModule extends Module {
    private ScheduledMessageManager scheduledMessageManager;

    public ScheduledMessagesModule() {
        super("scheduledmessages", "Send messages at a scheduled time", "ScheduledMessages");
    }

    @Override
    protected void onEnable(Section section) {
        scheduledMessageManager = new ScheduledMessageManager(this, section);

        new ScheduledMessagesTasks(this);
    }

    @Override
    public void onDisable() {

    }
}
