package edu.pw.parser;

import java.net.URI;
import java.util.List;

public record ParsedArgs(List<URI> workerURIs,
                         URI serverToTestURI,
                         int numOfRequests,
                         String httpMethod,
                         String bodyString) {

}
