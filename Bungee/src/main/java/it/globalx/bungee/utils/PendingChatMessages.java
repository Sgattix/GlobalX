package it.globalx.bungee.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Bungee's ChatEvent (unlike Velocity's PlayerChatEvent) can't carry a rewritten message between
// ordered listeners, so ChatFilterListener stashes its replacement here for FormatListener to consume.
@UtilityClass
public class PendingChatMessages {

    private final Map<UUID, String> pending = new ConcurrentHashMap<>();

    public void set(UUID player, String message) {
        pending.put(player, message);
    }

    public Optional<String> consume(UUID player) {
        return Optional.ofNullable(pending.remove(player));
    }

}
