package it.globalx.bungee.module.implementations.chatfiltering.check.result;

import it.globalx.bungee.module.implementations.chatfiltering.check.result.type.CheckResultType;

public record CheckResult(CheckResultType checkResultType, String newMessage) {

}
