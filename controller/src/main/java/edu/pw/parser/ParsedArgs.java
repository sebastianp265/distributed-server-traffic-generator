package edu.pw.parser;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

public record ParsedArgs(List<URI> workerURIs,
                         URI serverToTestURI,
                         int numOfRequests,
                         String httpMethod,
                         Map<String, List<String>> headers,
                         String bodyString,
                         byte[] bodyFileBytes,
                         File outputFile) {

}
