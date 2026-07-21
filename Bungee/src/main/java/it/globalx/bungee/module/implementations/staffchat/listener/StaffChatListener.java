package it.globalx.bungee.module.implementations.staffchat.listener;

import it.globalx.bungee.module.implementations.staffchat.StaffChatModule;
import it.globalx.proxy.staffchat.StaffChatUtils;
import it.globalx.bungee.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class StaffChatListener implements Listener {
    private final StaffChatModule staffChatModule;
    private final String staffChatFormat, staffChatPermission;

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.isCommand() || !(e.getSender() instanceof ProxiedPlayer player)) {
            return;
        }

        if (StaffChatUtils.isStaffChatEnabled(player.getUniqueId())) {
            e.setCancelled(true);

            BaseComponent[] message = ChatUtils.colorAndGetComponent(
                    staffChatFormat
                            .replace("%server%", player.getServer().getInfo().getName())
                            .replace("%player%", player.getName())
                            .replace("%message%", e.getMessage())
            );

            ProxyServer.getInstance().getPlayers()
                    .stream()
                    .filter(target -> target.hasPermission(staffChatPermission))
                    .forEach(target -> target.sendMessage(message));
        }
    }

}
