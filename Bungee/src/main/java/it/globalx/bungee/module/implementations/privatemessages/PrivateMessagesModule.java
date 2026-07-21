package it.globalx.bungee.module.implementations.privatemessages;

import co.aikar.commands.BungeeCommandManager;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.privatemessages.commands.MsgCMD;
import it.globalx.bungee.module.implementations.privatemessages.commands.ReplyCMD;

public class PrivateMessagesModule extends Module {
    public PrivateMessagesModule() {
        super("privatemessages", "Allow users to comunicate thru a private chat", "PrivateMessages");
    }

    @Override
    protected void onEnable(Section section) {
        BungeeCommandManager bungeeCommandManager = new BungeeCommandManager(plugin());

        bungeeCommandManager.registerCommand(new MsgCMD(
                this,
                section.getString("Msg.Permission"),
                section.getString("Msg.NoPermissionMessage"),
                section.getString("Msg.WrongUsage"),
                section.getString("Msg.TargetIsNull"),
                section.getString("Msg.CannotSendMessageToYourself"),
                section.getString("Msg.MustSpecifyMessage"),
                section.getString("Msg.SenderFormat"),
                section.getString("Msg.TargetFormat")
        ));
        if (section.getBoolean("Reply.Enabled", true)) {
            bungeeCommandManager.registerCommand(new ReplyCMD(
                    this,
                    section.getString("Reply.Permission"),
                    section.getString("Reply.NoPermissionMessage"),
                    section.getString("Reply.MustSpecifyMessage"),
                    section.getString("Reply.NoOneToReplyTo"),
                    section.getString("Reply.SenderFormat"),
                    section.getString("Reply.TargetFormat")
            ));
        }
    }

    @Override
    public void onDisable() {

    }
}
