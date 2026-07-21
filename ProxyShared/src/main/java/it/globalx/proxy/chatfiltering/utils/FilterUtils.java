package it.globalx.proxy.chatfiltering.utils;

import it.globalx.proxy.chatfiltering.check.implementation.badwords.BadWordsCheck;
import it.globalx.proxy.chatfiltering.check.implementation.domain.DomainCheck;
import it.globalx.proxy.chatfiltering.check.result.CheckResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FilterUtils {
    private final BadWordsCheck badWordsCheck;
    private final DomainCheck domainCheck;

    public CheckResult check(String message) {
        final CheckResult domainCheckResult = domainCheck.check(message);

        switch (domainCheckResult.checkResultType()) {
            case BLOCKED -> {
                return domainCheckResult;
            }
            case ALLOWED, REPLACED -> {
                return badWordsCheck.check(domainCheckResult.newMessage());
            }
        }
        return null;
    }

}
