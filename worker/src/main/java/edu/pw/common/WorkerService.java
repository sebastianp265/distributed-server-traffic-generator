package edu.pw.common;

import edu.pw.common.requests.SerializableHttpRequest;

import java.net.URI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WorkerService extends Remote {

    List<SingleTestResult> measureRequestsProcessingTime(URI workerURI,
                                                         SerializableHttpRequest httpRequest,
                                                         int numOfRequests) throws RemoteException;

}
