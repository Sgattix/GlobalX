package it.globalx.bungee.module.implementations.chatfiltering;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import it.globalx.bungee.module.Module;
import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.BadWordsCheck;
import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.word.BadWord;
import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.badwords.word.action.IntentAction;
import it.globalx.bungee.module.implementations.chatfiltering.check.implementation.domain.DomainCheck;
import it.globalx.bungee.module.implementations.chatfiltering.listener.ChatFilterListener;
import it.globalx.bungee.module.implementations.chatfiltering.utils.FilterUtils;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatFilteringModule extends Module {
    private BadWordsCheck badWordsCheck;
    private DomainCheck domainCheck;

    public ChatFilteringModule() {
        super("chatfiltering", "Chat filtering with single and multi-word support", "ChatFiltering");
    }

    @Override
    protected void onEnable(Section section) {
        Set<BadWord> badWords = new HashSet<>();

        for (Object wordObject : section.getSection("FilteredWords").getKeys()) {
            String word = (String) wordObject;

            IntentAction intentAction = IntentAction.REPLACE;

            String param = section.getSection("FilteredWords").getString(word);

            if (param.equals("Result.DENIED")) {
                intentAction = IntentAction.BLOCK_MESSAGE;
            }

            badWords.add(new BadWord(word, intentAction, param));
        }

        badWordsCheck = new BadWordsCheck(badWords);
        domainCheck = new DomainCheck();

        FilterUtils filterUtils = new FilterUtils(this);

        ProxyServer.getInstance().getPluginManager().registerListener(plugin(), new ChatFilterListener(filterUtils));
    }

    @Override
    public void onDisable() {

    }
}
