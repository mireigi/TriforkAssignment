package trifork.common;

import trifork.common.core.Result;

public interface IMessageQueueGateway {
    Result sendMessage(String exchange, String routingKey, String message);
}
