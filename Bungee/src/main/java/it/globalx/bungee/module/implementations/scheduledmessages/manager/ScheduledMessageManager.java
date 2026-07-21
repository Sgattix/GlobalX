package it.globalx.bungee.module.implementations.scheduledmessages.manager;

import com.google.common.base.Enums;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.scheduledmessages.messages.ScheduledMessage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.config.ServerInfo;

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
            Set<ServerInfo> servers = new HashSet<>();

            for (String serverName : section.getStringList(name + ".Servers")) {
                if (serverName.equalsIgnoreCase("*")) {
                    servers.clear();
                    servers.addAll(ProxyServer.getInstance().getServers().values());
                    break;
                } else {
                    ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(serverName);

                    if (serverInfo == null) {
                        throw new IllegalArgumentException("Unknown server: " + serverName);
                    }

                    servers.add(serverInfo);
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
