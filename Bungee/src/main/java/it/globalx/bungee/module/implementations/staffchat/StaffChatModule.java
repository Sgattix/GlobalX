package it.globalx.bungee.module.implementations.staffchat;

import co.aikar.commands.BungeeCommandManager;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.staffchat.commands.StaffChatCMD;
import it.globalx.bungee.module.implementations.staffchat.listener.StaffChatListener;
import net.md_5.bungee.api.ProxyServer;

public class StaffChatModule extends Module {
    public StaffChatModule() {
        super("staffchat", "Allow the staffs to have a private chat", "StaffChat");
    }

    @Override
    protected void onEnable(Section section) {
        BungeeCommandManager bungeeCommandManager = new BungeeCommandManager(plugin());

        bungeeCommandManager.registerCommand(new StaffChatCMD(
                section.getString("Permission"),
                section.getString("NoPermission"),
                section.getString("StaffChatEnabled"),
                section.getString("StaffChatDisabled"),
                this
        ));

        ProxyServer.getInstance().getPluginManager().registerListener(plugin(), new StaffChatListener(
                this,
                section.getString("StaffChatFormat"),
                section.getString("Permission")
        ));
    }

    @Override
    public void onDisable() {

    }
}
