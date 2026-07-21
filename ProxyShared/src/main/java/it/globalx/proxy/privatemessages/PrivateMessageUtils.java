package it.globalx.proxy.privatemessages;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.UUID;

@UtilityClass
public class PrivateMessageUtils {

    private final HashMap<UUID, UUID> lastMessageCache = new HashMap<>();

    public void setLastMessage(UUID sender, UUID receiver) {
        lastMessageCache.put(sender, receiver);
    }

    public UUID getLastMessage(UUID sender) {
        return lastMessageCache.get(sender);
    }

    public void removeLastMessage(UUID sender) {
        lastMessageCache.remove(sender);
    }


    public static boolean hasLastMessage(UUID uniqueId) {
        return lastMessageCache.containsKey(uniqueId);
    }
}
