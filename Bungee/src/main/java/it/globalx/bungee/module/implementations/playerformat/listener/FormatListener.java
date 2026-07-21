package it.globalx.bungee.module.implementations.playerformat.listener;

import it.globalx.bungee.module.implementations.playerformat.PlayerFormatModule;
import it.globalx.bungee.module.implementations.playerformat.format.PlayerFormat;
import it.globalx.bungee.utils.ChatUtils;
import it.globalx.bungee.utils.PendingChatMessages;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

@RequiredArgsConstructor
public class FormatListener implements Listener {
    private final PlayerFormatModule playerFormatModule;

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        //todo: setPlayerListName with Spigot Bridge
    }

    // Runs late so it can honour the chat filter's decision (ChatFilterListener is LOW)
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(ChatEvent event) {
        if (event.isCommand() || !(event.getSender() instanceof ProxiedPlayer player)) {
            return;
        }

        // The chat filter blocked the message: drop it instead of re-broadcasting
        if (event.isCancelled()) {
            return;
        }

        PlayerFormat playerFormat = playerFormatModule.getFormatManager().getFormat(player).orElse(null);

        if (playerFormat == null) {
            return;
        }

        // Use the filtered message if the chat filter rewrote it, else the original
        String rawMessage = PendingChatMessages.consume(player.getUniqueId()).orElse(event.getMessage());

        event.setCancelled(true);

        Server server = player.getServer();

        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());

        String prefix = "", suffix = "";

        if (user != null) {
            if (user.getCachedData().getMetaData().getPrefix() != null) {
                prefix = user.getCachedData().getMetaData().getPrefix();
            } else if (LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()).getCachedData().getMetaData().getPrefix() != null) {
                prefix = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()).getCachedData().getMetaData().getPrefix();
            }
            if (user.getCachedData().getMetaData().getSuffix() != null) {
                suffix = user.getCachedData().getMetaData().getSuffix();
            } else if (LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()).getCachedData().getMetaData().getSuffix() != null) {
                suffix = LuckPermsProvider.get().getGroupManager().getGroup(user.getPrimaryGroup()).getCachedData().getMetaData().getSuffix();
            }
        }

        if (prefix == null) {
            prefix = "";
        }

        if (suffix == null) {
            suffix = "";
        }

        String chatFormat = playerFormat.chatFormat()
                .replace("%player%", player.getName())
                .replace("%message%", rawMessage)
                .replace("%luckperms_prefix%", prefix)
                .replace("%luckperms_suffix%", suffix);

        BaseComponent[] message = ChatUtils.colorAndGetComponent(chatFormat);

        server.getInfo().getPlayers().forEach(target -> target.sendMessage(message));
    }

}
