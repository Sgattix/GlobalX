package it.globalx.bungee.module.implementations.playerformat.format;

import it.globalx.bungee.module.implementations.playerformat.format.causes.FormatCause;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Set;

public record PlayerFormat(String chatFormat, String tabFormat, Set<FormatCause> formatCauses) {

    public boolean isValid(ProxiedPlayer player) {
        for (FormatCause formatCause : formatCauses) {
            if (!formatCause.isSatisfied(player)) {
                return false;
            }
        }
        return true;
    }

}
