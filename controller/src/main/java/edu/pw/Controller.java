package edu.pw;

import edu.pw.common.SingleTestResult;
import edu.pw.common.requests.SerializableHttpRequest;
import edu.pw.controller.ControllerService;
import edu.pw.exceptions.ExitCode;
import edu.pw.exceptions.remote.WorkerBaseException;
import edu.pw.parser.ArgsParser;
import edu.pw.parser.ParsedArgs;
import org.apache.commons.cli.ParseException;

import java.net.http.HttpRequest;
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
        } catch (WorkerBaseException e) {
            System.err.println(e.getMessage());
            exit(e.exitCode.getValue());

        } catch (ExecutionException e) {
            System.err.println("Encountered an exception while waiting for results from workers");
            exit(ExitCode.COLLECTING_RESULTS_UNKNOWN_EXCEPTION.getValue());

        } catch (InterruptedException e) {
            System.err.println("Collecting results has been interrupted");
            exit(ExitCode.INTERRUPTED_COLLECTING_RESULTS_EXCEPTION.getValue());

        } catch (ParseException e) {

            System.err.println(e.getMessage());
            exit(ExitCode.ARGUMENT_PARSE_EXCEPTION.getValue());
        }
    }
}
