package edu.pw.exceptions.remote;

import edu.pw.exceptions.ExitCode;

import java.net.URI;
import java.rmi.NotBoundException;

public class WorkerNotBoundException extends WorkerBaseException {

    public WorkerNotBoundException(NotBoundException notBoundException, URI workerURI) {
        super("Worker identified by " + workerURI + " is not bound",
                notBoundException,
                ExitCode.WORKER_NOT_BOUND_EXCEPTION);
    }

}
