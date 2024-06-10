package edu.pw.exceptions;

public enum ExitCode {
    WORKER_ACCESS_EXCEPTION(1),
    WORKER_COMMUNICATION_EXCEPTION(2),
    WORKER_NOT_BOUND_EXCEPTION(3),
    INTERRUPTED_COLLECTING_RESULTS_EXCEPTION(4),
    ARGUMENT_PARSE_EXCEPTION(5),
    INVALID_URI_EXCEPTION(6),
    FILE_WRITE_EXCEPTION(7),
    UNHANDLED_EXCEPTION(-1),
    COLLECTING_RESULTS_UNKNOWN_EXCEPTION(-2);

    private final int value;

    ExitCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
