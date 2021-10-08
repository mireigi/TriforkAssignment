package trifork.messagereceiver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.common.IMessageQueueGateway;
import trifork.common.core.Result;
import trifork.messagereceiver.application.MessageActionEnum;
import trifork.messagereceiver.application.MessagingService;
import trifork.messagereceiver.model.TriforkMessage;

@SpringBootTest
public class MessagingServiceTests {
    private static final TriforkMessage DISCARDABLE_MESSAGE = TriforkMessage.create("Lorem Ipsum", Instant.parse("2000-10-08T23:00:01.000Z"));
    private static final TriforkMessage REQUEUEABLE_MESSAGE = TriforkMessage.create("Lorem Ipsum", Instant.parse("2021-10-08T23:00:01.000Z"));
    private static final TriforkMessage PERSISTABLE_MESSAGE = TriforkMessage.create("Lorem Ipsum", Instant.parse("2021-10-08T23:00:02.000Z"));

    @Test
    public void requeueMessage_WhenGatewaySucceeds_ReturnsSuccess() {
        IMessageQueueGateway gateway = Mockito.mock(IMessageQueueGateway.class);

        Mockito.when(gateway.sendMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Result.Success());
        
        MessagingService sut = new MessagingService(gateway);
        Result result = sut.requeueMessage(REQUEUEABLE_MESSAGE, "exchange", "routingKey");

        assertTrue(result.isSuccess());
    }

    @Test
    public void requeueMessage_WhenGatewayFails_ReturnsFailure() {
        IMessageQueueGateway gateway = Mockito.mock(IMessageQueueGateway.class);
        String errorMessage = "Test Error Message";

        Mockito.when(gateway.sendMessage(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Result.Fail(errorMessage));
        
        MessagingService sut = new MessagingService(gateway);
        Result result = sut.requeueMessage(REQUEUEABLE_MESSAGE, "exchange", "routingKey");

        assertTrue(result.isFailure());
        assertEquals(errorMessage, result.getErrorMessage());
    }

    @Test
    public void requeueMessage_WhenNotRequeueable_ReturnsFailure() {
        IMessageQueueGateway gateway = Mockito.mock(IMessageQueueGateway.class);

        MessagingService sut = new MessagingService(gateway);
        Result result = sut.requeueMessage(DISCARDABLE_MESSAGE, "exchange", "routingKey");

        assertTrue(result.isFailure());
    }

    @Test
    public void persistMessage_WhenNotPersistable_ReturnsFailure() {
        IMessageQueueGateway gateway = Mockito.mock(IMessageQueueGateway.class);

        MessagingService sut = new MessagingService(gateway);
        Result result = sut.persistMessage(DISCARDABLE_MESSAGE);

        assertTrue(result.isFailure());
    }

    @ParameterizedTest
    @MethodSource("provideInputsForDetermineActionTest")
    public void determineAction_ShouldReturnCorrectAction(TriforkMessage message, MessageActionEnum expectedAction) {
        IMessageQueueGateway gateway = Mockito.mock(IMessageQueueGateway.class);

        MessagingService sut = new MessagingService(gateway);
        MessageActionEnum action = sut.determineAction(message);

        assertEquals(expectedAction, action);
    }

    private static final Stream<Arguments> provideInputsForDetermineActionTest() {
        return Stream.of(
            Arguments.of(DISCARDABLE_MESSAGE, MessageActionEnum.Discard),
            Arguments.of(REQUEUEABLE_MESSAGE, MessageActionEnum.Requeue),
            Arguments.of(PERSISTABLE_MESSAGE, MessageActionEnum.Persist)
        );
    }
}
