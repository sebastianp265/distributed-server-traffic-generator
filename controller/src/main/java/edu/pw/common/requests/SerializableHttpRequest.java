package edu.pw.common.requests;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SerializableHttpRequest extends HttpRequest implements Serializable {

    private final URI uri;
    private final String method;
    private final Map<String, List<String>> headerMap;

    private String bodyString;
    private byte[] bodyBytes;

    public SerializableHttpRequest(URI uri, String method, Map<String, List<String>> headerMap, String bodyString) {
        this.uri = uri;
        this.method = method;
        this.headerMap = headerMap;
        this.bodyString = bodyString;
    }

    public SerializableHttpRequest(URI uri, String method, Map<String, List<String>> headerMap, byte[] bodyBytes) {
        this.uri = uri;
        this.method = method;
        this.headerMap = headerMap;
        this.bodyBytes = bodyBytes;
    }

    public String getBodyString() {
        return bodyString;
    }

    public Map<String, List<String>> getHeaderMap() {
        return headerMap;
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public String method() {
        return method;
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(headerMap, (x, y) -> true);
    }

    @Override
    public Optional<BodyPublisher> bodyPublisher() {
        if (bodyString != null) {
            return Optional.of(BodyPublishers.ofString(bodyString));
        }
        if (bodyBytes != null) {
            return Optional.of(BodyPublishers.ofByteArray(bodyBytes));
        }
        return Optional.of(BodyPublishers.noBody());
    }

    @Override
    public Optional<Duration> timeout() {
        return Optional.empty();
    }

    @Override
    public boolean expectContinue() {
        return false;
    }

    @Override
    public Optional<HttpClient.Version> version() {
        return Optional.empty();
    }

}
