package edu.pw;

import edu.pw.common.SingleTestResult;
import edu.pw.common.requests.SerializableHttpRequest;
import edu.pw.controller.ControllerService;
import edu.pw.parser.ArgsParser;
import edu.pw.parser.ParsedArgs;
import org.apache.commons.cli.ParseException;

import java.net.http.HttpRequest;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class Controller {

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    public static void main(String[] args) {
        try {
            ParsedArgs parsedArgs = ArgsParser.parse(args);

            try (ControllerService controllerService = new ControllerService(parsedArgs.workerURIs())) {
                HttpRequest httpRequest = new SerializableHttpRequest(
                        parsedArgs.serverToTestURI(),
                        "GET",
                        Collections.emptyMap()
                );
                List<SingleTestResult> results = controllerService.performTests(httpRequest, 100);
                System.out.println(results);
            }
        } catch (ParseException e) {
            logger.severe("Error parsing arguments: " + e.getMessage());
            exit(1);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
