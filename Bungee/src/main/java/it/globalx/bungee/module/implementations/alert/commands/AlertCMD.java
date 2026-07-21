package it.globalx.bungee.module.implementations.alert.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import it.globalx.bungee.module.implementations.alert.manager.ServerGroupsManager;
import it.globalx.bungee.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("alert")
@RequiredArgsConstructor
public class AlertCMD extends BaseCommand {
    private final ServerGroupsManager serverGroupsManager;
    private final String permission, noPermissionMessage, wrongUsage, invalidGroup, mustSpecifyMessage, format;

    @Default
    @HelpCommand
    public void onDefault(ProxiedPlayer player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }

        player.sendMessage(ChatUtils.colorAndGetComponent(wrongUsage));
    }

    @Default
    public void onDefault(ProxiedPlayer player, String group, String[] args) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }

        if (!serverGroupsManager.hasGroup(group)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(invalidGroup));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(ChatUtils.colorAndGetComponent(mustSpecifyMessage));
            return;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        String formatted = format
                .replace("%player%", player.getName())
                .replace("%message%", message.toString().trim());

        BaseComponent[] components = ChatUtils.colorAndGetComponent(formatted);

        serverGroupsManager.getServers(group).forEach(server ->
                server.getPlayers().forEach(recipient -> recipient.sendMessage(components))
        );
    }
}
