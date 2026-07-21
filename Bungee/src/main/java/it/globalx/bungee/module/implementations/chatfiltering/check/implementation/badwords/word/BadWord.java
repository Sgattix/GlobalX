package it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.word;

import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.word.action.IntentAction;
import org.jspecify.annotations.Nullable;

public record BadWord(String word, IntentAction intentAction, @Nullable String param) {

}
