package it.globalx.velocity.module.implementations.alert.manager;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.route.Route;
import it.globalx.velocity.GlobalXVelocity;

import java.util.*;
import java.util.regex.Pattern;

public class ServerGroupsManager {
    private static final String EXCLUDE_MARKER = "***";

    private final Map<String, List<RegisteredServer>> groups = new HashMap<>();

    public ServerGroupsManager(Section section) {
        Collection<RegisteredServer> allServers = GlobalXVelocity.getInstance().getProxyServer().getAllServers();

        for (Object key : section.getKeys()) {
            String id = (String) key;

            List<String> groupServers = section.getStringList(Route.fromString(id));

            List<RegisteredServer> resolved;
            if (!groupServers.isEmpty() && groupServers.get(0).equals(EXCLUDE_MARKER)) {
                List<Pattern> excludePatterns = compilePatterns(groupServers.subList(1, groupServers.size()));

                resolved = allServers.stream()
                        .filter(server -> excludePatterns.stream().noneMatch(pattern -> pattern.matcher(server.getServerInfo().getName()).matches()))
                        .toList();
            } else {
                List<Pattern> includePatterns = compilePatterns(groupServers);

                resolved = allServers.stream()
                        .filter(server -> includePatterns.stream().anyMatch(pattern -> pattern.matcher(server.getServerInfo().getName()).matches()))
                        .toList();
            }

            groups.put(id, resolved);
        }
    }

    public List<RegisteredServer> getServers(String key) {
        return groups.getOrDefault(key, new ArrayList<>());
    }

    public boolean hasGroup(String key) {
        return groups.containsKey(key);
    }

    private static List<Pattern> compilePatterns(List<String> patterns) {
        return patterns.stream().map(ServerGroupsManager::toPattern).toList();
    }

    private static Pattern toPattern(String glob) {
        StringBuilder regex = new StringBuilder("^");

        for (String part : glob.split("(?=\\*)|(?<=\\*)")) {
            regex.append(part.equals("*") ? ".*" : Pattern.quote(part));
        }

        regex.append("$");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }
}
