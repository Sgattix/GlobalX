package it.globalx.proxy.chatfiltering.check.result;

import it.globalx.proxy.chatfiltering.check.result.type.CheckResultType;

public record CheckResult(CheckResultType checkResultType, String newMessage) {

}
