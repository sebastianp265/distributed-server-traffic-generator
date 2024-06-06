package edu.pw.exceptions.remote;

import edu.pw.exceptions.ExitCode;

import java.net.URI;
import java.rmi.RemoteException;

public class WorkerCommunicationException extends WorkerBaseException {

    public WorkerCommunicationException(RemoteException remoteException, URI workerURI) {
        super("Remote communication with registry of worker with URI " + workerURI + " failed",
                remoteException,
                ExitCode.WORKER_COMMUNICATION_EXCEPTION);
    }

}
