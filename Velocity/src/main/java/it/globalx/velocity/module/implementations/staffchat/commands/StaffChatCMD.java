package it.globalx.velocity.module.implementations.staffchat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.velocitypowered.api.proxy.Player;
import it.globalx.proxy.staffchat.StaffChatUtils;
import it.globalx.velocity.module.implementations.staffchat.StaffChatModule;
import it.globalx.velocity.utils.ChatUtils;
import lombok.RequiredArgsConstructor;

@CommandAlias("staffchat|sc")
@RequiredArgsConstructor
public class StaffChatCMD extends BaseCommand {
    private final String permission, noPermissionMessage, staffChatEnabled, staffChatDisabled;
    private final StaffChatModule staffChatModule;

    @Default
    public void onCommand(Player player) {
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
