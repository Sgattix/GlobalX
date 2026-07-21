package it.globalx.bungee.module.implementations.staffchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import it.globalx.bungee.module.implementations.staffchat.StaffChatModule;
import it.globalx.bungee.module.implementations.staffchat.utils.StaffChatUtils;
import it.globalx.bungee.utils.ChatUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@CommandAlias("staffchat|sc")
@RequiredArgsConstructor
public class StaffChatCMD extends BaseCommand {
    private final String permission, noPermissionMessage, staffChatEnabled, staffChatDisabled;
    private final StaffChatModule staffChatModule;

    @Default
    public void onCommand(ProxiedPlayer player) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatUtils.colorAndGetComponent(noPermissionMessage));
            return;
        }
        if (StaffChatUtils.isStaffChatEnabled(player.getUniqueId())) {
            StaffChatUtils.disableStaffChat(player.getUniqueId());
            player.sendMessage(ChatUtils.colorAndGetComponent(staffChatDisabled));
            return;
        }
        StaffChatUtils.enableStaffChat(player.getUniqueId());
        player.sendMessage(ChatUtils.colorAndGetComponent(staffChatEnabled));
    }
}
