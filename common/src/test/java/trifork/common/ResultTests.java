package trifork.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import trifork.common.core.Result;

@SpringBootApplication
@SpringBootTest
public class ResultTests {
    
    @Test
    void success_WhenInitialized_ReturnsObjectWithoutErrorOrExceptionInformation() {
        Result sut = Result.Success();

        assertTrue(sut.isSuccess());
        assertFalse(sut.isFailure());
        assertFalse(sut.hasException());
        assertEquals("", sut.getErrorMessage());
        assertNull(sut.getException());
    }
    
    @Test
    void fail_WithoutException_ReturnsObjectWithoutExceptionInformation() {
        String errorMessage = "Something went wrong.";
        Result sut = Result.Fail(errorMessage);

        assertTrue(sut.isFailure());
        assertFalse(sut.isSuccess());
        assertEquals(errorMessage, sut.getErrorMessage());
        assertFalse(sut.hasException());
        assertNull(sut.getException());
    }
    
    @Test
    void fail_WithException_ReturnsObjectWithExceptionInformation() {
        String errorMessage = "Something went wrong.";
        Exception exception = new Exception(errorMessage);
        Result sut = Result.Fail(errorMessage, exception);

        assertTrue(sut.isFailure());
        assertFalse(sut.isSuccess());
        assertEquals(errorMessage, sut.getErrorMessage());
        assertTrue(sut.hasException());
        assertEquals(exception, sut.getException());
    }
}
