package it.globalx.velocity.module.implementations.scheduledmessages.manager;

import com.google.common.base.Enums;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.velocity.module.Module;
import it.globalx.velocity.module.implementations.scheduledmessages.messages.ScheduledMessage;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduledMessageManager {

    private final Set<ScheduledMessage> scheduledMessages = new HashSet<>();

    public ScheduledMessageManager(Module module, Section section) {
        section.getKeys().forEach(name -> {
            // Skip scalar keys like "enabled"; only nested sections describe a message
            if (!section.isSection(name.toString())) {
                return;
            }

            String message = section.getString(name + ".Message");
            int timeoutSeconds = section.getInt(name + ".Timeout");
            boolean repeat = section.getBoolean(name + ".Repeat");
            Set<RegisteredServer> servers = new HashSet<>();

            for (String serverName : section.getStringList(name + ".Servers")) {
                if (serverName.equalsIgnoreCase("*")) {
                    servers.clear();
                    servers.addAll(module.plugin().getProxyServer().getAllServers());
                    break;
                } else {
                    servers.add(module.plugin().getProxyServer().getServer(serverName).orElseThrow());
                }
            }

            boolean actionEnabled = section.getBoolean(name + ".Click.Enabled");
            String actionName = section.getString(name + ".Click.Action");
            ClickEvent.Action action = actionName == null
                    ? null
                    : Enums.getIfPresent(ClickEvent.Action.class, actionName).orNull();
            String actionValue = section.getString(name + ".Click.Value");

            boolean hoverEnabled = section.getBoolean(name + ".Hover.Enabled");
            List<String> hoverText = section.getStringList(name + ".Hover.Content");

            ScheduledMessage scheduledMessage = new ScheduledMessage(message, timeoutSeconds, repeat, servers, actionEnabled, action, actionValue, hoverEnabled, hoverText);
            scheduledMessages.add(scheduledMessage);
        });
    }

    public Set<ScheduledMessage> getScheduledMessages() {
        return scheduledMessages;
    }

    public void removeScheduledMessage(ScheduledMessage scheduledMessage) {
        scheduledMessages.remove(scheduledMessage);
    }
}
