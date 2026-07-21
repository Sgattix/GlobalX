package it.globalx.bungee.module.implementations.chatfiltering.utils;

import it.globalx.bungee.module.implementations.chatfiltering.ChatFilteringModule;
import it.globalx.bungee.module.implementations.chatfiltering.check.result.CheckResult;

public class FilterUtils {
    private static ChatFilteringModule module;

    public FilterUtils(ChatFilteringModule module) {
        FilterUtils.module = module;
    }

    public CheckResult check(String message) {
        final CheckResult domainCheckResult = module.getDomainCheck().check(message);

        switch (domainCheckResult.checkResultType()) {
            case BLOCKED -> {
                return domainCheckResult;
            }
            case ALLOWED, REPLACED -> {
                return module.getBadWordsCheck().check(domainCheckResult.newMessage());
            }
        }
        return null;
    }

}
