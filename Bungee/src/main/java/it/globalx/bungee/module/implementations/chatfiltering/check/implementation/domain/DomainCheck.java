package it.globalx.bungee.module.implementations.chatfiltering.check.implementation.domain;

import com.google.common.collect.Sets;
import it.globalx.bungee.module.implementations.chatfiltering.check.Check;
import it.globalx.bungee.module.implementations.chatfiltering.check.result.CheckResult;
import it.globalx.bungee.module.implementations.chatfiltering.check.result.type.CheckResultType;

import java.util.Set;

public class DomainCheck extends Check {
    private final static Set<String> BAD_WORDS = Sets.newHashSet(
            ".it",
            "play.",
            "mc.",
            ".me",
            ".us",
            "mine.",
            "craft.",
            "server.",
            "playserver.",
            "games.",
            "game.",
            "playmc.",
            ".uk",
            ".gg",
            "www.",
            ".cc",
            ".org",
            ".ly",
            "http://",
            "https://",
            ".eu",
            ".com",
            ".co.uk",
            ".net",
            ".tk"
    );

    @Override
    public CheckResult check(String message) {
        String[] words = message.split(" ");

        for (String word : words) {
            word = word.replace("(dot)", ".").replace(":", ".");

            for (String badWord : BAD_WORDS) {
                if (word.contains(badWord)) {
                    return new CheckResult(CheckResultType.BLOCKED, "");
                }
            }
        }

        return new CheckResult(CheckResultType.ALLOWED, message);
    }
}
