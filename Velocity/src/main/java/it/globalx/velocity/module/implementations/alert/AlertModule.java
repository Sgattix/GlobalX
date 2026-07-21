package it.globalx.velocity.module.implementations.alert;

import co.aikar.commands.VelocityCommandManager;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.velocity.module.Module;
import it.globalx.velocity.module.implementations.alert.commands.AlertCMD;
import it.globalx.velocity.module.implementations.alert.manager.ServerGroupsManager;

public class AlertModule extends Module {
    public AlertModule() {
        super("alert", "Let certain users make announcements around the network", "Alerts");
    }

    @Override
    protected void onEnable(Section section) {
        VelocityCommandManager velocityCommandManager = new VelocityCommandManager(plugin().getProxyServer(), plugin());
        ServerGroupsManager serverGroupsManager = new ServerGroupsManager(section.getSection("Groups"));

        velocityCommandManager.registerCommand(new AlertCMD(
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
