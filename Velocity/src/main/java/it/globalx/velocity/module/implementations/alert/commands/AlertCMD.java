package it.globalx.velocity.module.implementations.alert.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import com.velocitypowered.api.proxy.Player;
import it.globalx.velocity.module.implementations.alert.manager.ServerGroupsManager;
import it.globalx.velocity.utils.ChatUtils;
import lombok.RequiredArgsConstructor;

@CommandAlias("alert")
@RequiredArgsConstructor
public class AlertCMD extends BaseCommand {
    private final ServerGroupsManager serverGroupsManager;
    private final String permission, noPermissionMessage, wrongUsage, invalidGroup, mustSpecifyMessage, format;

    @Default
    @HelpCommand
    public void onDefault(Player player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }

        player.sendMessage(ChatUtils.colorAndGetComponent(wrongUsage));
    }

    @Default
    public void onDefault(Player player, String group, String[] args) {
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
                .replace("%player%", player.getUsername())
                .replace("%message%", message.toString().trim());

        serverGroupsManager.getServers(group).forEach(server ->
                server.getPlayersConnected().forEach(recipient ->
                        recipient.sendMessage(ChatUtils.colorAndGetComponent(formatted))
                )
        );
    }
}
