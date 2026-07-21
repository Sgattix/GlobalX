package it.globalx.bungee.module.implementations.scheduledmessages.messages;

import it.globalx.bungee.utils.ChatUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;
import java.util.Set;

public record ScheduledMessage(String message, int timeoutSeconds, boolean repeat, Set<ServerInfo> servers,
                               boolean actionEnabled, ClickEvent.Action action, String actionValue, boolean hoverEnabled,
                               List<String> hoverValue) {
    public void execute() {
        BaseComponent[] components = ChatUtils.colorAndGetComponent(message);

        if (actionEnabled) {
            ClickEvent clickEvent = new ClickEvent(action, actionValue);
            for (BaseComponent component : components) {
                component.setClickEvent(clickEvent);
            }
        }

        if (hoverEnabled) {
            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.colorAndGetComponent(String.join("\n", hoverValue)));
            for (BaseComponent component : components) {
                component.setHoverEvent(hoverEvent);
            }
        }

        servers.forEach(server -> server.getPlayers().forEach(player -> player.sendMessage(components)));
    }
}
