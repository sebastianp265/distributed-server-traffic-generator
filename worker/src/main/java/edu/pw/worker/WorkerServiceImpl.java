package edu.pw.worker;

import edu.pw.Worker;
import edu.pw.common.SingleTestResult;
import edu.pw.common.WorkerService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class WorkerServiceImpl extends UnicastRemoteObject implements WorkerService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final Logger logger = Logger.getLogger(WorkerServiceImpl.class.getName());

    public WorkerServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<SingleTestResult> measureRequestsProcessingTime(URI workerURI,
                                                                HttpRequest httpRequestToMake,
                                                                int numOfRequests) throws RemoteException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        
        List<SingleTestResult> results = new LinkedList<>();

        try (HttpClient client = HttpClient.newHttpClient()) {
            for (int i = 0; i < numOfRequests; i++) {
                logger.log(Level.FINE, "Sending request number {0}", i);

                Date dateAtTestStart = new Date();

                long start = System.currentTimeMillis();
                client.send(httpRequestToMake, HttpResponse.BodyHandlers.discarding());
                long end = System.currentTimeMillis();

                long requestProcessingTime = end - start;
                String[] dateParts = dateFormat.format(dateAtTestStart).split(" ");

                SingleTestResult testResult = new SingleTestResult(dateParts[0], dateParts[1], workerURI.toString(), Long.toString(requestProcessingTime));
                results.add(testResult);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RemoteException(e.toString());
        }

        return results;
    }
}