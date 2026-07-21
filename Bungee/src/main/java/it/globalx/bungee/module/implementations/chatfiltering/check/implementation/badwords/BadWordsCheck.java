package it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords;

import it.globalx.bungee.module.implementations.chatfiltering.check.Check;
import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.word.BadWord;
import it.globalx.bungee.module.implementations.chatfiltering.check.result.CheckResult;
import it.globalx.bungee.module.implementations.chatfiltering.check.result.type.CheckResultType;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class BadWordsCheck extends Check {
    private final Set<BadWord> badWords;

    @Override
    public CheckResult check(String message) {
        for (String word : message.split(" ")) {
            for (BadWord badWord : badWords) {
                if (word.contains(badWord.word())) {
                    switch (badWord.intentAction()) {
                        case REPLACE -> {
                            message = message.replace(badWord.word(), badWord.param());
                        }
                        case BLOCK_MESSAGE -> {
                            return new CheckResult(CheckResultType.BLOCKED, "");
                        }
                    }
                }
            }
        }
        return new CheckResult(CheckResultType.ALLOWED, message);
    }
}
