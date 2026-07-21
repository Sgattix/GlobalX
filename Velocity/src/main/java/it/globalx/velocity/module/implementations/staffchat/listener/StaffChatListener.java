package it.globalx.velocity.module.implementations.staffchat.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import it.globalx.proxy.staffchat.StaffChatUtils;
import it.globalx.velocity.module.implementations.staffchat.StaffChatModule;
import it.globalx.velocity.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StaffChatListener {
    private final StaffChatModule staffChatModule;
    private final String staffChatFormat, staffChatPermission;

    @Subscribe
    public void onChat(PlayerChatEvent e) {
        Player player = e.getPlayer();

        if (StaffChatUtils.isStaffChatEnabled(player.getUniqueId())) {
            e.setResult(PlayerChatEvent.ChatResult.denied());

            Component message = ChatUtils.colorAndGetComponent(
                    staffChatFormat
                            .replace("%server%", e.getPlayer().getCurrentServer().orElseThrow().getServerInfo().getName())
                            .replace("%player%", e.getPlayer().getUsername())
                            .replace("%message%", e.getMessage())
            );

            staffChatModule
                    .plugin()
                    .getProxyServer()
                    .getAllPlayers()
                    .stream()
                    .filter(target -> target.hasPermission(staffChatPermission))
                    .collect(Collectors.toSet())
                    .forEach(target -> {
                        target.sendMessage(message);
                    });
        }
    }

}
