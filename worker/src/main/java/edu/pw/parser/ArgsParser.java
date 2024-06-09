package edu.pw.parser;

import org.apache.commons.cli.*;

public class ArgsParser {

    private ArgsParser() {

    }

    public static boolean isHelpOptionPresent(String[] args) {
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
                return true;
            }
        }

        return false;
    }

    public static ParsedArgs parse(String[] args) throws ParseException {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        Integer port = commandLine.getParsedOptionValue("p");

        return new ParsedArgs(port);
    }

    public static Options getOptions() {
        Option portOption = new Option(
                "p",
                "port",
                true,
                "Port of the worker"
        );
        portOption.setRequired(true);
        portOption.setType(Integer.class);

        Option helpOption = new Option(
                "h",
                "help",
                false,
                "print this message"
        );

        Options options = new Options();
        options.addOption(portOption);
        options.addOption(helpOption);
        return options;
    }

}
