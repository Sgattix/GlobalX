package it.globalx.bungee.utils;

import dev.dejvokep.boostedyaml.route.Route;
import it.globalx.bungee.GlobalXBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatUtils {

    private static String prefix() {
        String prefix = GlobalXBungee.getInstance().getConfig().getString(Route.from("prefix"));
        return prefix == null ? "" : prefix;
    }

    public static BaseComponent[] colorAndGetComponent(String message) {
        return TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', message.replace("%prefix%", prefix()))
        );
    }

    public static List<BaseComponent[]> colorAndGetComponents(List<String> stringList) {
        List<BaseComponent[]> components = new ArrayList<>();
        for (String message : stringList) {
            components.add(colorAndGetComponent(message));
        }
        return components;
    }

    public static Set<BaseComponent[]> colorAndGetString(Set<String> strings) {
        Set<BaseComponent[]> newStrings = new HashSet<>();

        for (String string : strings) {
            newStrings.add(colorAndGetComponent(string));
        }

        return newStrings;
    }

    public static List<String> colorAndGetString(List<String> stringList) {
        List<String> strings = new ArrayList<>();
        for (String message : stringList) {
            strings.add(TextComponent.toLegacyText(colorAndGetComponent(message)));
        }
        return strings;
    }

    public static String extractPlainText(BaseComponent[] components) {
        return TextComponent.toPlainText(components);
    }

}
