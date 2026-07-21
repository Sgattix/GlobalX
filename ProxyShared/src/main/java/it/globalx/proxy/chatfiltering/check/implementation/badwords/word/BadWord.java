package it.globalx.proxy.chatfiltering.check.implementation.badwords.word;

import it.globalx.proxy.chatfiltering.check.implementation.badwords.word.action.IntentAction;

public record BadWord(String word, IntentAction intentAction, String param) {

}
