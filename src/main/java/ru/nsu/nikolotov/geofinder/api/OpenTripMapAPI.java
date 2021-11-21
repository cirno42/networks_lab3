package ru.nsu.nikolotov.geofinder.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.nsu.nikolotov.geofinder.responses.OtmPlacesResponse;
import ru.nsu.nikolotov.geofinder.responses.OtmPlaceInfo;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class OpenTripMapAPI {
    public static CompletableFuture<OtmPlacesResponse> getInterestingPlaces(double lat, double lng, int radius, String key) {
        String latS = Double.toString(lat).replace(',', '.');
        String lngS = Double.toString(lng).replace(',', '.');
        String uriString = String.format("https://api.opentripmap.com/0.1/en/places/radius?radius=%d&lon=%s&lat=%S&apikey=%s",
                radius,
                lngS,
                latS,
                key);

        URI uri = URI.create(uriString);
        return doAndParseRequestGetPlaces(uri);

    }

    public static CompletableFuture<OtmPlaceInfo> getInfoAboutPlace(String xid, String key) {
        String apikey = "5ae2e3f221c38a28845f05b6e5df804b3e3ff74134a880b27bbbe489";
        String uriString = String.format("https://api.opentripmap.com/0.1/en/places/xid/%s?apikey=%s",
                xid,
                apikey);

        URI uri = URI.create(uriString);
        return doAndParseRequestGetInfo(uri);
    }

    private static CompletableFuture<OtmPlaceInfo> doAndParseRequestGetInfo(URI uri) {
        HttpClient httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(resp -> {
            if (resp.statusCode() != 200) {
                JOptionPane.showMessageDialog(null, "ERROR: Status code of Open Trip map (info) response isn't 200!");
            }
            return resp;
            })
                .thenApply(HttpResponse::body)
                .thenApply(OpenTripMapAPI::parseRequestInfo);

    }
    private static CompletableFuture<OtmPlacesResponse> doAndParseRequestGetPlaces(URI uri) {
        HttpClient httpClient = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(resp -> {
            if (resp.statusCode() != 200) {
                JOptionPane.showMessageDialog(null, "ERROR: Status code of Open Trip map (places) response isn't 200!");
            }
            return resp;
        })
                .thenApply(HttpResponse::body)
                .thenApply(OpenTripMapAPI::parseRequestPlaces);
    }

    private static OtmPlacesResponse parseRequestPlaces(String rawResp) {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            var parsedResp = objectMapper.readValue(rawResp, OtmPlacesResponse.class);
            return parsedResp;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Can't parse open trip map (places) response!");
            e.printStackTrace();
            return null;
        }
    }

    private static OtmPlaceInfo parseRequestInfo(String rawResp) {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            var parsedResp = objectMapper.readValue(rawResp, OtmPlaceInfo.class);
            return parsedResp;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: Can't parse open trip map (info) response!");
            e.printStackTrace();
            return null;
        }
    }
}
