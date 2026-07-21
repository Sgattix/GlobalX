package it.globalx.bungee;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.alert.AlertModule;
import it.globalx.bungee.module.implementations.chatfiltering.ChatFilteringModule;
import it.globalx.bungee.module.implementations.playerformat.PlayerFormatModule;
import it.globalx.bungee.module.implementations.privatemessages.PrivateMessagesModule;
import it.globalx.bungee.module.implementations.scheduledmessages.ScheduledMessagesModule;
import it.globalx.bungee.module.implementations.staffchat.StaffChatModule;
import it.globalx.bungee.utils.UpdateChecker;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Getter
public class GlobalXBungee extends Plugin {

    @Getter
    private static GlobalXBungee instance;
    private YamlDocument config;
    private final HashMap<String, Module> modules = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"),
                    Objects.requireNonNull(getResourceAsStream("config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version"))
                            .setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                            .build()
            );

            config.update();
            config.save();
        } catch (Exception e) {
            getLogger().severe("Failed to load config.yml, disabling GlobalX: " + e.getMessage());
            return;
        }

        List.of(
                new ScheduledMessagesModule(),
                new PrivateMessagesModule(),
                new StaffChatModule(),
                new ChatFilteringModule(),
                new PlayerFormatModule(),
                new AlertModule()
        ).forEach(module -> {
            module.enable(config.getSection(Route.from(module.getConfigPath())));

            modules.put(module.getName(), module);
        });

        new UpdateChecker(this, getResourceId()).getVersion(version -> {
            if (!version.equals(getVersion())) {
                getLogger().info("[GlobalX] [UpdateChecker] There's a new update available: " + version + ", you're currently on " + getVersion());
            }
        });
    }

    @Override
    public void onDisable() {
        modules.values().forEach(Module::onDisable);
    }

    private String getVersion() {
        return "1.0.0";
    }

    public static int getResourceId() {
        return 102941;
    }

    public ProxyServer getProxyServer() {
        return getProxy();
    }
}
