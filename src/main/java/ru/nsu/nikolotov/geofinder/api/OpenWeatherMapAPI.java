package ru.nsu.nikolotov.geofinder.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.nikolotov.geofinder.responses.GeoCodingResponse;
import ru.nsu.nikolotov.geofinder.responses.OpenWeatherMapResponse;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OpenWeatherMapAPI {
    public static CompletableFuture<OpenWeatherMapResponse> getWeatherByCords(double lat, double lng, String key) {
        String uriString = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s",lat, lng, key);
        URI uri = URI.create(uriString);

        return doAndParseRequest(uri);
    }
    private static CompletableFuture<OpenWeatherMapResponse> doAndParseRequest(URI uri) {
        HttpClient httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(resp -> {
            if (resp.statusCode() != 200) {
                JOptionPane.showMessageDialog(null, "ERROR: Status code of Open Weather Map response isn't 200!");
            }
            return resp;
        })
                .thenApply(HttpResponse::body)
                .thenApply(OpenWeatherMapAPI::parseRequest);
    }

    private static OpenWeatherMapResponse parseRequest(String rawResp) {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            var parsedResp = objectMapper.readValue(rawResp, OpenWeatherMapResponse.class);
            return parsedResp;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Can't parse open weather map response!");
            e.printStackTrace();
            return null;
        }
    }
}
