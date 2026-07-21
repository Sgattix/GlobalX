package it.globalx.velocity;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import io.github.retrooper.packetevents.velocity.factory.VelocityPacketEventsBuilder;
import it.globalx.velocity.module.Module;
import it.globalx.velocity.module.implementations.alert.AlertModule;
import it.globalx.velocity.module.implementations.chatfiltering.ChatFilteringModule;
import it.globalx.velocity.module.implementations.playerformat.PlayerFormatModule;
import it.globalx.velocity.module.implementations.privatemessages.PrivateMessagesModule;
import it.globalx.velocity.module.implementations.scheduledmessages.ScheduledMessagesModule;
import it.globalx.velocity.module.implementations.staffchat.StaffChatModule;
import it.globalx.velocity.utils.UpdateChecker;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Plugin(
        id = "velocity",
        name = "Velocity",
        version = "1.0.0"
)
@Getter
public class GlobalXVelocity {

    @Getter
    public static PacketEventsAPI<?> PE;

    @Getter
    private static GlobalXVelocity instance;
    private final ProxyServer proxyServer;
    private final Logger logger;
    private YamlDocument config;
    private final Path dataDirectory;
    private final HashMap<String, Module> modules = new HashMap<>();

    @Inject
    public GlobalXVelocity(ProxyServer proxyServer, Logger logger, @DataDirectory Path path) {
        instance = this;
        this.proxyServer = proxyServer;
        this.logger = logger;
        this.dataDirectory = path;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        PacketEvents.setAPI(PE = VelocityPacketEventsBuilder.build(proxyServer, proxyServer.getPluginManager().ensurePluginContainer(this), logger, dataDirectory));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(true);
        PacketEvents.getAPI().load();

        try {
            config = YamlDocument.create(new File(dataDirectory.toFile(), "config.yml"),
                    Objects.requireNonNull(getClass().getResourceAsStream("/config.yml")),
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
            logger.error("Failed to load config.yml, disabling GlobalX", e);
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
                getLogger().info("&7[GlobalX] [UpdateChecker] There's a new update available: &b" + version + "&7, you're currently on &b" + getVersion());
            }
        });

        PE.init();
    }

    private String getVersion() {
        return "1.0.0";
    }

    public static int getResourceId() {
        return 102941;
    }
}
