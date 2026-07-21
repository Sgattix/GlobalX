package it.globalx.bungee.module.implementations.playerformat;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.playerformat.format.PlayerFormat;
import it.globalx.bungee.module.implementations.playerformat.format.causes.FormatCause;
import it.globalx.bungee.module.implementations.playerformat.format.causes.type.FormatCausesType;
import it.globalx.bungee.module.implementations.playerformat.listener.FormatListener;
import it.globalx.bungee.module.implementations.playerformat.manager.FormatManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class PlayerFormatModule extends Module {
    private FormatManager formatManager;

    public PlayerFormatModule() {
        super("playerformat", "Allow you to use custom player formatting", "PlayerFormat");
    }

    @Override
    protected void onEnable(Section section) {
        this.formatManager = new FormatManager();

        for (Object nameObject : section.getSection("Format").getKeys()) {
            String name = (String) nameObject;

            String chatFormat = section.getString("Format." + name + ".Chat");
            String tabFormat = section.getString("Format." + name + ".Tab");
            List<String> formatCauses = section.getStringList("Format." + name + ".Causes");

            Set<FormatCause> causes = new HashSet<>();

            for (String cause : formatCauses) {
                String[] split = cause.split(" ");

                if (split.length != 2) {
                    throw new IllegalArgumentException("Cause must be in the format '[type] param'");
                }

                String type = split[0].replace("[", "").replace("]", "");

                FormatCausesType formatCausesType = FormatCausesType.valueOf(type.toUpperCase());

                causes.add(new FormatCause(formatCausesType, split[1]));
            }

            formatManager.addFormat(name, new PlayerFormat(chatFormat, tabFormat, causes));
        }

        ProxyServer.getInstance().getPluginManager().registerListener(plugin(), new FormatListener(this));
    }

    @Override
    public void onDisable() {

    }
}
