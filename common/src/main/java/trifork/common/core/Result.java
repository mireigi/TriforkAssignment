package trifork.common.core;

public class Result {
    private final Boolean _success;

    private final String _errorMessage;
    public String getErrorMessage() { return _errorMessage; }

    private final Exception _exception;
    public Exception getException() { return _exception; }

    protected Result(Boolean success, String errorMessage, Exception exception) {
        super();

        _success = success;
        _errorMessage = errorMessage;
        _exception = exception;
    }

    public Boolean isSuccess() { return _success; }
    public Boolean isFailure() { return !_success; }
    public Boolean hasException() { return _exception != null; }

    public static Result Success() {
        return new Result(true, "", null);
    }

    public static Result Fail(String errorMessage) {
        return new Result(false, errorMessage, null);
    }

    public static Result Fail(String errorMessage, Exception exception) {
        return new Result(false, errorMessage, exception);
    }
}