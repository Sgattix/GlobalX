package it.globalx.bungee.module.implementations.alert;

import co.aikar.commands.BungeeCommandManager;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.alert.commands.AlertCMD;
import it.globalx.bungee.module.implementations.alert.manager.ServerGroupsManager;

public class AlertModule extends Module {
    public AlertModule() {
        super("alert", "Let certain users make announcements around the network", "Alerts");
    }

    @Override
    protected void onEnable(Section section) {
        BungeeCommandManager bungeeCommandManager = new BungeeCommandManager(plugin());
        ServerGroupsManager serverGroupsManager = new ServerGroupsManager(section.getSection("Groups"));

        bungeeCommandManager.registerCommand(new AlertCMD(
                serverGroupsManager,
                section.getString("Permission"),
                section.getString("NoPermission"),
                section.getString("WrongUsage"),
                section.getString("InvalidGroup"),
                section.getString("MustSpecifyMessage"),
                section.getString("Format")
        ));
    }

    @Override
    public void onDisable() {

    }
}
