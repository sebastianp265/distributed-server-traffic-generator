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
            workerURIs.add(URI.create(workerURIString));
        }

        URI serverAddress = URI.create(commandLine.getOptionValue("s"));

        return new ParsedArgs(workerURIs, serverAddress);
    }

    private static Options getOptions() {
        Option workersOption = new Option(
                "w",
                "workers",
                true,
                "List of worker URI's"
        );
        workersOption.setArgs(Option.UNLIMITED_VALUES);
        workersOption.setRequired(true);

        Option serverOption = new Option(
                "s",
                "server",
                true,
                "Server address to test"
        );
        serverOption.setRequired(true);

        Options options = new Options();
        options.addOption(workersOption);
        options.addOption(serverOption);
        return options;
    }

}
