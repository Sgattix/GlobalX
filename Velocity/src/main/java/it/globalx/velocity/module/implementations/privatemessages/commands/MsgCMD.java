package it.globalx.velocity.module.implementations.privatemessages.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.HelpCommand;
import com.velocitypowered.api.proxy.Player;
import it.globalx.velocity.module.implementations.privatemessages.PrivateMessagesModule;
import it.globalx.velocity.module.implementations.privatemessages.utils.PrivateMessageUtils;
import it.globalx.velocity.utils.ChatUtils;
import lombok.RequiredArgsConstructor;

@CommandAlias("msg")
@RequiredArgsConstructor
public class MsgCMD extends BaseCommand {
    private final PrivateMessagesModule privateMessagesModule;
    private final String permission, noPermissionMessage, wrongUsage, targetIsNull, cannotSendMessageToYourself,
            mustSpecifyMessage, senderFormat, receiverFormat;

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
    @CommandCompletion("@players")
    public void onDefault(Player player, String targetName, String[] args) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }

        Player target = privateMessagesModule.plugin().getProxyServer().getPlayer(targetName).orElse(null);

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
                                .replace("%target%", target.getUsername())
                                .replace("%message%", message.toString())
                )
        );

        target.sendMessage(
                ChatUtils.colorAndGetComponent(
                        receiverFormat
                                .replace("%sender%", player.getUsername())
                                .replace("%message%", message.toString())
                )
        );

        PrivateMessageUtils.setLastMessage(player.getUniqueId(), target.getUniqueId());
        PrivateMessageUtils.setLastMessage(target.getUniqueId(), player.getUniqueId());
    }

}
