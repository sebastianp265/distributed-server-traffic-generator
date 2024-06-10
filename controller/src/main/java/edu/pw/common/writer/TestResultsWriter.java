package edu.pw.common.writer;

import edu.pw.common.SingleTestResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestResultsWriter {

    public static void writeToFile(List<SingleTestResult> testResults, File outputFile) throws IOException {
        try (FileWriter fileWriter = new FileWriter(outputFile); BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write("date;time;worker URI;request processing time (ms); status code; response\n");
            for (SingleTestResult singleTestResult : testResults) {
                writer.write(singleTestResult.date() + ";" +
                        singleTestResult.time() + ";" +
                        singleTestResult.workerURI() + ";" +
                        singleTestResult.requestProcessingTime() + ";" +
                        singleTestResult.statusCode() + ";" +
                        singleTestResult.response() + "\n"
                );
            }
        }
    }
}
