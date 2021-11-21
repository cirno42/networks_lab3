package ru.nsu.nikolotov.geofinder.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.nikolotov.geofinder.responses.GeoCodingResponse;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphHopperAPI {
    public static CompletableFuture<GeoCodingResponse> getAddressesByName(String name, String key) {
        String uriString = String.format("https://graphhopper.com/api/1/geocode?q=%s&locale=en&key=%s",
                name,
                key);

        URI uri = URI.create(uriString);
        return doAndParseRequest(uri);
    }

    private static CompletableFuture<GeoCodingResponse> doAndParseRequest(URI uri) {
        HttpClient httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(resp -> {
            if (resp.statusCode() != 200) {
                JOptionPane.showMessageDialog(null, "ERROR: Status code of geocode response isn't 200!");
            }
            return resp;
        })
                .thenApply(HttpResponse::body)
                .thenApply(GraphHopperAPI::parseRequest);
    }

    private static GeoCodingResponse parseRequest(String rawResp) {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            var parsedResp = objectMapper.readValue(rawResp, GeoCodingResponse.class);
            return parsedResp;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Can't parse geocoding response!");
            e.printStackTrace();
            return null;
        }
    }
}
