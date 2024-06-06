package edu.pw;

import edu.pw.parser.ArgsParser;
import edu.pw.parser.ParsedArgs;
import edu.pw.worker.WorkerServiceImpl;
import org.apache.commons.cli.ParseException;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class Worker {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());
    private static final String WORKER_NAME = "worker";

    public static void main(String[] args) {
        try {
            ParsedArgs parsedArgs = ArgsParser.parse(args);
            WorkerServiceImpl workerServiceImpl = new WorkerServiceImpl();
            Registry registry = LocateRegistry.createRegistry(parsedArgs.port());
            registry.rebind(WORKER_NAME, workerServiceImpl);
            logger.info("Worker ready.");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
