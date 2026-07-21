package it.globalx.bungee.module.implementations.playerformat.format.causes;

import it.globalx.proxy.playerformat.FormatCausesType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public record FormatCause(FormatCausesType type, String param) {

    public boolean isSatisfied(ProxiedPlayer player) {
        switch (type) {
            case GROUP -> {
                LuckPerms luckPerms = LuckPermsProvider.get();
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());

                if (user == null) {
                    return false;
                }

                return user.getInheritedGroups(QueryOptions.defaultContextualOptions()).stream()
                        .anyMatch(group -> group.getName().equalsIgnoreCase(param));
            }
            case PERMISSION -> {
                return player.hasPermission(param);
            }
        }
        return false;
    }
}
