package edu.pw.common;

import java.net.URI;
import java.net.http.HttpRequest;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WorkerService extends Remote {

    List<SingleTestResult> measureRequestsProcessingTime(URI workerURI,
                                                         HttpRequest httpRequestToMake,
                                                         int numOfRequests) throws RemoteException;

}
