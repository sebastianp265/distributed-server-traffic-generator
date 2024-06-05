package edu.pw.parser;

import org.apache.commons.cli.*;

public class ArgsParser {

    private ArgsParser() {

    }

    public static ParsedArgs parse(String[] args) throws ParseException {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        Integer port = commandLine.getParsedOptionValue("p");

        return new ParsedArgs(port);
    }

    private static Options getOptions() {
        Option portOption = new Option(
                "p",
                "port",
                true,
                "Port of the worker"
        );
        portOption.setRequired(true);
        portOption.setType(Integer.class);

        Options options = new Options();
        options.addOption(portOption);
        return options;
    }

}
