package edu.pw.exceptions.remote;

import edu.pw.exceptions.ExitCode;

import java.net.URI;
import java.rmi.AccessException;

public class WorkerAccessException extends WorkerBaseException {

    public WorkerAccessException(AccessException accessException, URI workerURI) {
        super("You are not allowed to access the worker with URI " + workerURI,
                accessException,
                ExitCode.WORKER_ACCESS_EXCEPTION);
    }
}
