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
    public static CompletableFuture<OpenWeatherMapResponse> getWeatherByCordsOrNull(double lat, double lng, String key) {
        String uriString = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s",lat, lng, key);
        URI uri = URI.create(uriString);

        return APIUtils.doAndParseRequest(uri, OpenWeatherMapResponse.class,"ERROR: Status code of Open Weather Map response isn't 200!");
    }
}
