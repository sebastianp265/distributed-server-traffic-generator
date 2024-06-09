package edu.pw.parser;

import edu.pw.exceptions.parser.HeadersParseException;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;

public class ArgsParser {

    private ArgsParser() {

    }

    public static ParsedArgs parse(String[] args) throws ParseException, IOException {
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

        if (numOfRequests <= 0) {
            throw new IllegalArgumentException("Number of requests must be greater than 0");
        }

        String httpMethod = commandLine.getOptionValue("m");

        String headersString = commandLine.getOptionValue("H");
        Map<String, List<String>> headers = headersString != null ? getHeadersMap(headersString) : new HashMap<>();

        if (commandLine.hasOption("t") && commandLine.hasOption("f")) {
            throw new ParseException("You can send either text or a file, not both");
        }
        String bodyString = commandLine.getOptionValue("t");

        byte[] bodyFileBytes = null;
        if (commandLine.hasOption("f")) {
            File bodyFile = commandLine.getParsedOptionValue("f");
            String boundary = String.valueOf(UUID.randomUUID());
            headers.put("Content-Type", List.of("multipart/form-data; boundary=" + boundary));
            try {
                bodyFileBytes = buildMultipartBody(Files.readAllBytes(bodyFile.toPath()), bodyFile.getName(), boundary);
            } catch (IOException e) {
                throw new ParseException("Failed to read file: \n" + e.getMessage());
            }
        }

        return new ParsedArgs(workerURIs, serverURI, numOfRequests, httpMethod, headers, bodyString, bodyFileBytes);
    }

    private static byte[] buildMultipartBody(byte[] fileContent, String fileName, String boundary) {
        String boundaryDelimiter = "--" + boundary + "\r\n";
        String endBoundaryDelimiter = "\r\n--" + boundary + "--\r\n";

        String filePartHeader = "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n";

        byte[] boundaryBytes = boundaryDelimiter.getBytes();
        byte[] filePartHeaderBytes = filePartHeader.getBytes();
        byte[] endBoundaryBytes = endBoundaryDelimiter.getBytes();

        int contentLength = boundaryBytes.length + filePartHeaderBytes.length + fileContent.length + endBoundaryBytes.length;

        byte[] multipartBody = new byte[contentLength];
        int offset = 0;
        System.arraycopy(boundaryBytes, 0, multipartBody, offset, boundaryBytes.length);
        offset += boundaryBytes.length;
        System.arraycopy(filePartHeaderBytes, 0, multipartBody, offset, filePartHeaderBytes.length);
        offset += filePartHeaderBytes.length;
        System.arraycopy(fileContent, 0, multipartBody, offset, fileContent.length);
        offset += fileContent.length;
        System.arraycopy(endBoundaryBytes, 0, multipartBody, offset, endBoundaryBytes.length);

        return multipartBody;
    }

    private static Map<String, List<String>> getHeadersMap(String headersString) throws HeadersParseException {
        Map<String, List<String>> headersMap = new HashMap<>();

        headersString = headersString.trim();
        String[] headerLines = headersString.split("\\r?\\n");

        for (String headerLine : headerLines) {
            String[] nameValuesPair = headerLine.split(":");
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
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
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
                "t",
                "text",
                true,
                "Textual representation of request body to send"
        );
        bodyStringOption.setArgName("textual request body");

        Option bodyFileOption = new Option(
                "f",
                "file",
                true,
                "Path of the file to send"
        );
        bodyFileOption.setType(File.class);

        Option headersStringOption = new Option(
                "H",
                "headers",
                true,
                "Headers of the request in standard HTTP format"
        );
        headersStringOption.setArgName("headers");

        Options options = new Options();
        options.addOption(helpOption);
        options.addOption(workersOption);
        options.addOption(serverOption);
        options.addOption(numOfRequestsOption);
        options.addOption(httpMethodOption);
        options.addOption(bodyStringOption);
        options.addOption(bodyFileOption);
        options.addOption(headersStringOption);

        return options;
    }

}
