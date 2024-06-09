package edu.pw;

import edu.pw.parser.ArgsParser;
import edu.pw.parser.ParsedArgs;
import edu.pw.worker.WorkerServiceImpl;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Worker {

    private static final HelpFormatter helpFormatter = new HelpFormatter();
    private static final String CMD_LINE_SYNTAX = "java -jar worker.jar";

    private static final String WORKER_NAME = "worker";

    public static void main(String[] args) {
        if (ArgsParser.isHelpOptionPresent(args)) {
            helpFormatter.printHelp(CMD_LINE_SYNTAX, ArgsParser.getOptions(), true);
            System.exit(0);
        }

        try {
            ParsedArgs parsedArgs = ArgsParser.parse(args);
            WorkerServiceImpl workerServiceImpl = new WorkerServiceImpl();
            Registry registry = LocateRegistry.createRegistry(parsedArgs.port());
            registry.rebind(WORKER_NAME, workerServiceImpl);
            System.out.println("Worker started");
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            helpFormatter.printHelp(CMD_LINE_SYNTAX, ArgsParser.getOptions(), true);
            System.exit(1);
        } catch (AccessException e) {
            System.err.println("Worker isn't allowed to work on this port");
            System.exit(2);
        } catch (RemoteException e) {
            System.err.println("There is an error creating the registry");
            System.exit(3);
        }
    }
}
