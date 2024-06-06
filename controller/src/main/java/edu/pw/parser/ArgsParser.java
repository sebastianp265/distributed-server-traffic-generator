package edu.pw.parser;

import org.apache.commons.cli.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

        String bodyString = commandLine.getOptionValue("b");

        return new ParsedArgs(workerURIs, serverURI, numOfRequests, httpMethod, bodyString);
    }

    public static boolean isHelpOptionPresent(String[] args) {
        Option helpOption = getHelpOption();

        Options options = new Options();
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            return commandLine.hasOption("h");
        } catch (ParseException e) {
            return false;
        }
    }

    private static Option getHelpOption() {
        return new Option(
                "h",
                "help",
                false,
                "print this message"
        );
    }

    public static Options getOptions() {
        Option helpOption = getHelpOption();

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

        Options options = new Options();
        options.addOption(helpOption);
        options.addOption(workersOption);
        options.addOption(serverOption);
        options.addOption(numOfRequestsOption);
        options.addOption(httpMethodOption);
        options.addOption(bodyStringOption);

        return options;
    }

}
