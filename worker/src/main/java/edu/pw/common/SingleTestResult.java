package edu.pw.common;

import java.io.Serializable;

public record SingleTestResult(String date,
                               String time,
                               String workerURI,
                               String requestProcessingTime) implements Serializable {

}
