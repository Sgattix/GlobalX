package it.globalx.proxy.staffchat;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class StaffChatUtils {

    private final Set<UUID> staffsWithChatEnabled = new HashSet<>();

    public void enableStaffChat(UUID staff) {
        staffsWithChatEnabled.add(staff);
    }

    public void disableStaffChat(UUID staff) {
        staffsWithChatEnabled.remove(staff);
    }

    public boolean isStaffChatEnabled(UUID staff) {
        return staffsWithChatEnabled.contains(staff);
    }

    public Set<UUID> getStaffsWithChatEnabled() {
        return staffsWithChatEnabled;
    }

}
