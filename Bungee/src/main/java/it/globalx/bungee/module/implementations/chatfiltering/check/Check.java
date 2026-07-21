package it.globalx.bungee.module.implementations.chatfiltering.check;

import it.globalx.bungee.module.implementations.chatfiltering.check.result.CheckResult;

public abstract class Check {

    public abstract CheckResult check(String message);

}
