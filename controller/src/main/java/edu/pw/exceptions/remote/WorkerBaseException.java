package edu.pw.exceptions.remote;

import edu.pw.exceptions.ExitCode;

public abstract class WorkerBaseException extends Exception {

    public final ExitCode exitCode;

    protected WorkerBaseException(String message, Exception originalException, ExitCode exitCode) {
        super(message, originalException);
        this.exitCode = exitCode;
    }

}
