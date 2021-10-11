package trifork.messagereceiver.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import trifork.common.core.Result;
import trifork.messagereceiver.model.TriforkMessage;

@Component
public class MessageRepository implements IMessageRepository {
    private static final Logger Log = LoggerFactory.getLogger(MessageRepository.class);

    private final TriforkDataContext _dataContext;

    public MessageRepository(TriforkDataContext dataContext) {
        super();

        _dataContext = dataContext;
    }

    public Result add(TriforkMessage message) {
        String sql = "INSERT INTO Messages(content,timestamp) VALUES(?,?);";
        
        Object[] params = new Object[] {
            message.getContent(),
            message.getTimestamp().getEpochSecond()
        };
        
        Result result = _dataContext.executeSql(sql, params);
        
        if (result.isSuccess()) {
            Log.info("Message successfully added to database.");
            return result;
        }
        
        // TODO: Decide on retry flow
        // Discuss possibilities at job interview
        Log.error("Message was not added to database.");
        return result;
    }

}
