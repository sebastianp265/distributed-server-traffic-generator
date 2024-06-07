package edu.pw.exceptions.parser;

import org.apache.commons.cli.ParseException;

public class HeadersParseException extends ParseException {

    public HeadersParseException(String originHeadersString) {
        super("Failed to parse headers from string: " + originHeadersString);
    }
}
