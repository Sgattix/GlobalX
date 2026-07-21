package it.globalx.proxy.chatfiltering.check;

import it.globalx.proxy.chatfiltering.check.result.CheckResult;

public abstract class Check {

    public abstract CheckResult check(String message);

}
