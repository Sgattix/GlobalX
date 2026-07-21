package it.globalx.bungee.module.implementations.privatemessages.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import it.globalx.bungee.module.implementations.privatemessages.PrivateMessagesModule;
import it.globalx.proxy.privatemessages.PrivateMessageUtils;
import it.globalx.bungee.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("msg")
@RequiredArgsConstructor
public class MsgCMD extends BaseCommand {
    private final PrivateMessagesModule privateMessagesModule;
    private final String permission, noPermissionMessage, wrongUsage, targetIsNull, cannotSendMessageToYourself,
            mustSpecifyMessage, senderFormat, receiverFormat;

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
    @CommandCompletion("@players")
    public void onDefault(ProxiedPlayer player, String targetName, String[] args) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }

        ProxiedPlayer target = privateMessagesModule.plugin().getProxyServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(ChatUtils.colorAndGetComponent(targetIsNull));
            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.colorAndGetComponent(cannotSendMessageToYourself));
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

        player.sendMessage(
                ChatUtils.colorAndGetComponent(
                        senderFormat
                                .replace("%target%", target.getName())
                                .replace("%message%", message.toString())
                )
        );

        target.sendMessage(
                ChatUtils.colorAndGetComponent(
                        receiverFormat
                                .replace("%sender%", player.getName())
                                .replace("%message%", message.toString())
                )
        );

        PrivateMessageUtils.setLastMessage(player.getUniqueId(), target.getUniqueId());
        PrivateMessageUtils.setLastMessage(target.getUniqueId(), player.getUniqueId());
    }

}
