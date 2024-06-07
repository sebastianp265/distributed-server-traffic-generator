package edu.pw.parser;

import edu.pw.exceptions.parser.HeadersParseException;
import org.apache.commons.cli.*;

import java.net.URI;
import java.util.*;

public class ArgsParser {

    private ArgsParser() {

    }

    public static ParsedArgs parse(String[] args) throws ParseException {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        String[] workerURIStrings = commandLine.getOptionValues("w");
        List<URI> workerURIs = new ArrayList<>(workerURIStrings.length);
        for (String workerURIString : workerURIStrings) {
            try {
                workerURIs.add(URI.create(workerURIString));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid worker URI: " + workerURIString, e);
            }
        }

        String serverURIString = commandLine.getOptionValue("s");
        URI serverURI;
        try {
            serverURI = URI.create(serverURIString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid server URI: " + serverURIString);
        }

        int numOfRequests = commandLine.getParsedOptionValue("n");

        String httpMethod = commandLine.getOptionValue("m");

        String headersString = commandLine.getOptionValue("H");
        Map<String, List<String>> headers = headersString != null ? getHeadersMap(headersString) : Collections.emptyMap();

        String bodyString = commandLine.getOptionValue("b");

        return new ParsedArgs(workerURIs, serverURI, numOfRequests, httpMethod, headers, bodyString);
    }

    private static Map<String, List<String>> getHeadersMap(String headersString) throws HeadersParseException {
        Map<String, List<String>> headersMap = new HashMap<>();

        headersString = headersString.trim();
        String[] headers = headersString.split(";");

        for (String header : headers) {
            String[] nameValuesPair = header.split(":");
            if (nameValuesPair.length != 2) {
                throw new HeadersParseException(headersString);
            }

            String name = nameValuesPair[0].trim();
            String[] values = nameValuesPair[1].trim().split(",");
            for (int j = 0; j < values.length; j++) {
                values[j] = values[j].trim();
            }

            headersMap.put(name, List.of(values));
        }

        return headersMap;
    }

    public static boolean isHelpOptionPresent(String[] args) {
        for(String arg : args) {
            if(arg.equals("-h") || arg.equals("--help")) {
                return true;
            }
        }

        return false;
    }

    public static Options getOptions() {
        Option helpOption = new Option(
                "h",
                "help",
                false,
                "print this message"
        );

        Option workersOption = new Option(
                "w",
                "workers",
                true,
                "List of worker URIs seperated by spaces"
        );
        workersOption.setArgs(Option.UNLIMITED_VALUES);
        workersOption.setRequired(true);
        workersOption.setArgName("worker URIs");

        Option serverOption = new Option(
                "s",
                "server",
                true,
                "Server URI to test"
        );
        serverOption.setRequired(true);
        serverOption.setType(URI.class);
        serverOption.setArgName("server URI");

        Option numOfRequestsOption = new Option(
                "n",
                "num-of-requests",
                true,
                "Number of requests to send"
        );
        numOfRequestsOption.setRequired(true);
        numOfRequestsOption.setType(Integer.class);
        numOfRequestsOption.setArgName("number of requests");

        Option httpMethodOption = new Option(
                "m",
                "method",
                true,
                "HTTP method of requests to send"
        );
        httpMethodOption.setRequired(true);
        httpMethodOption.setArgName("HTTP method");

        Option bodyStringOption = new Option(
                "b",
                "body",
                true,
                "Body of request to send"
        );
        bodyStringOption.setArgName("request body");

        Option headersStringOption = new Option(
                "H",
                "headers",
                true,
                "Headers of the request, seperated by semicolons"
        );
        headersStringOption.setArgName("headers");

        Options options = new Options();
        options.addOption(helpOption);
        options.addOption(workersOption);
        options.addOption(serverOption);
        options.addOption(numOfRequestsOption);
        options.addOption(httpMethodOption);
        options.addOption(bodyStringOption);
        options.addOption(headersStringOption);

        return options;
    }

}
