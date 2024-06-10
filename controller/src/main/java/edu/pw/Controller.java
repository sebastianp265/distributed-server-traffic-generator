package edu.pw;

import edu.pw.common.SingleTestResult;
import edu.pw.common.requests.SerializableHttpRequest;
import edu.pw.common.writer.TestResultsWriter;
import edu.pw.controller.ControllerService;
import edu.pw.exceptions.ExitCode;
import edu.pw.exceptions.remote.WorkerBaseException;
import edu.pw.parser.ArgsParser;
import edu.pw.parser.ParsedArgs;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Controller {

    private static final HelpFormatter helpFormatter = new HelpFormatter();
    private static final String CMD_LINE_SYNTAX = "java -jar controller.jar";

    public static void main(String[] args) {
        if (ArgsParser.isHelpOptionPresent(args)) {
            helpFormatter.printHelp(CMD_LINE_SYNTAX, ArgsParser.getOptions(), true);
            System.exit(0);
        }

        try {
            ParsedArgs parsedArgs = ArgsParser.parse(args);

            try (ControllerService controllerService = new ControllerService(parsedArgs.workerURIs())) {
                SerializableHttpRequest httpRequest = getChosenHttpRequest(parsedArgs);
                List<SingleTestResult> results = controllerService.performTests(httpRequest, parsedArgs.numOfRequests());
                TestResultsWriter.writeToFile(results, parsedArgs.outputFile());
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            helpFormatter.printHelp(CMD_LINE_SYNTAX, ArgsParser.getOptions(), true);
            System.exit(ExitCode.ARGUMENT_PARSE_EXCEPTION.getValue());

        } catch (WorkerBaseException e) {
            System.err.println(e.getMessage());
            System.exit(e.exitCode.getValue());

        } catch (ExecutionException e) {
            System.err.println("Encountered an exception while waiting for results from workers, message from worker:\n" +
                    e.getCause().getMessage());
            System.exit(ExitCode.COLLECTING_RESULTS_UNKNOWN_EXCEPTION.getValue());

        } catch (InterruptedException e) {
            System.err.println("Collecting results has been interrupted");
            Thread.currentThread().interrupt();
            System.exit(ExitCode.INTERRUPTED_COLLECTING_RESULTS_EXCEPTION.getValue());

        } catch (IOException e) {
            System.err.println("Couldn't write result to file, further info:");
            System.err.println(e.getMessage());
            System.exit(ExitCode.FILE_WRITE_EXCEPTION.getValue());

        } catch (Exception e) {
            System.err.println("Unhandled exception occurred");
            System.err.println(e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(ExitCode.UNHANDLED_EXCEPTION.getValue());
        }
    }

    private static SerializableHttpRequest getChosenHttpRequest(ParsedArgs parsedArgs) {
        SerializableHttpRequest httpRequest;
        if (parsedArgs.bodyString() != null) {
            httpRequest = new SerializableHttpRequest(
                    parsedArgs.serverToTestURI(),
                    parsedArgs.httpMethod(),
                    parsedArgs.headers(),
                    parsedArgs.bodyString()
            );
        }
        else if(parsedArgs.bodyFileBytes() != null) {
            httpRequest = new SerializableHttpRequest(
                    parsedArgs.serverToTestURI(),
                    parsedArgs.httpMethod(),
                    parsedArgs.headers(),
                    parsedArgs.bodyFileBytes()
            );
        } else {
            httpRequest = new SerializableHttpRequest(
                    parsedArgs.serverToTestURI(),
                    parsedArgs.httpMethod(),
                    parsedArgs.headers()
            );
        }
        return httpRequest;
    }
}
