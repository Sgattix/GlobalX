package it.globalx.bungee.module.implementations.chatfiltering.listener;

import it.globalx.bungee.utils.PendingChatMessages;
import it.globalx.proxy.chatfiltering.check.result.CheckResult;
import it.globalx.proxy.chatfiltering.utils.FilterUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@RequiredArgsConstructor
public class ChatFilterListener implements Listener {
    private final FilterUtils filterUtils;

    // Runs early so PlayerFormat (HIGH) sees the filtered/blocked result
    @EventHandler(priority = EventPriority.LOW)
    public void onChat(ChatEvent event) {
        if (event.isCommand() || !(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }

        CheckResult result = filterUtils.check(event.getMessage());

        if (result == null) {
            return;
        }

        switch (result.checkResultType()) {
            case BLOCKED -> event.setCancelled(true);
            case ALLOWED, REPLACED -> {
                if (!result.newMessage().equals(event.getMessage())) {
                    PendingChatMessages.set(player.getUniqueId(), result.newMessage());
                }
            }
        }
    }
}
