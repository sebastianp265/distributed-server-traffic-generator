package edu.pw.worker;

import edu.pw.common.SingleTestResult;
import edu.pw.common.WorkerService;
import edu.pw.common.requests.SerializableHttpRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class WorkerServiceImpl extends UnicastRemoteObject implements WorkerService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public WorkerServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<SingleTestResult> measureRequestsProcessingTime(URI workerURI,
                                                                SerializableHttpRequest httpRequestToMake,
                                                                int numOfRequests) throws RemoteException {
        System.out.println("Started processing...");
        System.out.println("Will be sending request with:");
        System.out.println("URI = " + httpRequestToMake.uri().toString());
        System.out.println("Request = " + httpRequestToMake.method());
        System.out.println("Headers = " + httpRequestToMake.getHeaderMap());
        System.out.println("Body = " + httpRequestToMake.getBodyString());
        System.out.println("Num of requests = " + numOfRequests);
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        List<SingleTestResult> results = new LinkedList<>();

        try (HttpClient client = HttpClient.newHttpClient()) {
            for (int i = 0; i < numOfRequests; i++) {

                Date dateAtTestStart = new Date();

                long start = System.currentTimeMillis();
                HttpResponse<String> httpResponse = client.send(httpRequestToMake, HttpResponse.BodyHandlers.ofString());
                long end = System.currentTimeMillis();

                long requestProcessingTime = end - start;
                String[] dateParts = dateFormat.format(dateAtTestStart).split(" ");

                SingleTestResult testResult = new SingleTestResult(
                        dateParts[0],
                        dateParts[1],
                        workerURI.toString(),
                        Long.toString(requestProcessingTime),
                        Integer.toString(httpResponse.statusCode()),
                        httpResponse.body()
                );
                results.add(testResult);
            }
        } catch (IOException e) {
            throw new RemoteException("Couldn't send or receive on worker with URI = " + workerURI , e);
        } catch (InterruptedException e) {
            throw new RemoteException("Worker with URI = " + workerURI + " was interrupted", e);
        }

        System.out.println("Finished processing");
        return results;
    }
}