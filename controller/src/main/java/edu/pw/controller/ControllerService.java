package edu.pw.controller;

import edu.pw.common.SingleTestResult;
import edu.pw.common.WorkerService;
import edu.pw.exceptions.remote.WorkerAccessException;
import edu.pw.exceptions.remote.WorkerCommunicationException;
import edu.pw.exceptions.remote.WorkerNotBoundException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ControllerService implements AutoCloseable {
    private static final String WORKER_NAME = "worker";

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final CompletionService<List<SingleTestResult>> completionService
            = new ExecutorCompletionService<>(threadPool);

    private final List<URI> workerURIs;
    private final List<WorkerService> workers;

    public ControllerService(List<URI> workerURIs)
            throws WorkerNotBoundException, WorkerAccessException, WorkerCommunicationException {

        this.workerURIs = workerURIs;

        workers = new ArrayList<>(workerURIs.size());

        for (URI workerURI : workerURIs) {
            try {
                Registry registry = LocateRegistry.getRegistry(workerURI.getHost(), workerURI.getPort());
                WorkerService worker = (WorkerService) registry.lookup(WORKER_NAME);

                workers.add(worker);
            } catch (NotBoundException e) {
                throw new WorkerNotBoundException(e, workerURI);
            } catch (AccessException e) {
                throw new WorkerAccessException(e, workerURI);
            } catch (RemoteException e) {
                throw new WorkerCommunicationException(e, workerURI);
            }
        }

    }

    public List<SingleTestResult> performTests(HttpRequest requestToMake, int numOfRequests)
            throws InterruptedException, ExecutionException {
        sendRequests(requestToMake, numOfRequests);
        List<SingleTestResult> results = collectResults();

        results.sort(Comparator.comparing(SingleTestResult::date).thenComparing(SingleTestResult::time));
        return results;
    }

    @Override
    public void close() {
        threadPool.close();
    }

    private List<SingleTestResult> collectResults() throws InterruptedException, ExecutionException {
        List<SingleTestResult> testResults = new LinkedList<>();
        for (int i = 0; i < workers.size(); i++) {
            Future<List<SingleTestResult>> futurePartialTestResults = completionService.take();
            List<SingleTestResult> partialTestResults = futurePartialTestResults.get();
            testResults.addAll(partialTestResults);
        }

        return testResults;
    }

    private void sendRequests(HttpRequest requestToMake,
                              int numOfRequests) {
        for (int i = 0; i < workers.size(); i++) {
            WorkerService worker = workers.get(i);
            URI workerURI = workerURIs.get(i);

            completionService.submit(() ->
                    worker.measureRequestsProcessingTime(
                            workerURI,
                            requestToMake,
                            numOfRequests
                    )
            );
        }
    }

}