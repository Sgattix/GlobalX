package it.globalx.velocity.utils;

import dev.dejvokep.boostedyaml.route.Route;
import it.globalx.velocity.GlobalXVelocity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatUtils {

    private static String prefix() {
        String prefix = GlobalXVelocity.getInstance().getConfig().getString(Route.from("prefix"));
        return prefix == null ? "" : prefix;
    }

    public static Component colorAndGetComponent(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(
                message
                        .replace("%prefix%", prefix())
        );
    }

    public static List<Component> colorAndGetComponents(List<String> stringList) {
        List<Component> components = new ArrayList<>();
        for (String message : stringList) {
            components.add(colorAndGetComponent(message));
        }
        return components;
    }

    public static Set<Component> colorAndGetString(Set<String> strings) {
        Set<Component> newStrings = new HashSet<>();

        for (String string : strings) {
            newStrings.add(colorAndGetComponent(string));
        }

        return newStrings;
    }

    public static List<String> colorAndGetString(List<String> stringList) {
        List<String> strings = new ArrayList<>();
        for (String message : stringList) {
            strings.add(LegacyComponentSerializer.legacyAmpersand().serialize(colorAndGetComponent(message)));
        }
        return strings;
    }

    public static String extractPlainText(Component component) {
        PlainTextComponentSerializer plainText = PlainTextComponentSerializer.plainText();
        return plainText.serialize(component);
    }

}
