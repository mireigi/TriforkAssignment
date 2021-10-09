package trifork.messagereceiver.data;

import trifork.common.core.Result;
import trifork.messagereceiver.model.TriforkMessage;

public interface IMessageRepository {
    public Result add(TriforkMessage message);
}
