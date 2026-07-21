package it.globalx.bungee.module.implementations.alert.manager;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.route.Route;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.*;
import java.util.regex.Pattern;

public class ServerGroupsManager {
    // A group whose pattern list starts with "***" selects every server except the ones matched by the remaining patterns
    private static final String EXCLUDE_MARKER = "***";

    private final Map<String, List<ServerInfo>> groups = new HashMap<>();

    public ServerGroupsManager(Section section) {
        Collection<ServerInfo> allServers = ProxyServer.getInstance().getServers().values();

        for (Object key : section.getKeys()) {
            String id = (String) key;

            List<String> groupServers = section.getStringList(Route.fromString(id));

            List<ServerInfo> resolved;
            if (!groupServers.isEmpty() && groupServers.get(0).equals(EXCLUDE_MARKER)) {
                List<Pattern> excludePatterns = compilePatterns(groupServers.subList(1, groupServers.size()));

                resolved = allServers.stream()
                        .filter(server -> excludePatterns.stream().noneMatch(pattern -> pattern.matcher(server.getName()).matches()))
                        .toList();
            } else {
                List<Pattern> includePatterns = compilePatterns(groupServers);

                resolved = allServers.stream()
                        .filter(server -> includePatterns.stream().anyMatch(pattern -> pattern.matcher(server.getName()).matches()))
                        .toList();
            }

            groups.put(id, resolved);
        }
    }

    public List<ServerInfo> getServers(String key) {
        return groups.getOrDefault(key, new ArrayList<>());
    }

    public boolean hasGroup(String key) {
        return groups.containsKey(key);
    }

    private static List<Pattern> compilePatterns(List<String> patterns) {
        return patterns.stream().map(ServerGroupsManager::toPattern).toList();
    }

    // Converts a glob-style name pattern (only "*" is special) into an anchored, case-insensitive regex
    private static Pattern toPattern(String glob) {
        StringBuilder regex = new StringBuilder("^");

        for (String part : glob.split("(?=\\*)|(?<=\\*)")) {
            regex.append(part.equals("*") ? ".*" : Pattern.quote(part));
        }

        regex.append("$");
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }
}
