package it.globalx.velocity.module.implementations.chatfiltering.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import it.globalx.proxy.chatfiltering.check.result.CheckResult;
import it.globalx.proxy.chatfiltering.utils.FilterUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatFilterListener {
    private final FilterUtils filterUtils;

    // Runs early so PlayerFormat (LATE) sees the filtered/blocked result
    @Subscribe(order = PostOrder.EARLY)
    public void onChat(PlayerChatEvent event) {
        CheckResult result = filterUtils.check(event.getMessage());

        if (result == null) {
            return;
        }

        switch (result.checkResultType()) {
            case BLOCKED -> event.setResult(PlayerChatEvent.ChatResult.denied());
            case ALLOWED, REPLACED -> {
                if (!result.newMessage().equals(event.getMessage())) {
                    event.setResult(PlayerChatEvent.ChatResult.message(result.newMessage()));
                }
            }
        }
    }
}
